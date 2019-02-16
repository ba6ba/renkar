package com.example.sarwan.renkar.utils

import android.widget.TextView
import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.model.ListerProfile
import com.example.sarwan.renkar.model.RenterProfile
import com.example.sarwan.renkar.model.User

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
        /*fun createChatRoom(members: ArrayList<ChatMembers>?, title: String?, message: String? = null,
                           senderName: String? = null, senderId: Int? = null, messageTime : Long ? = null):
                HashMap<String, Any?> {
            val map : HashMap<String, Any?> = hashMapOf()
            members?.let {member->
                for (i in members){
                    map[i.id.toString()] = ModelMappingUtility.mapOnChatRoomObject(i, false)
                }
            }
            map[VFirestoreQueryManager.TITLE] = title
            map[VFirestoreQueryManager.LAST_MESSAGE] = message
            map[VFirestoreQueryManager.LAST_MESSAGE_SENDER] = senderName
            map[VFirestoreQueryManager.LAST_MESSAGE_SENDER_ID] = senderId
            map[VFirestoreQueryManager.LAST_MESSAGE_TIME] = messageTime ?:kotlin.run { Calendar.getInstance().timeInMillis / 1000 }
            return map
        }
*/
    }
}