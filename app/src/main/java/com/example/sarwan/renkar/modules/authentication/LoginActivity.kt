package com.example.sarwan.renkar.modules.authentication

import android.content.Intent
import android.os.Bundle
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.extras.CustomDialogs
import com.example.sarwan.renkar.firebase.FirebaseExtras
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.model.ListerProfile
import com.example.sarwan.renkar.model.RenterProfile
import com.example.sarwan.renkar.model.User
import com.example.sarwan.renkar.modules.lister.ListerActivity
import com.example.sarwan.renkar.modules.renter.RenterActivity
import com.example.sarwan.renkar.modules.welcome.WelcomeActivity
import com.example.sarwan.renkar.permissions.PermissionActivity
import com.example.sarwan.renkar.permissions.Permissions
import com.example.sarwan.renkar.utils.ValidationUtility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.login_layout.*

class LoginActivity : ParentActivity() {

    private var mAuth : FirebaseAuth?=null
    private var listener : FirebaseAuth.AuthStateListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        initializeFireBaseAuth()
        putDataInViews()
        onClickListeners()
    }

    private fun initializeFireBaseAuth() {
        mAuth = FirebaseAuth.getInstance()
    }

    private fun putDataInViews() {
        user?.email?.let {
            email.setText(it)
        }
    }

    private fun onClickListeners(){
        login.setOnClickListener {
            if (validateData())
                makeLoginRequestOnFireBase()
        }

        signupFromLogin.setOnClickListener {
            CustomDialogs().typeDialog(this)
        }
    }

    private fun validateData(): Boolean {
        return ValidationUtility.isNotEmptyField(email,password)
    }

    private fun makeLoginRequestOnFireBase() {
        showProgress()
        mAuth?.signInWithEmailAndPassword(email.text.toString(), password.text.toString())?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                //Login successfully
                fetchUserDataFromFireBase(email.text.toString())
            }else
                hideProgress()
        }
    }

    private fun fetchUserDataFromFireBase(uid: String?) {
        uid?.let {
            makeRequestToFetchUser(uid)
        }
    }

    private fun makeRequestToFetchUser(uid: String){
        FirestoreQueryCenter.getUser(uid).addOnSuccessListener { it ->
            it?.data?.let { data->
                user = it.toObject(User::class.java)
                makeAppropriateRequest(data[FirebaseExtras.TYPE] as? String)
            }
        }
    }

    private fun makeAppropriateRequest(type: String?){
        when(type){
            ApplicationConstants.LISTER -> { makeRequestOnListerNode(email.text.toString())}
            ApplicationConstants.RENTER -> { makeRequestOnRenterNode(email.text.toString())}
            ApplicationConstants.BOTH -> {}
        }
        user?.type = type
    }

    private fun makeRequestOnRenterNode(uid: String) {
        FirestoreQueryCenter.getRenter(uid).addOnSuccessListener { it ->
            it?.let { data->
                saveRenterInSharedPreferences(data)
            }
        }
        hideProgress()
    }

    private fun makeRequestOnListerNode(uid: String) {
        FirestoreQueryCenter.getLister(uid).addOnSuccessListener { it ->
            it?.let { data->
                saveListerInSharedPreferences(data)
            }
        }
        hideProgress()
    }

    private fun saveRenterInSharedPreferences(data: DocumentSnapshot) {
        user?.renter = data.toObject(RenterProfile::class.java)
        user?.isLogin = true
        saveUserInSharedPreferences()
        openActivityIfPermissible()
    }

    private fun saveListerInSharedPreferences(data: DocumentSnapshot) {
        user?.lister = data.toObject(ListerProfile::class.java)
        user?.isLogin = true
        saveUserInSharedPreferences()
        openActivityIfPermissible()
    }

    private fun openActivityIfPermissible(){
        Permissions.getNotGranted(this).run {
            if (this.isEmpty()){
                when(user?.type){
                    ApplicationConstants.RENTER -> openActivityWithFinish(Intent(this@LoginActivity, RenterActivity::class.java))
                    ApplicationConstants.LISTER -> openActivityWithFinish(Intent(this@LoginActivity, ListerActivity::class.java))
                }
            }
            else {
                openActivityWithFinish(
                    Intent(this@LoginActivity, PermissionActivity::class.java).
                        putExtra(ApplicationConstants.PERMISSIONS, this))
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
