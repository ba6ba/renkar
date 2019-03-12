package com.example.sarwan.renkar.model.chat

import com.google.firebase.firestore.Exclude
import java.io.Serializable

class ChatRooms : Serializable{

    @get:Exclude
    var key :String? = null

    var title : String? = null
    var last_message_time : Double? = null
    var last_message : String? = null
    var last_message_sender : String? = null
    var last_message_sender_email : String? = null
    var read : ArrayList<String> ? = null
    var online : ArrayList<Int> ? = null
    var car_number : String ? = null
    @get:Exclude
    var chat_members : ArrayList<ChatMembers> = ArrayList()
    @get:Exclude
    var timePeriod : String ? = null

    var carAvailabilityDays : ArrayList<Int> ? = null

}