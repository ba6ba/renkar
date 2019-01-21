package com.example.sarwan.renkar.modules.authentication

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants

class SignupActivity : ParentActivity() {

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()
        auth = FirebaseAuth.getInstance()
    }

    private fun getIntentData() {
        intent?.hasExtra(ApplicationConstants.LISTER)?.let {
            if (it)  user?.isLister = intent?.getBooleanExtra(ApplicationConstants.LISTER, false)
        }
        setContentView(ApplicationConstants.SIGNUP_USER_TYPE_LAYOUT_MAP[user?.isLister]!!)
    }

    private fun signUpProcessing() {
        auth?.createUserWithEmailAndPassword("", "")
                ?.addOnCompleteListener { task: Task<AuthResult> ->
                    if (auth != null) {
                        task.result?.user?.uid?.let {
                            if(it.isNotEmpty())
                                createUserNodeOnFireBase(it)
                        }
                    }
                }
    }

    private fun createUserNodeOnFireBase(it: String) {
        //TODO create node on firebase
        takeAppropriateAction()
    }

    private fun takeAppropriateAction() {
        saveUserInSharedPreferences()
        openActivityWithFinish(Intent(this, ApplicationConstants.LOGIN_USER_TYPE_ACTIVITY_MAP[user?.isLister]))
    }
}

