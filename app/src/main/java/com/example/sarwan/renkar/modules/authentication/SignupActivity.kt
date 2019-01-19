package modules.authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.sarwan.renkar.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mobitribe.qulabro.modules.base.ParentActivity
import java.util.*


class SignupActivity : ParentActivity() {

    private var auth: FirebaseAuth? = null
    //for Signup
    private var databaseReference: DatabaseReference? = null
    private var firebaseDatabase: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.reference?.child("Users")
        signUp()
    }

    private var userId : String ? = null
    private fun generateToken() {
//        userId = UUID.randomUUID().node().toString().trim { a-> }
    }

    private fun signUpProcessing() {
        auth?.fetchSignInMethodsForEmail("")?.listener
        auth?.createUserWithEmailAndPassword(emailSignUp.text.toString(), passwordSignUp.text.toString())
                ?.addOnCompleteListener { task: Task<AuthResult> ->

                    if (auth != null) {

                        val userAuthId = auth?.getCurrentUser()?.uid
                        if(userAuthId!=null){
                            val current_user_db = databaseReference?.child(userAuthId as String)
                            current_user_db?.child("id")?.setValue(userId!!)
                            current_user_db?.child("name")?.setValue(firstNameSignUp.text.toString() + secondNameSignUp.text.toString())
                            current_user_db?.child("email")?.setValue(emailSignUp.text.toString())
                            current_user_db?.child("password")?.setValue(passwordSignUp.text.toString())
                            current_user_db?.child("travellingCategory")?.setValue(spinner.text.toString())
                            hideProgress()
                            saveData()
                            openActivityWithFinish(Intent(this@SignupActivity, MainActivity::class.java))
                        }
                        else {
                            alertDialog?.builder(this,"You can't register with same Email address. Try new one.")
                        }

                    } else {
                        hideProgress()
                        alertDialog?.builder(this,task.exception?.message)
                    }
                }
    }

    private fun saveData() {
        profile?.emailAddress = emailSignUp.text.toString()
        profile?.firstName = firstNameSignUp.text.toString()
        profile?.lastName = secondNameSignUp.text.toString()
        profile?.id = userId
        profile?.travellingCategory = spinner.text.toString()
        profile?.isLoggedIn = true
        saveProfileInSharedPreference()
    }

}

