package com.example.sarwan.renkar.modules.authentication

import android.content.Intent
import android.os.Bundle
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_layout.*

class LoginActivity : ParentActivity() {

    private var mAuth : FirebaseAuth?=null
    private var listener : FirebaseAuth.AuthStateListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        mAuth = FirebaseAuth.getInstance()
        getIntentData()
        appropriateLayout()
        onClickListeners()
    }

    private fun onClickListeners(){
        userLoginChoice.setOnClickListener {
            user?.isLister = !user?.isLister!!
            appropriateLayout()
        }

        login.setOnClickListener {
            makeLoginRequestOnFireBase()
        }

        signup.setOnClickListener {
            openActivity(Intent(this, LoginActivity::class.java).putExtra(ApplicationConstants.LISTER, user?.isLister))
        }
    }

    private fun makeLoginRequestOnFireBase() {
        mAuth?.signInWithEmailAndPassword("", "")?.addOnCompleteListener(this) { task ->
            hideProgress()
            if (task.isSuccessful) {
                //we in
                fetchUserDataFromFireBase(task.result?.user?.uid)
            }
        }
    }

    private fun fetchUserDataFromFireBase(uid: String?) {
        uid?.let {
            when (it.isNotEmpty()){
                true-> if (user?.isLister!!)
                    makeRequestOnListerNode(uid)
                else
                    makeRequestOnRenterNode(uid)
                false-> showMessage("Server error!")
            }
        }
    }

    private fun makeRequestOnRenterNode(uid: String) {
        //TODO make query to fetch data of above uid

        //when gets Data
        val data : Any = ""
        takeAppropriateAction(data)
    }

    private fun takeAppropriateAction(data : Any) {
        user?.isLogin = true
        saveUserInSharedPreferences()
        openActivity(Intent(this, ApplicationConstants.LOGIN_USER_TYPE_ACTIVITY_MAP[user?.isLister]))
    }

    private fun makeRequestOnListerNode(uid: String) {
        //TODO make query to fetch data of above uid

        val data : Any = ""
        takeAppropriateAction(data)
    }

    private fun appropriateLayout() {
        userLoginChoice.text = ApplicationConstants.LOGIN_USER_TYPE_TEXT_MAP[user?.isLister]

    }

    private fun getIntentData() {
        intent?.hasExtra(ApplicationConstants.LISTER)?.let {
            if (it)  user?.isLister = intent?.getBooleanExtra(ApplicationConstants.LISTER, false)
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
