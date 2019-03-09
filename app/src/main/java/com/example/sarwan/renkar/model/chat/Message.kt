package com.example.sarwan.renkar.model.chat

import com.google.firebase.firestore.Exclude


class Message(){

    @get:Exclude
    var messageStatus :String? = ""
    @get:Exclude
    var readBy :ArrayList<String>? = null

    var message: String? = null
    var sender_email: String? = null
    var sender_name: String? = null
    var timestamp: Long = System.currentTimeMillis() / 1000
    var confirmation_option : Int ? = null
    var confirmation_type : Int ? = null

    constructor(message: String, sender_email : String,sender_name : String,  confirmation_option : Int,confirmation_type : Int ) : this(){
        this.message = message
        this.sender_email = sender_email
        this.sender_name = sender_name
        this.confirmation_option = confirmation_option
        this.confirmation_type = confirmation_type
        timestamp = System.currentTimeMillis() / 1000
    }
}