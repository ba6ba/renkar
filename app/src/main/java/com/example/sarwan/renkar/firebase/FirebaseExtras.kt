package com.example.sarwan.renkar.firebase

class FirebaseExtras {
    companion object {
        const val UPLOAD_FAILURE : Int = 901
        const val CAR_EXISTS : Int = 990
        const val UPLOAD_SUCCESS :Int = 910

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
    }
}
