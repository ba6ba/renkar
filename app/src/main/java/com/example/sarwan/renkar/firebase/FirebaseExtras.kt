package com.example.sarwan.renkar.firebase

class FirebaseExtras {
    companion object {
        const val UPLOAD_FAILURE : Int = 901
        const val CAR_EXISTS_FAILURE : Int = 902
        const val UPLOAD_SUCCESS :Int = 910
        const val CAR_EXISTS_SUCCESS :Int = 911

        interface PutObjectCallBack{
            fun onPutSuccess(code : Int)
            fun onPutFailure(code : Int)
        }
    }
}