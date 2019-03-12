package com.example.sarwan.renkar.modules.chat.messaging

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.firebase.FirebaseExtras
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.fragments.ConfirmationFragment
import com.example.sarwan.renkar.model.chat.ChatRooms
import com.example.sarwan.renkar.model.chat.Message
import com.example.sarwan.renkar.modules.renter.PeriodFragment
import com.example.sarwan.renkar.modules.send.message.SendMessage
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.chat_fragment.*
import java.util.*
import kotlin.collections.ArrayList

open class ChattingBaseFragment : Fragment(), EventListener<QuerySnapshot> ,
    ConfirmationFragment.ConfirmationFragmentCallBack<Any>, PeriodFragment.PeriodFragmentCallBack,
    MessagesAdapter.MessagesAdapterCallBack,
        SendMessage.SendMessageCallBack
{

    protected var adapter: MessagesAdapter? = null
    protected var chatRoom: ChatRooms ? = null
    private var roomId = ""
    protected var TAG = "ChatFragment"
    protected lateinit var pActivity : ParentActivity
    protected var CHAT_ROOM = "CHAT_ROOM"
    private var onlineMembers : ArrayList<String> = ArrayList()
    private var readMembers : ArrayList<String> = ArrayList()
    protected lateinit var sendMessage : SendMessage
    protected lateinit var lastMessage : Any
    protected var lastMessageTime : Long ? = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
        sendMessage = SendMessage(pActivity, this)
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
            roomId = it
            sendMessage.setRoomId(it)
            chatRoom?.let { chatRoom ->  sendMessage.setChatRoom(chatRoom) }
            makeChatTitleBar()
            checkRoomExists()
        }
    }

    private fun makeChatTitleBar() {
        chat_title.text = """${chatRoom?.chat_members?.find {find-> find.email!=pActivity.user?.email }?.name} (${chatRoom?.car_number})"""
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
        checkLastMessageType(message)
    }

    private fun checkReceivingMessageType(message: Message) {
        lastMessage = message.message as Any
        lastMessageTime = message.timestamp
        message.confirmation_option?.let {
            sendMessage.checkMessageType(message)
        }
        addMessageToView(message)
    }

    private fun checkLastMessageType(message: Message){
        message.confirmation_option?.let {
            sendMessage.checkMessageType(message)
        }
    }

    private fun addMessageToView(message: Message) {
        adapter?.addItem(message)
        scrollToLastPosition()
    }

    private fun loadDataInList(messages: java.util.ArrayList<Message>) {
        adapter?.swapList(messages)
        scrollToLastPosition()
        if(!messages.isEmpty()){
            lastMessage = messages.last().message as Any
            lastMessageTime = messages.last().timestamp
            addReadListener()
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

    private fun removeDataChangeListener(roomId: String){
        FirestoreQueryCenter.getMessageQuery(roomId).addSnapshotListener(this).remove()
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


    private fun scrollToLastPosition() {
        var position = adapter?.itemCount ?: kotlin.run { 1 }
        position -= 1
        recyclerChat?.scrollToPosition(position)
    }

    protected fun attachDialogFragment(type: ConfirmationFragment.Companion.ConfirmationType) {
        fragmentManager?.let { ConfirmationFragment.getInstance(type.ordinal)?.initListener(this)?.
            show( it, type.name) }
    }

    protected fun attachDaysFragment() {
        fragmentManager?.let { PeriodFragment.getInstance(chatRoom?.carAvailabilityDays as java.util.ArrayList<Int>)?.initListener(this)?.
            show( it, PeriodFragment::class.java.simpleName) }
    }

    override fun onSelection(period: String) {
        chatRoom?.timePeriod = period
    }


    override fun attachFragmentRequest(type: ConfirmationFragment.Companion.ConfirmationType) {
        attachDialogFragment(type)
    }

    /*
    Confirmation fragment call back
    */
    override fun onAction(type: Any, option: Any) {
        when(type as Int){
            ConfirmationFragment.Companion.ConfirmationType.OK.ordinal->{
                sendMessage.setAdapterCount(adapter?.itemCount == 0).takeOkConfirmationAction(type, option as Int)
            }
            ConfirmationFragment.Companion.ConfirmationType.DONE.ordinal->{
                sendMessage.setAdapterCount(adapter?.itemCount == 0).takeDoneConfirmationAction(type, option as Int)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (roomId.isNotEmpty()){
            removeOnlineListener()
            removeReadListener()
            removeDataChangeListener(roomId)
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
            removeDataChangeListener(roomId)
        }
    }

    override fun onResume() {
        super.onResume()
        if (roomId.isNotEmpty()){
            readMessage()
        }
    }

}
