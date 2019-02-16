package com.example.sarwan.renkar.modules.authentication

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.model.ListerProfile
import com.example.sarwan.renkar.model.RenterProfile
import com.example.sarwan.renkar.utils.ModelMappingUtility

open class SignupBaseActivity : ParentActivity() {

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    protected fun setUserType(type : String){
        user?.type = type
    }

    protected fun signUpProcessing(email : String, password : String, phoneNo : String, firstName: String, lastName : String, username : String, type: String) {
        showProgress()
        auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener { task: Task<AuthResult> ->
                    if (auth != null) {
                        task.result?.user?.uid?.let {
                            createUserNodeOnFireBase(email,phoneNo, firstName, lastName, username, type)
                        }
                    }
                }
    }

    private fun createUserNodeOnFireBase(
        email: String,
        phoneNo: String,
        firstName: String,
        lastName: String,
        username: String,
        type: String
    ) {

        FirestoreQueryCenter.addUserInDB(email,
            ModelMappingUtility.mapFields(
                email,
                phoneNo,
                firstName,
                lastName,
                username,
                type))
            .addOnSuccessListener {
                if (user?.type == ApplicationConstants.LISTER)
                    createListerNode(email)
                else
                    createRenterNode(email)
        }
    }

    private fun createRenterNode(id: String) {
        FirestoreQueryCenter.addDataToRenterNode(id, RenterProfile()).
                addOnSuccessListener {
                    takeAppropriateAction(id)
                }
    }

    private fun createListerNode(id: String) {
        FirestoreQueryCenter.addDataToListerNode(id, ListerProfile()).
            addOnSuccessListener {
                takeAppropriateAction(id)
            }
    }

    private fun takeAppropriateAction(email: String) {
        hideProgress()
        user?.email = email
        saveUserInSharedPreferences()
        openActivityWithFinish(Intent(this, LoginActivity::class.java))
    }
}

