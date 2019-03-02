package com.example.sarwan.renkar.utils

import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class ModelMappingUtility {
    companion object {

        fun mapFields(email : String , phone_no : String, first_name : String, last_name : String,
                              username : String, type: String) : Any {
            val user = User()
            user.email = email
            user.phone_no = phone_no
            user.first_name = first_name
            user.last_name = last_name
            user.user_name = username
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