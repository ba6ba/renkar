package com.mobitribe.qulabro.modules.chat.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.mobitribe.qulabro.extras.AlertDialogBuilder
import com.mobitribe.qulabro.extras.DialogListener
import com.mobitribe.qulabro.models.chat.Message
import com.example.sarwan.renkar.base.ParentActivity
import com.mobitribe.qulabro.models.chat.ChatRooms
import com.mobitribe.qulabro.utils.ModelMappingUtility
import firebase.VFirestoreQueryManager
import kotlinx.android.synthetic.main.chat_fragment.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class ChatBaseFragment : androidx.fragment.app.Fragment(), DialogListener, EventListener<QuerySnapshot> {

    protected var adapter: ListMessageAdapter? = null
    protected var chatModel: ChatRooms? = null
    protected var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
    protected var roomId = ""
    protected var TAG = "ChatFragment"
    protected var group = false
    protected lateinit var pActivity : ParentActivity
    protected var CHAT_ROOM = "CHAT_ROOM"
    var blockUserId: Int? = 0
    var isOpponentOnline = false
    var isOpponentLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
    }

    fun initChat(chatRooms: ChatRooms){
        chatModel = chatRooms
        makeChatScreen()
    }

    protected fun makeChatScreen() {
        initializeView()
        makeRoomId()
    }

    private fun makeRoomId() {
        chatModel?.key?.let {
            roomId = it
            checkRoomExists()
        }
    }

    /**
     * It initialize recycler view with empty objects
     */
    private fun initializeView() {

        // Initialize recycler view
        linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(pActivity)
        recyclerChat?.layoutManager = linearLayoutManager!!
        adapter = ListMessageAdapter(pActivity, java.util.ArrayList(), pActivity.profile!!, chatModel!!)
        recyclerChat?.adapter = adapter

        //Request focus
        editWriteMessage!!.requestFocus()

    }

    /**
     * @usage it checks the room exists on the firebase or we have to create it first. If exists then it load the messages data first
     */
    private fun checkRoomExists() {
        VFirestoreQueryManager.getMessageQuery(roomId).get().addOnSuccessListener { querySnapshot ->
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
                if (list.first().sender_id!=pActivity.profile?.id){
                    adapter.addItem(list.first())
                    recyclerChat?.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun loadDataInList(messages: java.util.ArrayList<Message>) {
        adapter?.let {
            it.swapList(messages)
            recyclerChat?.scrollToPosition(it.itemCount - 1)
        }
        if(!messages.isEmpty())
            checkMembersReadStatus()
    }

    /**
     * @param first
     * @usage It sets the fields on firebase and make room id on the basis of who is first and second in the openent
     *          the big one is always be a first and the small one will be called as second.
     */
    protected fun setChatRoomFields(message: Message) {
        chatModel?.chat_members?.let { members->
            members.add(ModelMappingUtility.makeChatMember(pActivity.profile))
            VFirestoreQueryManager.addChatRoom(roomId, ModelMappingUtility.createChatRoom(members,
                    chatModel?.title, message.message, pActivity.profile?.userName, pActivity.profile?.id, message.timestamp))
        }
    }

    protected fun makeMessageObject(content: String?): Message {
        val newMessage = Message()
        newMessage.message = content
        newMessage.sender_id = pActivity.profile?.id
        newMessage.sender_name = pActivity.profile?.userName
        newMessage.timestamp = Calendar.getInstance().timeInMillis / 1000
        return newMessage
    }

    /**
    * @usage It checks the openent has been read the last message or not.
    *        If it is read or not it update the last message status accordingly
    */
    protected fun checkMembersReadStatus() {
        VFirestoreQueryManager.getMembersReadStatus(roomId).addSnapshotListener(statusDocumentListener)
    }

    private val statusDocumentListener = EventListener<DocumentSnapshot> { documentSnapshot, fireStoreException ->
        try {
            fireStoreException?.let {
                return@EventListener
            }?:kotlin.run {
                documentSnapshot?.let {query->
                    getMemberStatus(query.data!!)
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    private fun getMemberStatus(data: MutableMap<String, Any>) {
        val readStatus : ArrayList<Int> = ArrayList()
        for (i in data.keys){
            try {
                if(data[i] !is String && data[i] !is Long && data[i]!=null && i!=pActivity.profile?.id.toString()){
                    data[i]?.let {member->
                        val isRead = (member as HashMap<String, String>)[VFirestoreQueryManager.CHAT_USER_READ].toString().toBoolean()
                        val memberId = (member)[VFirestoreQueryManager.CHAT_USER_ID].toString().toInt()
                        isRead.let { read ->
                            if (isRead)
                                readStatus.add(memberId)
                        }
                    }
                }
            }catch (e : Exception){
                e.localizedMessage
            }
        }
        updateLastMessageStatus(readStatus)
    }



    private fun updateLastMessageStatus(readStatus: ArrayList<Int>) {
        if (readStatus.isEmpty())
            adapter?.updateLastMessageStatus("sent", readStatus)
        else
            adapter?.updateLastMessageStatus("read", readStatus)
    }


    private fun addDataChangeListener(roomId: String) {

        /* Listener for loading if new data or
           data changed on fire base fire store*/
        VFirestoreQueryManager.getMessageQuery(roomId).addSnapshotListener(this)
    }

    override fun onEvent(snapshot: QuerySnapshot?, p1: FirebaseFirestoreException?) {
        snapshot?.let {
            if (!it.isEmpty){
                dataChangeListener(it)
            }
        }
    }

    private fun addMessageHasbeenReadByMeListener() {
        VFirestoreQueryManager.myStatusListener(roomId).addSnapshotListener(myStatusListener)
    }

    private fun removeMessageHasbeenReadByMeListener() {
        VFirestoreQueryManager.myStatusListener(roomId).addSnapshotListener(myStatusListener).remove()
    }

    private fun removeMessageHasBeenReadByMemberStatus(){
        VFirestoreQueryManager.getMembersReadStatus(roomId).addSnapshotListener(statusDocumentListener).remove()
    }

    private val myStatusListener = EventListener<DocumentSnapshot> { documentSnapshot, fireStoreException ->
        try {
            fireStoreException?.let {
                return@EventListener
            }?:kotlin.run {
                documentSnapshot?.let {query->
                    VFirestoreQueryManager.readConversation(roomId, pActivity.profile?.id!!)
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    fun visibleBlockUserLayout() {
        blockedUser.visibility = View.VISIBLE
        bottom.visibility = View.INVISIBLE
    }

    fun invisibleBlockUserLayout() {
        blockedUser.visibility = View.INVISIBLE
        bottom.visibility = View.VISIBLE
    }

    fun showBlockedDialog() {
        val text = "Unblock " + chatModel?.title + " to send a message"
        AlertDialogBuilder.create(pActivity,"CONFIRMATION",text,false,this,"UNBLOCK","CANCEL")
    }

    private fun blockTheUser() {
        VFirestoreQueryManager.blockTheUser(roomId, getOpponentId())
    }

    private fun getOpponentId(): Int? {
        return chatModel?.chat_members?.filter { it.id!=pActivity.profile?.id }?.first()?.id
    }


    fun unBlockTheUser() {
        VFirestoreQueryManager.blockTheUser(roomId, 0)
    }

    fun showBlockWarning() {
        val text = "Block " + chatModel?.title + "? " + "Blocked contacts will no longer be able to send you messages"
        AlertDialogBuilder.create(pActivity,"CONFIRMATION",text,true,this,"BLOCK","CANCEL")
    }

    override fun onClick(action: Boolean) {
        if(action) blockTheUser()
        else unBlockTheUser()
    }

    override fun onDetach() {
        super.onDetach()
        if (roomId.isNotEmpty()){
            removeMessageHasBeenReadByMemberStatus()
            removeMessageHasbeenReadByMeListener()
        }
    }

    override fun onPause() {
        super.onPause()
        if (roomId.isNotEmpty()){
            removeMessageHasBeenReadByMemberStatus()
            removeMessageHasbeenReadByMeListener()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (roomId.isNotEmpty()){
            removeMessageHasBeenReadByMemberStatus()
            removeMessageHasbeenReadByMeListener()
        }
    }


    override fun onResume() {
        super.onResume()
        if (roomId.isNotEmpty())
            addMessageHasbeenReadByMeListener()
    }
}
