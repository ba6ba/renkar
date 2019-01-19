package firebase

import com.google.firebase.firestore.*
import kotlin.collections.HashMap

/**
 * Created by Muhammad Shahab on 8/8/16.
 */
object VFirestoreQueryManager {

    val CONVERSATION = "chat-rooms"
    val BLOCK_USER = "block_user"
    val CHAT = "chat-room-messages"
    val LAST_MESSAGE= "last_message"
    val UPDATED_TIME= "last_message_time"
    val LAST_MESSAGE_SENDER = "last_message_sender"
    val LAST_MESSAGE_SENDER_ID = "last_message_sender_id"
    val CHAT_USER_NAME = "name"
    val CHAT_USER_READ = "read"
    val CHAT_USER_IMAGE = "image"
    val CHAT_USER_ID = "id"

    val LAST_MESSAGE_TIME = "last_message_time"
    val TITLE = "title"
    val MESSAGES = "messages"

    fun getMembersReadStatus(conversationId: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(CONVERSATION).document(conversationId)
    }

    fun blockTheUser(conversationId: String ,id: Int?) {
        val map : MutableMap<String, Any> = hashMapOf(BLOCK_USER to id.toString())
        FirebaseFirestore.getInstance().collection(CONVERSATION).document(conversationId).set(map, SetOptions.merge())
    }

    fun checkIfBlockUser(conversationId: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(CONVERSATION).document(conversationId)
    }

    fun getMessageQuery(conversationId: String) : CollectionReference {
        return FirebaseFirestore.getInstance().collection(CHAT).document(conversationId).collection(MESSAGES)
    }

    fun setLastMessageOfConversation(conversationId: String, lastMessage: String, sender_name: String, senderId: Int, title: String,
                                     userIds: ArrayList<Int>) {
        val lastMessageData = HashMap<String, Any>()
        lastMessageData[LAST_MESSAGE] = lastMessage
        lastMessageData[UPDATED_TIME] = System.currentTimeMillis() / 1000
        lastMessageData[LAST_MESSAGE_SENDER] = sender_name
        lastMessageData[LAST_MESSAGE_SENDER_ID] = senderId
        lastMessageData[TITLE] = title
        unReadConversation(conversationId, userIds, senderId)
        FirebaseFirestore.getInstance().collection(CONVERSATION).document(conversationId).update(lastMessageData)
    }

    fun addChatRoom(conversationId: String, map : HashMap<String, Any?>){
        FirebaseFirestore.getInstance().collection(CONVERSATION).document(conversationId).set(map)
    }

    fun readConversation(conversationId: String, id : Int){
        val ref = FirebaseFirestore.getInstance().collection(CONVERSATION).document(conversationId)
        FirebaseFirestore.getInstance().runTransaction {
            val snapshot = it.get(ref)
            val status = (snapshot.get(id.toString()) as HashMap<String, Boolean>).getValue(CHAT_USER_READ)
            if (!status) it.update(ref, "$id.$CHAT_USER_READ",true)
            null
        }
    }

    private fun unReadConversation(conversationId: String, usersId: ArrayList<Int>, senderId: Int){
        val ref = FirebaseFirestore.getInstance().collection(CONVERSATION).document(conversationId)
        FirebaseFirestore.getInstance().runTransaction {
        for (i in usersId) { it.update(ref, "$i.$CHAT_USER_READ",false) }
            it.update(ref, "$senderId.$CHAT_USER_READ",true)
            null
        }
    }

    fun myStatusListener(conversationId: String) : DocumentReference{
        return FirebaseFirestore.getInstance().collection(CONVERSATION).document(conversationId)
    }

    fun leaveGroup(conversationId: String, id : Int) {
        val map = hashMapOf<String, Any>(id.toString() to FieldValue.delete())
        FirebaseFirestore.getInstance().collection(CONVERSATION).document(conversationId).update(map)
    }

    fun deleteGroup(conversationId: String) {
        FirebaseFirestore.getInstance().collection(CONVERSATION).document(conversationId).delete()
    }


    fun getConversationById(userId: Int): com.google.firebase.firestore.Query {
        return FirebaseFirestore.getInstance().collection(CONVERSATION).orderBy(userId.toString())
    }

    fun getGroupConversation(chatRoom: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(CONVERSATION).document(chatRoom)
    }
}