package com.example.sarwan.renkar.modules.chat.messaging

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.firebase.FirebaseExtras
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.fragments.ConfirmationFragment
import com.example.sarwan.renkar.model.History
import com.example.sarwan.renkar.model.chat.ChatRooms
import com.example.sarwan.renkar.model.chat.Message
import com.example.sarwan.renkar.modules.history.HistoryHelper
import com.example.sarwan.renkar.modules.lister.ListerActivity
import com.example.sarwan.renkar.modules.renter.PeriodFragment
import com.example.sarwan.renkar.utils.ModelMappingUtility
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.chat_fragment.*
import java.util.*
import kotlin.collections.ArrayList

open class ChattingBaseFragment : Fragment(), EventListener<QuerySnapshot> ,
    ConfirmationFragment.ConfirmationFragmentCallBack<Any>, PeriodFragment.PeriodFragmentCallBack, MessagesAdapter.MessagesAdapterCallBack{

    protected var adapter: MessagesAdapter? = null
    protected var chatRoom: ChatRooms ? = null
    protected var roomId = ""
    protected var TAG = "ChatFragment"
    protected var group = false
    protected lateinit var pActivity : ParentActivity
    protected var CHAT_ROOM = "CHAT_ROOM"
    protected var onlineMembers : ArrayList<String> = ArrayList()
    private var readMembers : ArrayList<String> = ArrayList()
    protected var confirmationFragment : ConfirmationFragment? = null
    protected var periodFragment : PeriodFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
        addOnlineListener()
    }

    fun initChat(chatRooms: ChatRooms){
        chatRoom = chatRooms
        makeChatScreen()
    }

    protected fun makeChatScreen() {
        initializeView()
        makeRoomId()
    }

    private fun makeRoomId() {
        chatRoom?.key?.let {
            //TODO - make roomId from Booknow buton in car details activity
            roomId = it
            chat_title.text = chatRoom?.chat_members?.find {find-> find.email!=pActivity.user?.email }?.name
            checkRoomExists()
        }
    }

    /**
     * It initialize recycler view with empty objects
     */
    private fun initializeView() {

        // Initialize recycler view
        recyclerChat?.layoutManager = LinearLayoutManager(pActivity)
        pActivity.user?.let {
            chatRoom?.let {chatRoom->
                adapter = MessagesAdapter(pActivity, java.util.ArrayList(), it, chatRoom, this)
                recyclerChat?.adapter = adapter
            }
        }

        //Request focus
        editWriteMessage?.requestFocus()

    }

    /**
     * @usage it checks the room exists on the firebase or we have to create it first. If exists then it load the messages data first
     */
    private fun checkRoomExists() {
        FirestoreQueryCenter.getMessageQuery(roomId).get().addOnSuccessListener { querySnapshot ->
            val messages = java.util.ArrayList<Message>()
            querySnapshot?.let {
                for (child in it) {
                    messages.add(child.toObject(Message::class.java))
                }
            }

            loadDataInList(messages)
            addDataChangeListener(roomId)
        }
    }

    private fun dataChangeListener(it: QuerySnapshot) {
        adapter?.let {adapter->
            if (it.size() > adapter.itemCount) {
                val list = it.toObjects(Message::class.java)
                list.sortByDescending { it.timestamp }
                checkReceivingMessageType(list.first())
            }
        }
    }

    override fun onLastMessage(message: Message, position: Int) {
        checkReceivingMessageType(message)
    }

    private fun checkReceivingMessageType(message: Message) {
        message.confirmation_option?.let {
            checkMessageType(message, it)
        } ?:kotlin.run {
            addMessageToView(message)
        }
    }

    private fun addMessageToView(message: Message) {
        adapter?.addItem(message)
        scrollToLastPosition()
    }

    private fun loadDataInList(messages: java.util.ArrayList<Message>) {
        adapter?.swapList(messages)
        scrollToLastPosition()
        if(!messages.isEmpty())
            addReadListener()
    }

    /**
     * @param first
     * @usage It sets the fields on firebase and make room id on the basis of who is first and second in the openent
     *          the big one is always be a first and the small one will be called as second.
     */
    private fun setChatRoomFields(message: Message) {
        chatRoom?.chat_members.let { members->
            members?.add(ModelMappingUtility.makeChatMember(pActivity.user))
            FirestoreQueryCenter.addChatRoom(roomId, ModelMappingUtility.createChatRoom(members,
                    chatRoom?.title, message.message, pActivity.user?.name, pActivity.user?.email,
                message.timestamp, chatRoom?.car_number, chatRoom?.carAvailabilityDays))
        }
    }

    protected fun makeMessageObject(content: String?): Message {
        val newMessage = Message()
        newMessage.message = content
        newMessage.sender_email = pActivity.user?.email
        newMessage.sender_name = pActivity.user?.name
        newMessage.timestamp = Calendar.getInstance().timeInMillis / 1000
        return newMessage
    }

    /**
    * @usage It checks the openent has been read the last message or not.
    *        If it is read or not it update the last message status accordingly
    */

    private fun addOnlineListener(){
        FirestoreQueryCenter.getOnline().addSnapshotListener(onlineListener)
    }

    private fun removeOnlineListener(){
        FirestoreQueryCenter.getOnline().addSnapshotListener(onlineListener).remove()
    }


    private val onlineListener = EventListener<DocumentSnapshot> { documentSnapshot, fireStoreException ->
        try {
            fireStoreException?.let {
                return@EventListener
            }?:kotlin.run {
                documentSnapshot?.let {query->
                    getOnlineStatus(query[FirebaseExtras.ONLINE])
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    private fun addReadListener(){
        FirestoreQueryCenter.getReadStatus(roomId).addSnapshotListener(readListener)
    }

    private fun removeReadListener(){
        FirestoreQueryCenter.getReadStatus(roomId).addSnapshotListener(readListener).remove()
    }

    private val readListener = EventListener<DocumentSnapshot> { documentSnapshot, fireStoreException ->
        try {
            fireStoreException?.let {
                return@EventListener
            }?:kotlin.run {
                documentSnapshot?.let {query->
                    getMemberStatus(query[FirebaseExtras.READ])
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    private fun getMemberStatus(read: Any?) {
        readMembers = read as ArrayList<String>
        updateLastMessageStatus(readMembers)
    }


    private fun updateLastMessageStatus(readStatus: ArrayList<String>) {
        if (readStatus.isEmpty())
            adapter?.updateLastMessageStatus("sent", readStatus)
        else
            adapter?.updateLastMessageStatus("read", readStatus)
    }

    private fun getOnlineStatus(list: Any?) {
        onlineMembers = list as ArrayList<String>
        onlineMembers.filter { it!=pActivity.user?.email }
        changeOnlineStatus()
    }


    private fun addDataChangeListener(roomId: String) {

        /* Listener for loading if new data or
           data changed on fire base fire store*/
        FirestoreQueryCenter.getMessageQuery(roomId).addSnapshotListener(this)
    }

    override fun onEvent(snapshot: QuerySnapshot?, p1: FirebaseFirestoreException?) {
        snapshot?.let {
            if (!it.isEmpty){
                dataChangeListener(it)
                readMessage()
            }
        }
    }

   private fun readMessage() {
        pActivity.user?.email?.let {
            FirestoreQueryCenter.setRead(roomId,it)
        }
    }

    private fun changeOnlineStatus(){
        if (onlineMembers.contains(chatRoom?.chat_members?.find { it.email!=pActivity.user?.email }?.email)){
            pActivity.show(online)
        }else
            pActivity.hide(online)
    }

    override fun onDetach() {
        super.onDetach()
        if (roomId.isNotEmpty()){
            removeOnlineListener()
            removeReadListener()
        }
    }

    override fun onPause() {
        super.onPause()
        if (roomId.isNotEmpty()){
            removeOnlineListener()
            removeReadListener()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (roomId.isNotEmpty()){
            removeOnlineListener()
            removeReadListener()
        }
    }

    override fun onResume() {
        super.onResume()
        if (roomId.isNotEmpty()){
            readMessage()
        }
    }

    private fun scrollToLastPosition() {
        var position = adapter?.itemCount ?: kotlin.run { 1 }
        position -= 1
        recyclerChat?.scrollToPosition(position)
    }

    protected fun attachDialogFragment(type: ConfirmationFragment.Companion.ConfirmationType) {
        ConfirmationFragment.newInstance(type.ordinal).run {
            confirmationFragment = this
            confirmationFragment?.initListener(this@ChattingBaseFragment)
            if (!isAdded){
                createManager()?.let { manager->confirmationFragment?.show(manager, type.name) }
            }
        }
    }

    protected fun attachDaysFragment() {
        PeriodFragment.newInstance(chatRoom?.carAvailabilityDays as java.util.ArrayList<Int>).run {
            periodFragment = this
            periodFragment?.initListener(this@ChattingBaseFragment)
            if (!isAdded)
                createManager()?.let { show(it, periodFragment.toString()) }
        }
    }

    override fun onSelection(period: String) {
        chatRoom?.timePeriod = period
    }

    private fun createManager(): FragmentManager? {
         return fragmentManager?.beginTransaction().run {
                    fragmentManager?.findFragmentByTag(ConfirmationFragment.Companion.ConfirmationType.OK.name)?.let { this?.remove(it) }
                    fragmentManager?.findFragmentByTag(ConfirmationFragment.Companion.ConfirmationType.DONE.name)?.let { this?.remove(it) }
                /*run {

//                    addToBackStack(null)
                }*/
            fragmentManager
        }
    }


    protected fun performMessageSendingTasks(message: Message){
        setLastMessageOfChatRoom(message)
        //addItemInAdapter(message)
        sendMessage(message)
    }

    private fun sendMessage(message: Message) {
        FirestoreQueryCenter.getMessageQuery(roomId).add(message).addOnCompleteListener {
            if (it.isSuccessful){
                //adapter?.updateLastMessageStatus("sent",null)
            }
        }
    }
    private fun addItemInAdapter(message: Message) {
        adapter?.addItem(message)
        adapter?.updateLastMessageStatus("sending...", null)
        scrollToLastPosition()
    }

    private fun setLastMessageOfChatRoom(message: Message) {
        if (adapter?.itemCount == 0) setChatRoomFields(message)
        else {
            FirestoreQueryCenter.setLastMessageOfConversation(roomId, message.message ,
                pActivity.user?.name, pActivity.user?.email, chatRoom?.title)
        }
    }

    fun sendConfirmationMessageToFirebase(typeName : String, typeOrdinal: Int, optionOrdinal: Int,
                                          name : String = pActivity.user?.name?.let { it }?:"" , email : String = pActivity.user?.email?.let { it }?:"") {

        if (name.isNotEmpty() && email.isNotEmpty())
            performMessageSendingTasks(Message((typeName), email, name, typeOrdinal, optionOrdinal))
    }

    /*
    * firebase Confirmation type message callback
    * */
    private fun checkMessageType(message: Message, option: Int) {
        message.apply {
            when(confirmation_type){
                ConfirmationFragment.Companion.ConfirmationType.OK.ordinal->{
                    when(confirmation_option){
                        ConfirmationFragment.Companion.ConfirmationOption.RENTER_BOOK.ordinal->{
                            if (pActivity.user?.type == ApplicationConstants.LISTER){
                                attachDialogFragment(ConfirmationFragment.Companion.ConfirmationType.DONE)
                            }
                        }
                    }
                }
                ConfirmationFragment.Companion.ConfirmationType.DONE.ordinal->{
                    when(confirmation_option){
                        ConfirmationFragment.Companion.ConfirmationOption.LISTER_BOOK.ordinal->{
                            pActivity.showMessage("Renter's request to book a car has been accepted")
                            Handler().postDelayed({
                                pActivity.openActivityWithFinish(Intent(pActivity, ListerActivity::class.java)
                                    .putExtra(ApplicationConstants.PAGER_NUMBER, 2))
                            },2000L)
                        }

                        ConfirmationFragment.Companion.ConfirmationOption.LISTER_END.ordinal->{
                            if (pActivity.user?.type == ApplicationConstants.RENTER){
                                pActivity.showMessage("Lister denied for booking")
                            }else
                                pActivity.showMessage("You denied for booking")
                        }
                    }
                }
            }
        }
    }

    /*
    Confirmation fragment call back
    */
    override fun onAction(type: Any, option: Any) {
        when(type as Int){
            ConfirmationFragment.Companion.ConfirmationType.OK.ordinal->{
                when(option as Int){
                    ConfirmationFragment.Companion.ConfirmationOption.RENTER_BOOK.ordinal->{
                        Toast.makeText(pActivity, getString(R.string.ok_msg), Toast.LENGTH_LONG).show()
                        sendConfirmationMessageToFirebase(ConfirmationFragment.Companion.ConfirmationType.OK.name, type, option)
                        HistoryHelper.create(ModelMappingUtility.createHistory(chatRoom, pActivity.user, History.STATUS.REQUEST_PENDING))
                    }
                    ConfirmationFragment.Companion.ConfirmationOption.RENTER_END.ordinal->{
                        pActivity.finish()
                    }
                }
            }
            ConfirmationFragment.Companion.ConfirmationType.DONE.ordinal->{
                when(option as Int){
                    ConfirmationFragment.Companion.ConfirmationOption.LISTER_BOOK.ordinal->{
                        sendConfirmationMessageToFirebase(ConfirmationFragment.Companion.ConfirmationType.DONE.name, type, option)
                        HistoryHelper.create(ModelMappingUtility.createHistory(chatRoom, pActivity.user, History.STATUS.REQUEST_APPROVED))
                    }
                    ConfirmationFragment.Companion.ConfirmationOption.LISTER_END.ordinal->{
                        sendConfirmationMessageToFirebase(ConfirmationFragment.Companion.ConfirmationOption.DENY.name, type, option)
                        HistoryHelper.create(ModelMappingUtility.createHistory(chatRoom, pActivity.user, History.STATUS.REQUEST_DECLINED))
                    }
                }
            }
        }
    }
}
