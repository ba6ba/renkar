package com.example.sarwan.renkar.modules.chat.messaging

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.firebase.FirebaseExtras
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.fragments.ConfirmationFragment
import com.example.sarwan.renkar.model.chat.ChatRooms
import com.example.sarwan.renkar.model.chat.Message
import com.example.sarwan.renkar.utils.ModelMappingUtility
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.chat_fragment.*
import java.util.*
import kotlin.collections.ArrayList

open class ChattingBaseFragment : Fragment(), EventListener<QuerySnapshot> , ConfirmationFragment.ConfirmationFragmentCallBack<Any>{

    protected var adapter: MessagesAdapter? = null
    protected var chatRoom: ChatRooms ? = null
    protected var roomId = ""
    protected var TAG = "ChatFragment"
    protected var group = false
    protected lateinit var pActivity : ParentActivity
    protected var CHAT_ROOM = "CHAT_ROOM"
    var isOpponentOnline = false
    var isOpponentLogin = false
    protected var onlineMembers : ArrayList<String> = ArrayList()
    private var readMembers : ArrayList<String> = ArrayList()
    protected var confirmationFragment : ConfirmationFragment? = null


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
                adapter = MessagesAdapter(pActivity, java.util.ArrayList(), it, chatRoom)
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
        /*adapter?.let {adapter->
            if (it.size() > adapter.itemCount) {
                val list = it.toObjects(Message::class.java)
                list.sortByDescending { it.timestamp }
                if (list.first().sender_email!=pActivity.user?.email){
                    adapter.addItem(list.first())
                    recyclerChat?.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }*/
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
    protected fun setChatRoomFields(message: Message) {
        chatRoom?.chat_members.let { members->
            members?.add(ModelMappingUtility.makeChatMember(pActivity.user))
            FirestoreQueryCenter.addChatRoom(roomId, ModelMappingUtility.createChatRoom(members,
                    chatRoom?.title, message.message, pActivity.user?.name, pActivity.user?.email, message.timestamp))
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

    private fun getOpponentEmail(): String? {
        return chatRoom?.chat_members?.filter { it.email!=pActivity.user?.email }?.first()?.email
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

    protected fun scrollToLastPosition() {
        var position = adapter?.itemCount ?: kotlin.run { 1 }
        position -= 1
        recyclerChat?.scrollToPosition(position)
    }


    private fun checkMessageType(message: Message, option: Int) {
        when(option){
            ConfirmationFragment.Companion.ConfirmationOption.BOOK.ordinal-> {
                pActivity.user?.lister?.let {
                    attachDialogFragment(ConfirmationFragment.Companion.ConfirmationType.DONE)
                }
            }
            ConfirmationFragment.Companion.ConfirmationOption.ALLOW.ordinal-> {
                attachDialogFragment(ConfirmationFragment.Companion.ConfirmationType.DONE)
            }
        }
    }


    protected fun attachDialogFragment(type: ConfirmationFragment.Companion.ConfirmationType) {
        ConfirmationFragment.newInstance(type.ordinal).run {
            confirmationFragment = this
            confirmationFragment?.initListener(this@ChattingBaseFragment)
            if (!isAdded)
                show(createManager(), type.name)
        }
    }


    private fun createManager(): FragmentManager {
        childFragmentManager.beginTransaction().run {
            run {
                if ((childFragmentManager.findFragmentByTag(ConfirmationFragment.Companion.ConfirmationType.OK.name) != null))
                    childFragmentManager.findFragmentByTag(ConfirmationFragment.Companion.ConfirmationType.OK.name)?.let { this.remove(it) }
                else if ((childFragmentManager.findFragmentByTag(ConfirmationFragment.Companion.ConfirmationType.DONE.name) != null))
                    childFragmentManager.findFragmentByTag(ConfirmationFragment.Companion.ConfirmationType.DONE.name)?.let { this.remove(it) }
                addToBackStack(null)
            }
        }
        return childFragmentManager
    }

    override fun onAction(type: Any, option: Any) {
        if (option == ConfirmationFragment.Companion.ConfirmationOption.END.ordinal){
            pActivity.user?.lister?.let {
                sendConfirmationMessageToFirebase(type as ConfirmationFragment.Companion.ConfirmationType,
                    ConfirmationFragment.Companion.ConfirmationOption.DENY)
            }?:kotlin.run {
                pActivity.finish()
            }
        }else {
            pActivity.user?.renter?.let { pActivity.showProgress() }
            sendConfirmationMessageToFirebase(ConfirmationFragment.Companion.ConfirmationType.DONE, ConfirmationFragment.Companion.ConfirmationOption.BOOK)
        }
    }

    private fun sendConfirmationMessageToFirebase(type: ConfirmationFragment.Companion.ConfirmationType, option: ConfirmationFragment.Companion.ConfirmationOption) {
        pActivity.user?.apply {
            name?.let { name->
                email?.let {email->
                    performMessageSendingTasks(Message((type.name),
                        email, name, type.ordinal
                        , option.ordinal))
                }
            }
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
                adapter?.updateLastMessageStatus("sent",null)
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
}
