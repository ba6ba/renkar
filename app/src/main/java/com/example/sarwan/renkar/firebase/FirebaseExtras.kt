package com.example.sarwan.renkar.firebase

class FirebaseExtras {
    companion object {
        const val UPLOAD_FAILURE : Int = 901
        const val UPDATED_FAILURE : Int = 902
        const val CAR_EXISTS : Int = 990
        const val UPLOAD_SUCCESS :Int = 910
        const val UPDATED_SUCESS :Int = 911
        const val SET_SUCCESS :Int = 912

        enum class FLAG {UPDATE, SET, REMOVE}

        interface PutObjectCallBack{
            fun onPutSuccess(code : Int)
            fun onPutFailure(code : Int)
        }

        /*
        * firebase collections
        * */
        const val LISTER = "listers"
        const val RENTER = "renters"
        const val CARS = "cars"
        const val USER = "users"
        const val REGISTRATION = "registration"
        const val BASIC = "basic"
        const val OWNER = "owner"
        const val SPECS = "specifications"
        const val TYPE = "type"
        const val PAYMENT_METHOD = "paymentMethod"
        const val NEAREST_FROM = "nearestFrom"
        const val RATING = "rating"
        const val BOOKING = "booking"




        /*
        *
        * firebase fields
        */

        const val EMAIL = "email"
        const val NAME = "name"
        const val IMAGE = "image"
        const val MODEL = "model"
        const val DESCRIPTION = "description"
        const val MANUFACTURED_BY = "manufactured_by"
        const val MILES = "miles"
        const val COVER_IMAGE = "cover_image"
        const val ADDRESS = "address"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"

        const val HOLDER_NAME = "holderName"
        const val EXPIRY_DATE = "expiryDate"
        const val CCV = "ccv"
        const val ID = "id"
        const val NUMBER = "number"
        const val CREATED_AT = "createdAt"

        const val CHAT_USER_NAME = "name"
        const val CHAT_USER_IMAGE = "image"
        const val CHAT_USER_EMAIL = "email"

        val CONVERSATION = "chat-rooms"
        val BLOCK_USER = "block_user"
        val CHAT = "chat-room-messages"
        val LAST_MESSAGE= "last_message"
        val UPDATED_TIME= "last_message_time"
        val LAST_MESSAGE_SENDER = "last_message_sender"
        val LAST_MESSAGE_SENDER_ID = "last_message_sender_id"
        val STATUSES = "statuses"
        val ONLINE = "online"
        val ONLINE_USERS = "online_users"
        val ARRAY = "array"
        val READ  = "read"
        val LAST_MESSAGE_TIME = "last_message_time"
        val TITLE = "title"
        val MESSAGES = "messages"
        const val CAR_NUMBER = "car_number"
        const val CAR_AVAILABILITY_DAYS= "carAvailabilityDays"
        const val HISTORY = "history"
        const val RENTED_BY = "rentedBy"
        const val LISTED_BY = "listedBy"
        const val BOOKING_HISTORY = "bookingHistory"
        const val PERIOD = "period"
        const val DETAILS = "details"
        const val STATUS = "status"


    }
}
