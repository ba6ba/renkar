package com.example.sarwan.renkar.firebase

import com.example.sarwan.renkar.model.Cars
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.internal.it
import com.google.firebase.firestore.*
import kotlin.collections.HashMap


object FirestoreQueryCenter {

    private var registration : ListenerRegistration ? = null
    private var callBack : FirebaseExtras.Companion.PutObjectCallBack ? = null
    
    fun getMembersReadStatus(conversationId: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(conversationId)
    }


    fun checkIfBlockUser(conversationId: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(conversationId)
    }

    fun getMessageQuery(conversationId: String) : CollectionReference {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(conversationId).collection(FirebaseExtras.CARS)
    }

    fun addChatRoom(conversationId: String, map : HashMap<String, Any?>){
        FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(conversationId).set(map)
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

    private fun unReadConversation(conversationId: String, usersId: ArrayList<Int>, senderId: Int){
        val ref = FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(conversationId)
        FirebaseFirestore.getInstance().runTransaction {
        for (i in usersId) { it.update(ref, "$i.$FirebaseExtras.CARS",false) }
            it.update(ref, "$senderId.$FirebaseExtras.CARS",true)
            null
        }
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


    fun getConversationById(userId: Int): com.google.firebase.firestore.Query {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).orderBy(userId.toString())
    }

    fun getGroupConversation(chatRoom: String): DocumentReference {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.CARS).document(chatRoom)
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
}