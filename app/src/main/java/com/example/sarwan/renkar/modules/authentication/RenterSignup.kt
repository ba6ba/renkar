package com.example.sarwan.renkar.modules.authentication

import android.os.Bundle
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.utils.ValidationUtility
import kotlinx.android.synthetic.main.signup_renter_layout.*

class RenterSignup : SignupBaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_renter_layout)
        setUserType(ApplicationConstants.RENTER)
        onClickListeners()
    }

    private fun onClickListeners() {
        signup_R.setOnClickListener {
            if (validateData())
                signUpProcessing(email_R.text.toString(), password_R.text.toString(), phone_no_R.text.toString(),
                    first_name_R.text.toString(), last_name_R.text.toString(), user_name_R.text.toString(), ApplicationConstants.RENTER)
        }
    }

    private fun validateData(): Boolean {
        return (ValidationUtility.isNotEmptyField(email_R, first_name_R, last_name_R, phone_no_R, user_name_R, password_R))
    }

}