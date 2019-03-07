package com.example.sarwan.renkar.utils

import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.model.PaymentMethods
import com.example.sarwan.renkar.model.User
import com.google.firebase.firestore.QuerySnapshot

class ModelMappingUtility {
    companion object {

        fun mapFields(email : String , phone_no : String, first_name : String, last_name : String,
                              username : String, type: String) : Any {
            val user = User()
            user.email = email
            user.phoneNo = phone_no
            user.firstName = first_name
            user.lastName = last_name
            user.userName = username
            user.type = type
            return user
        }

        fun allCars(documents: QuerySnapshot): ArrayList<Cars>{
            val list : ArrayList<Cars> = ArrayList()
            list.addAll(documents.toObjects(Cars::class.java))
            return list
        }

    }
}