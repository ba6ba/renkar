package com.example.sarwan.renkar.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import java.io.IOException
import android.widget.Toast
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.ProgressLoader
import com.example.sarwan.renkar.extras.SharedPreferences
import com.example.sarwan.renkar.model.User
import com.example.sarwan.renkar.modules.authentication.LoginActivity

abstract class ParentActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private var progressLoader: ProgressLoader? = null
    private var animationNeeded: Boolean = false
    private var forwardTransition: Boolean = false
    private val view: View? = null
    private val TAG = "ParentActivity"

    var user: User? = null

    /**
     * @usage onCreate method that will be called by all child class instances to initialize some useful objects
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = SharedPreferences(this)
        animationNeeded = true
        forwardTransition = true
        user = sharedPreferences.userProfile
        if(user == null)
            user = User()
    }

    fun saveUserInSharedPreferences(){
        sharedPreferences.saveUserProfile(user)
    }

    /**
     * @purpose  Setter for animation needed parameter
     * @param value
     */
    fun setAnimationNeeded(value: Boolean) {
        animationNeeded = value
    }

    /**
     * @usage it opens the activity receives in parameter
     * @param activity
     */
    fun openActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
    }

    /**
     * @usage it opens the activity receives in parameter and finish  the current activity running
     * @param activity
     */
    fun openActivityWithFinish(activity: Class<*>) {
        startActivity(Intent(this, activity))
        finish()
    }



    /**
     * @usage it opens the activity receives in parameter and finish  the current activity running
     * @param activity
     */
    fun openMainActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finish()
    }
    /**
     * @usage it opens the activity with provide intent and finish  the current activity running
     * @param intent
     */
    fun openActivityWithFinish(intent: Intent) {
        startActivity(intent)
        finish()
    }

    /**
     * @usage it opens the activity with provide intent
     * @param intent
     */
    fun openActivity(intent: Intent) {
        startActivity(intent)
    }

    /**
     * @usage It opens the activity with the provided bundle as a parameter
     * @param activity
     * @param bundle
     */
    fun openActivity(activity: Class<*>, bundle: Bundle) {
        startActivity(Intent(this, activity).putExtras(bundle))
    }

    /**
     * @usage It opens the activity for result with the provided bundle as a parameter
     * @param activity
     * @param bundle
     */
    fun openActivityForResults(activity: Class<*>, bundle: Bundle, requestCode: Int) {
        startActivityForResult(Intent(this, activity).putExtras(bundle), requestCode)
    }


    /**
     * @usage It opens the activity for result
     * @param activity
     */
    fun openActivityForResults(activity: Class<*>, requestCode: Int) {
        startActivityForResult(Intent(this, activity), requestCode)
    }


    /**
     * @usage Making notification bar transparent
     */
    fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * @usage It use to show any message provided by the caller
     * @param message
     */
    fun showMessage(message: String?) {
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * @usage it handles onFailure Response of retrofit
     * @param throwable
     * @param view
     */
    fun onFailureResponse(view: View, throwable: Throwable) {
        if (throwable is IOException) {
            showMessage(getString(R.string.internet_connectivity))
        } else {
            showMessage(getString(R.string.some_thing_went_wrong))
        }
    }

    /**
     * @usage it handles onFailure Response of retrofit
     * @param throwable
     * @param view
     */
    fun onFailureResponse(throwable: Throwable?) {
        if (throwable is IOException) {
            showMessage(getString(R.string.internet_connectivity))
        } else {
            showMessage(getString(R.string.some_thing_went_wrong))
        }
    }

    fun showProgress() {
        try {
            if (progressLoader == null) {
                progressLoader = ProgressLoader()
            }

            progressLoader!!.show(supportFragmentManager, TAG)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "showProgress:" + e.message)
        }

    }

    fun hideProgress() {
        if (progressLoader != null) {
            try {
                progressLoader!!.dismissAllowingStateLoss()
            } catch (e: Exception) {
                Log.e(TAG, "hideProgress:" + e.message)
            }

        }
    }

    fun onBackPressed(v: View) {
        super.onBackPressed()
    }

    override fun onPause() {
        if (animationNeeded) {
            if (forwardTransition)
                overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity)
            else
                overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
        }
        super.onPause()

    }



    override fun onBackPressed() {
        forwardTransition = false
        super.onBackPressed()
    }

    companion object {

        var currentCallStatus : CallStatus? = CallStatus.NoCall

        @SuppressLint("StaticFieldLeak")
        var callActivityContext : Activity ? = Activity()

        @SuppressLint("StaticFieldLeak")
        var previousActivity: Activity? = null
        @SuppressLint("StaticFieldLeak")
        var firstPreviousActivity: Activity? = null
        fun closePreviousActivity() {
            try {
                previousActivity?.finish()
                firstPreviousActivity?.finish()
            } catch (e: Exception) {
                Log.e("ClosePreviousActivity", e.message)
            }
        }

        var currentChatUserId : Int = -1
    }

    enum class CallStatus { OnCall, NoCall }

   /* private fun appropriateEmail() : String{
        return if (user?.isLister!!){
            user?.lister?.email!!
        }
        else{
            user?.renter?.email!!
        }
    }

    private fun appropriateEmail(email : String){
        if (user?.isLister!!){
            user?.lister?.email = email
        }
        else{
            user?.renter?.email = email
        }
    }

    fun logOut(){
        val temp_email : String
        val temp_isLister : Boolean
        val temp_isFirst : Boolean
        temp_email = appropriateEmail()
        temp_isLister = user?.isLister!!
        temp_isFirst = user?.isFirst!!
        user = User()
        user?.isLister = temp_isLister
        user?.isFirst = temp_isFirst
        appropriateEmail(temp_email)
        saveUserInSharedPreferences()
        openMainActivity(LoginActivity::class.java)
    }
*/
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
