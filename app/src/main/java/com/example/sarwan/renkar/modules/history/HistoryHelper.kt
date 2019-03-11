package com.example.sarwan.renkar.modules.history

import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.firebase.FirebaseExtras
import com.example.sarwan.renkar.model.History
import com.google.firebase.firestore.*

object HistoryHelper {
    fun create(data : History){
        val ref = FirebaseFirestore.getInstance().collection(FirebaseExtras.HISTORY).document(data.name.toString())
        FirebaseFirestore.getInstance().runTransaction {
            if (it.get(ref).exists()){
                ref.update(mapOf(FirebaseExtras.LISTED_BY to data.listedBy))
                ref.update(mapOf(FirebaseExtras.RENTED_BY to data.rentedBy))
                ref.update(mapOf(FirebaseExtras.CAR_NUMBER to data.car_number.toString()))
                ref.update(mapOf(FirebaseExtras.PERIOD to data.period.toString()))
                ref.update(mapOf(FirebaseExtras.NAME to data.name.toString()))
                ref.update(mapOf(FirebaseExtras.ID to data.id))
                ref.update(mapOf(FirebaseExtras.DETAILS to data.details.toString()))
                ref.update(mapOf(FirebaseExtras.STATUS to data.status.toString()))
            }else{
                ref.set(mapOf(FirebaseExtras.LISTED_BY to data.listedBy))
                ref.set(mapOf(FirebaseExtras.RENTED_BY to data.rentedBy))
                ref.set(mapOf(FirebaseExtras.CAR_NUMBER to data.car_number.toString()))
                ref.set(mapOf(FirebaseExtras.PERIOD to data.period.toString()))
                ref.set(mapOf(FirebaseExtras.NAME to data.name.toString()))
                ref.set(mapOf(FirebaseExtras.ID to data.id))
                ref.set(mapOf(FirebaseExtras.DETAILS to data.details.toString()))
                ref.set(mapOf(FirebaseExtras.STATUS to data.status.toString()))
            }
        }
    }

    fun get(email : String, type : String) : Query {
        return FirebaseFirestore.getInstance().collection(FirebaseExtras.HISTORY).
            whereEqualTo(if (type==ApplicationConstants.LISTER)
                FirebaseExtras.LISTED_BY else FirebaseExtras.RENTED_BY, email )
    }
}