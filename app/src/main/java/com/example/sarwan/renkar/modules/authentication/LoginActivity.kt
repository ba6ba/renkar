package modules.authentication


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import extras.AlertDialog
import kotlinx.android.synthetic.main.third_party_service_layout.*
import modules.main.MainActivity
import utils.ValidationUtility


class LoginActivity : ParentActivity() {

    private var mAuth : FirebaseAuth?=null
    private var listener : FirebaseAuth.AuthStateListener?=null
    private var alertDialog : AlertDialog ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setEmailFromSharedPref()
        alertDialog = AlertDialog()
        mAuth = FirebaseAuth.getInstance()
        setOnClickListeners()
    }

    private fun setEmailFromSharedPref() {
        if(profile?.emailAddress!=null){
            emailLogin.setText(profile?.emailAddress.toString(), TextView.BufferType.EDITABLE)
            emailLogin.setSelection(profile?.emailAddress.toString().length)
        }
    }


    private fun setOnClickListeners() {


        signupButton.setOnClickListener {
            openActivityWithFinish(Intent(this@LoginActivity,SignupActivity::class.java))
        }

        login_button.setOnClickListener {
           checkFields()
        }
        facebookButton.setOnClickListener {
            alertDialog?.builder(this,"Can't Connect right now :(")
        }

        twitterButton.setOnClickListener {
            alertDialog?.builder(this,"Can't Connect right now :(")
        }

        googlePlusButton.setOnClickListener {
            alertDialog?.builder(this,"Can't Connect right now :(")
        }

    }

    private fun checkFields() {
        if ((!TextUtils.isEmpty(emailLogin.text))
                && (!TextUtils.isEmpty(passwordLogin.text))) {
            showProgress()
            if(emailLogin.text.toString()!=profile?.emailAddress.toString())
            {
                resetSharedPreferences()
            }
            requestForLogin(emailLogin.text.toString(), passwordLogin.text.toString())
        }
        else {
            ValidationUtility.setError(emailLoginLayout,getString(R.string.must_not_be_empty))
            ValidationUtility.setError(passwordLoginLayout,getString(R.string.must_not_be_empty))
            ValidationUtility.removeErrors(emailLoginLayout,passwordLoginLayout)
        }
    }


    private fun requestForLogin(email: String, password: String) {
        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this)
        { task ->
            hideProgress()
            if (task.isSuccessful) {
                //we in
                profile?.isLoggedIn = true
                saveProfileInSharedPreference()
                openActivityWithFinish(Intent(this@LoginActivity, MainActivity::class.java))

            } else {
                alertDialog?.builder(this,task.exception.toString())
            }
        }

    }



    override fun onStart() {
        super.onStart()
        mAuth?.addAuthStateListener { listener }
    }

    override fun onStop() {
        super.onStop()
        if(listener!=null){
            mAuth?.removeAuthStateListener { listener }
        }
    }


}


