package com.example.sarwan.renkar.firebase

import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.utils.HashUtility
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.HashMap


object FirestoreQueryCenter {

    private var registration : ListenerRegistration ? = null
    private var callBack : FirebaseExtras.Companion.PutObjectCallBack ? = null

    fun setOnline(email: String){
        val ref = FirebaseFirestore.getInstance().collection(FirebaseExtras.ONLINE_USERS).document(FirebaseExtras.USER)
        FirebaseFirestore.getInstance().runTransaction {
            if (!it.get(ref).exists()){
                it.set(ref, mapOf(FirebaseExtras.ONLINE to FieldValue.arrayUnion(email)))
            }else{
                it.update(ref, mapOf(FirebaseExtras.ONLINE to FieldValue.arrayUnion(email)))
            }
        }
    }

    fun setOffline(email: String){
        FirebaseFirestore.getInstance().collection(FirebaseExtras.ONLINE_USERS).document(FirebaseExtras.USER)
            .update(mapOf(FirebaseExtras.ONLINE to FieldValue.arrayRemove(email)))
    }

    fun getOnline(): DocumentReference {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.ONLINE_USERS).document(FirebaseExtras.USER)
    }


    fun getReadStatus(conversationId: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CONVERSATION).document(conversationId)
    }

    fun setRead(chatRoom : String, email : String){
        FirebaseFirestore.getInstance().collection(FirebaseExtras.CONVERSATION).document(chatRoom).apply {
            update(mapOf(FirebaseExtras.READ to FieldValue.arrayUnion(email)))
        }
    }


    fun checkIfBlockUser(conversationId: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CONVERSATION).document(conversationId)
    }

    fun getMessageQuery(conversationId: String) : CollectionReference {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CHAT).document(conversationId).collection(FirebaseExtras.MESSAGES)
    }

    fun addChatRoom(conversationId: String, map : HashMap<String, Any?>){
        FirebaseFirestore.getInstance().collection(FirebaseExtras.CONVERSATION).document(conversationId).set(map)
    }

    fun readConversation(conversationId: String, id : Int){
        val ref = FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(conversationId)
        FirebaseFirestore.getInstance().runTransaction {
            val snapshot = it.get(ref)
            val status = (snapshot.get(id.toString()) as HashMap<String, Boolean>).getValue(FirebaseExtras.CARS)
            if (!status) it.update(ref, "$id.$FirebaseExtras.CARS",true)
            null
        }
    }

    private fun unReadConversation(conversationId: String, senderEmail: String?){
        FirebaseFirestore.getInstance().collection(FirebaseExtras.CONVERSATION).document(conversationId).
                update(mapOf(FirebaseExtras.READ to arrayListOf(senderEmail)))
    }

    fun myStatusListener(conversationId: String) : DocumentReference{
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(conversationId)
    }

    fun leaveGroup(conversationId: String, id : Int) {
        val map = hashMapOf<String, Any>(id.toString() to FieldValue.delete())
        FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(conversationId).update(map)
    }

    fun deleteGroup(conversationId: String) {
        FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(conversationId).delete()
    }

    fun setLastMessageOfConversation(conversationId: String, lastMessage: String?, sender_name: String?, senderEmail: String?, title : String?) {
        val lastMessageData = HashMap<String, Any>()
        lastMessageData[FirebaseExtras.LAST_MESSAGE] = lastMessage ?: kotlin.run { "" }
        lastMessageData[FirebaseExtras.UPDATED_TIME] = System.currentTimeMillis() / 1000
        lastMessageData[FirebaseExtras.LAST_MESSAGE_SENDER] = sender_name ?: kotlin.run { "" }
        lastMessageData[FirebaseExtras.LAST_MESSAGE_SENDER_ID] = senderEmail ?: kotlin.run { -1 }
        lastMessageData[FirebaseExtras.TITLE] = title ?: kotlin.run { "" }
        FirebaseFirestore.getInstance().collection(FirebaseExtras.CONVERSATION).document(conversationId).update(lastMessageData)
        unReadConversation(conversationId, senderEmail ?: kotlin.run { "" })
    }



    fun getConversationById(email: String): com.google.firebase.firestore.Query {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CONVERSATION).whereEqualTo("""${HashUtility.md5New(email)}.email""", email)
    }

    fun getGroupConversation(chatRoom: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(chatRoom)
    }

    fun getPaymentMethods(type: String?, email: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(if (type==ApplicationConstants.LISTER) FirebaseExtras.LISTER else FirebaseExtras.RENTER).document(email)
    }

    fun getCars(): Query {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).limit(10)
    }

    fun getSpecifications(carNumber: String): CollectionReference {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(carNumber).collection(FirebaseExtras.SPECS)
    }

    fun getRegistration(carNumber: String): CollectionReference {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(carNumber).collection(FirebaseExtras.REGISTRATION)
    }

    fun getListerCars(email: String): Query {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).whereEqualTo("${FirebaseExtras.OWNER}.email", email)
    }

    fun getNearestCars(nearestFrom: String): Query {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).whereEqualTo(FirebaseExtras.NEAREST_FROM, nearestFrom)
    }

    fun getPopularCars(rating: Float): Query {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).whereGreaterThanOrEqualTo(FirebaseExtras.RATING, rating)
    }

    fun checkIfCarExists(carNumber: String, callBack: FirebaseExtras.Companion.PutObjectCallBack){
        this.callBack = callBack
        registration = FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(carNumber).addSnapshotListener(listener)
    }

    private val listener = EventListener<DocumentSnapshot> { documentSnapshot, exception ->
        documentSnapshot?.exists()?.let {
            if (it) {
                callBack?.onPutFailure(FirebaseExtras.CAR_EXISTS)
            }else {
                callBack?.onPutSuccess(FirebaseExtras.CAR_EXISTS)
            }
        }
        registration?.remove()
    }


    fun batchWrite(
        carNumber: String,
        data: Cars,
        regData : Any,
        specsData :Any,
        callBack: FirebaseExtras.Companion.PutObjectCallBack
    ) {

         FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(carNumber).apply {
             set(data)
                 .addOnSuccessListener {
                     collection(FirebaseExtras.REGISTRATION).document().set(regData)
                         .addOnSuccessListener {regComplete->

                             collection(FirebaseExtras.SPECS).document().set(specsData)
                                 .addOnSuccessListener {specsComplete->
                                     callBack.onPutSuccess(FirebaseExtras.UPLOAD_SUCCESS)
                                 }
                                 .addOnFailureListener{failure->
                                     callBack.onPutFailure(FirebaseExtras.UPLOAD_FAILURE)
                                 }
                         }
                         .addOnFailureListener{failure->
                             callBack.onPutFailure(FirebaseExtras.UPLOAD_FAILURE)
                         }
                 }
                 .addOnFailureListener{failure->
                     callBack.onPutFailure(FirebaseExtras.UPLOAD_FAILURE)
                 }
         }
     }

    fun batchUpdate(
        carNumber: String,
        data: Cars,
        regData : Any,
        specsData :Any,
        callBack: FirebaseExtras.Companion.PutObjectCallBack
    ) {

        FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(carNumber).apply {
            update(mapOf(FirebaseExtras.CARS to data))
                .addOnSuccessListener {
                    collection(FirebaseExtras.REGISTRATION).document().update(mapOf(FirebaseExtras.REGISTRATION to regData))
                        .addOnSuccessListener {regComplete->

                            collection(FirebaseExtras.SPECS).document().set(mapOf(FirebaseExtras.SPECS to specsData))
                                .addOnSuccessListener {specsComplete->
                                    callBack.onPutSuccess(FirebaseExtras.UPLOAD_SUCCESS)
                                }
                                .addOnFailureListener{failure->
                                    callBack.onPutFailure(FirebaseExtras.UPLOAD_FAILURE)
                                }
                        }
                        .addOnFailureListener{failure->
                            callBack.onPutFailure(FirebaseExtras.UPLOAD_FAILURE)
                        }
                }
                .addOnFailureListener{failure->
                    callBack.onPutFailure(FirebaseExtras.UPLOAD_FAILURE)
                }
        }
    }

    fun addPaymentMethod(type : String?, email: String, map: Map<String,Any>): Task<Void> {
        return FirebaseFirestore.getInstance().collection(if (type==ApplicationConstants.LISTER) FirebaseExtras.LISTER else FirebaseExtras.RENTER).document(email).
            update(mapOf(FirebaseExtras.PAYMENT_METHOD to FieldValue.arrayUnion(map)))
    }

    fun addCarToListerNode(email: String, data: Any){
        FirebaseFirestore.getInstance().collection(FirebaseExtras.LISTER).document(email).update(mapOf(FirebaseExtras.CARS to arrayListOf(data)))
    }

    fun addDataToListerNode(email: String, data: Any) : Task<Void>{
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.LISTER).document(email).set(data, SetOptions.merge())
    }

    fun addDataToRenterNode(email: String, data: Any) : Task<Void>{
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.RENTER).document(email).set(data, SetOptions.merge())
    }

    fun getLister(email : String) : Task<DocumentSnapshot> {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.LISTER).document(email).get()
    }

    fun getRenter(email : String) : Task<DocumentSnapshot> {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.RENTER).document(email).get()
    }

    fun getUser(email : String) : Task<DocumentSnapshot> {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.USER).document(email).get()
    }

    fun addUserInDB(email: String, map : Any) : Task<Void> {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.USER).document(email).set(map)
    }

    fun updateUserInDB(email: String, map : Any) : Task<Void> {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.USER).document(email).set(map)
    }
}