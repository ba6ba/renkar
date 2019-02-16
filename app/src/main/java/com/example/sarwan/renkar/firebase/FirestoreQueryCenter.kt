package com.example.sarwan.renkar.firebase

import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.model.PaymentMethod
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlin.collections.HashMap


object FirestoreQueryCenter {

    val LISTER = "listers"
    val RENTER = "renters"
    val CARS = "cars"
    val USER = "users"
    val REGISTRATION = "registration"
    val BASIC = "basic"
    val OWNER = "owner"
    val PRICE = "price"
    val SPECS = "specifications"
    val ADDRESS = "address"
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

    fun checkIfCarExists(carNumber: String) : Boolean {
        return FirebaseFirestore.getInstance().runTransaction {
            it.get(FirebaseFirestore.getInstance().collection(CARS).document(carNumber)).exists()
            null
        }.isSuccessful
    }

    fun addCarData(carNumber: String, data: Any) : Task<Void>{
        return FirebaseFirestore.getInstance().collection(CARS).document(carNumber).set(data, SetOptions.merge())
    }

    fun addNestedCarData(carNumber: String, data: Any, subCollection : String) : Task<Void>{
        return FirebaseFirestore.getInstance().collection(CARS).document(carNumber).collection(subCollection).document().set(data)
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