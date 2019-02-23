package com.example.sarwan.renkar.modules.authentication

import android.os.Bundle
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.utils.ValidationUtility
import kotlinx.android.synthetic.main.signup_lister_layout.*

class ListerSignup : SignupBaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_lister_layout)
        setUserType(ApplicationConstants.LISTER)
        onClickListeners()
    }

    private fun onClickListeners() {
        signup_L.setOnClickListener {
            if (validateData())
                signUpProcessing(email_L.text.toString(), password_L.text.toString(),phone_no_L.text.toString(), first_name_L.text.toString(),
                    last_name_L.text.toString() , user_name_L.text.toString(), ApplicationConstants.LISTER)
        }
    }

    private fun validateData(): Boolean {
        return (ValidationUtility.isNotEmptyField(email_L, first_name_L, last_name_L, phone_no_L, user_name_L, password_L))
    }
    
}