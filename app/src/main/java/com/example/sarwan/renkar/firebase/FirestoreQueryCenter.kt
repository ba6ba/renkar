package com.example.sarwan.renkar.firebase

import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.model.PaymentMethod
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlin.collections.HashMap


object FirestoreQueryCenter {

    val LISTER = "listers"
    val RENTER = "renters"
    val CARS = "cars"
    val USER = "users"
    val EMAIL = "email"
    val FIRST_NAME = "first_name"
    val LAST_NAME = "last_name"
    val PHONE_NO = "phone_no"
    val DATE_OF_BIRTH = "date_of_birth"
    val USER_NAME = "username"

    val TYPE = "type"


    fun getMembersReadStatus(conversationId: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(CARS).document(conversationId)
    }


    fun checkIfBlockUser(conversationId: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(CARS).document(conversationId)
    }

    fun getMessageQuery(conversationId: String) : CollectionReference {
        return FirebaseFirestore.getInstance().collection(CARS).document(conversationId).collection(CARS)
    }

    /*fun setLastMessageOfConversation(conversationId: String, lastMessage: String, sender_name: String, senderId: Int, name: String,
                                     userIds: ArrayList<Int>) {
        val lastMessageData = HashMap<String, Any>()
        lastMessageData[LAST_MESSAGE] = lastMessage
        lastMessageData[UPDATED_TIME] = System.currentTimeMillis() / 1000
        lastMessageData[LAST_MESSAGE_SENDER] = sender_name
        lastMessageData[LAST_MESSAGE_SENDER_ID] = senderId
        lastMessageData[TITLE] = name
        unReadConversation(conversationId, userIds, senderId)
        FirebaseFirestore.getInstance().collection(CARS).document(conversationId).update(lastMessageData)
    }
*/
    fun addChatRoom(conversationId: String, map : HashMap<String, Any?>){
        FirebaseFirestore.getInstance().collection(CARS).document(conversationId).set(map)
    }

    fun readConversation(conversationId: String, id : Int){
        val ref = FirebaseFirestore.getInstance().collection(CARS).document(conversationId)
        FirebaseFirestore.getInstance().runTransaction {
            val snapshot = it.get(ref)
            val status = (snapshot.get(id.toString()) as HashMap<String, Boolean>).getValue(CARS)
            if (!status) it.update(ref, "$id.$CARS",true)
            null
        }
    }

    private fun unReadConversation(conversationId: String, usersId: ArrayList<Int>, senderId: Int){
        val ref = FirebaseFirestore.getInstance().collection(CARS).document(conversationId)
        FirebaseFirestore.getInstance().runTransaction {
        for (i in usersId) { it.update(ref, "$i.$CARS",false) }
            it.update(ref, "$senderId.$CARS",true)
            null
        }
    }

    fun myStatusListener(conversationId: String) : DocumentReference{
        return FirebaseFirestore.getInstance().collection(CARS).document(conversationId)
    }

    fun leaveGroup(conversationId: String, id : Int) {
        val map = hashMapOf<String, Any>(id.toString() to FieldValue.delete())
        FirebaseFirestore.getInstance().collection(CARS).document(conversationId).update(map)
    }

    fun deleteGroup(conversationId: String) {
        FirebaseFirestore.getInstance().collection(CARS).document(conversationId).delete()
    }


    fun getConversationById(userId: Int): com.google.firebase.firestore.Query {
        return FirebaseFirestore.getInstance().collection(CARS).orderBy(userId.toString())
    }

    fun getGroupConversation(chatRoom: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(CARS).document(chatRoom)
    }

    fun addDataToListerNode(email: String, data: Any) : Task<Void>{
        return FirebaseFirestore.getInstance().collection(LISTER).document(email).set(data, SetOptions.merge())
    }

    fun addDataToRenterNode(email: String, data: Any) : Task<Void>{
        return FirebaseFirestore.getInstance().collection(RENTER).document(email).set(data, SetOptions.merge())
    }

    fun getLister(email : String) : Task<DocumentSnapshot> {
        return FirebaseFirestore.getInstance().collection(LISTER).document(email).get()
    }

    fun getRenter(email : String) : Task<DocumentSnapshot> {
        return FirebaseFirestore.getInstance().collection(RENTER).document(email).get()
    }

    fun getUser(email : String) : Task<DocumentSnapshot> {
        return FirebaseFirestore.getInstance().collection(USER).document(email).get()
    }

    fun addUserInDB(email: String, map : Any) : Task<Void> {
        return FirebaseFirestore.getInstance().collection(USER).document(email).set(map)
    }
}