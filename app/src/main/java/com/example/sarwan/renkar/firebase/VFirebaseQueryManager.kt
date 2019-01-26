package firebase

import com.google.firebase.database.*
import kotlin.collections.HashMap

/**
 * Created by Muhammad Shahab on 8/8/16.
 */
object VFirebaseQueryManager {

    val CONVERSATION = "chat-rooms"
    val BLOCK_USER = "block_user"
    val CHAT = "chat-room-messages"
    val FIRST_ID = "first_id"
    val FIRST_READ = "first_read"
    val FIRST_USER_NAME= "first_user_name"
    val SECOND_USER_NAME= "second_user_name"
    val LAST_MESSAGE= "last_message"
    val UPDATED_TIME= "last_message_time"
    val SECOND_ID = "second_id"
    val SECOND_READ = "second_read"
    val LAST_MESSAGE_SENDER = "last_message_sender"
    val LAST_MESSAGE_SENDER_ID = "last_message_sender_id"
    val FIRST_USER_TOKEN: String = "first_user_token"
    val SECOND_USER_TOKEN: String = "second_user_token"
    val USER : String = "users"
    val ONLINE_STATUS : String = "online"
    val ONLINE_STAMP  : String = "last_online"
    val LOGIN  : String = "login"
    val ACTION : String = "action"
    val URL : String = "pexip_url"
    val ALIAS : String = "pexip_alias"
    val CALLER_ID : String = "caller_id"
    val RECEIVER_ID : String = "receiver_id"
    val CALL_TYPE : String = "call_type"
    val PIN : String = "pexip_pin"
    val CALL_START : String = "call_start"
    val PEXIP : String = "pexip"

    private val MESSAGES_LIMIT = 100

    val groupsQuery: Query
        get() = FirebaseDatabase.getInstance().reference.child(CONVERSATION)

    fun getConversationByFirstId(usedId: Double): Query {
        return FirebaseDatabase.getInstance().reference.child(CONVERSATION).orderByChild(FIRST_ID).equalTo(usedId)
    }

    fun getConversationBySecondId(usedId: Double): Query {
        return FirebaseDatabase.getInstance().reference.child(CONVERSATION).orderByChild(SECOND_ID).equalTo(usedId)
    }

    fun getMessageQuery(groupId: String): Query {
        return FirebaseDatabase.getInstance().reference.child(CHAT).child(groupId)
    }

    fun getMessageNode(): Query {
        return FirebaseDatabase.getInstance().reference.child(CHAT)
    }


    fun setFirstId(groupId: String, firstId: String?) {
        FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(groupId).child(FIRST_ID).setValue(firstId)
    }

    fun setSecondId(groupId: String, secondId: String?) {
        FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(groupId).child(SECOND_ID).setValue(secondId)
    }

    fun setLastMessageOfConversation(conversationId: String, lastMessage: String, first_second_read: String, sender_name: String, userId: String ) {
        val lastMessage_lastMessageData = HashMap<String, Any>()
        lastMessage_lastMessageData[LAST_MESSAGE] = lastMessage
        lastMessage_lastMessageData[UPDATED_TIME] = System.currentTimeMillis() / 1000
        lastMessage_lastMessageData[first_second_read] = false
        lastMessage_lastMessageData[LAST_MESSAGE_SENDER] = sender_name
        lastMessage_lastMessageData[LAST_MESSAGE_SENDER_ID] = userId
        FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(conversationId).updateChildren(lastMessage_lastMessageData)

    }

    fun getParticularChat(conversationId: String): Query {
        return FirebaseDatabase.getInstance().reference.child(CHAT).child(conversationId)

    }

    fun isEmployerTyping(conversationId: String): Query {
        return FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(conversationId).child("employer_is_typing")
    }

    fun getOponentReadStatus(conversationId: String, OPONENT_NODE: String): Query {
        return FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(conversationId).child(OPONENT_NODE)
    }

    fun setJobSeekerIsTyping(conversationId: String, isTyping: Boolean) {
        FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(conversationId).child("jobseeker_is_typing").setValue(isTyping)
    }

    fun sendMessageToEmployer(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child(CHAT).push().ref
    }

    fun blockTheUser(roomId: String ,id: Int?) {
        FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(roomId).child(BLOCK_USER).setValue(id)
    }

    fun checkIfBlockUser(roomId: String ):Query {
        return FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(roomId).child(BLOCK_USER)
    }

    fun setFirstUserName(roomId: String, contactsBookName: String?) {
        FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(roomId).child(FIRST_USER_NAME).setValue(contactsBookName)
    }

    fun setSecondUserName(roomId: String, contactsBookName: String?) {
        FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(roomId).child(SECOND_USER_NAME).setValue(contactsBookName)
    }

    fun setLastMessage(roomId: String, lastMessage: String) {
        FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(roomId).child(LAST_MESSAGE).setValue(lastMessage)
    }

    fun setUpdateTimeStamp(roomId: String, updateTime: Long) {
        FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(roomId).child(UPDATED_TIME).setValue(updateTime)
    }

    fun setReadTheConversation(roomId: String, first_second_read: String) {
        FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(roomId).child(first_second_read).setValue(true)
    }

    fun getReadTheConversationNode(roomId: String, first_second_read: String): DatabaseReference {
       return FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(roomId).child(first_second_read).ref
    }

    fun setFirebaseToken(roomId: String,firsT_USER_TOKEN: String, firebasE_TOKEN: String) {
        FirebaseDatabase.getInstance().reference.child(CONVERSATION).child(roomId).child(firsT_USER_TOKEN).setValue(firebasE_TOKEN)
    }

    fun getUserReference(userId: Long): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child(USER).child(userId.toString())
    }

    fun getPexipReference(userId: Long?): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child(USER).child(userId.toString()).child(PEXIP)
    }

    fun getConnectionReference(userId: Long?): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child(USER).child(userId.toString()).child(ONLINE_STATUS)
    }

    fun getLastOnlineTimeRef(userId: Long?): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child(USER).child(userId.toString()).child(ONLINE_STAMP)
    }

    fun getActionReference(userId: Long): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child(USER).child(userId.toString()).child(PEXIP).child(ACTION)
    }
}