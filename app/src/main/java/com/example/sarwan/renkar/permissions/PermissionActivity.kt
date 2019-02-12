package com.example.sarwan.renkar.permissions

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.modules.lister.ListerActivity
import com.example.sarwan.renkar.modules.location.Location
import com.example.sarwan.renkar.modules.renter.RenterActivity
import kotlinx.android.synthetic.main.activity_permission.*

class PermissionActivity : ParentActivity(), PermissionsAdapter.PermissionListener {

    private var adapter: PermissionsAdapter? = null
    private var permissions : ArrayList<String> = ArrayList()
    private var location : Location? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        location = Location(this)
        getIntentData()
        initializeViews()
        setListeners()
    }

    private fun getIntentData() {
        permissions = intent?.extras?.getSerializable(ApplicationConstants.PERMISSIONS) as ArrayList<String>
    }

    private fun setListeners() {
        start.setOnClickListener {
            if (location?.checkForLocationPermissions() == Permissions.location_permissions.size){
                location?.get()
                when(user?.type){
                    ApplicationConstants.RENTER -> openActivityWithFinish(Intent(this, RenterActivity::class.java))
                    ApplicationConstants.LISTER -> openActivityWithFinish(Intent(this, ListerActivity::class.java))
                }
            }
            else{
                Toast.makeText(this, getString(R.string.location_permission_warning), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initializeViews() {
        adapter = PermissionsAdapter(this, ArrayList(), this)
        recyclerView.layoutManager = GridLayoutManager(this,2,RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
        setPermissions()
    }

    private fun setPermissions(){
        adapter?.swap(permissions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode)
        {
            ApplicationConstants.PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // task you need to do.
                    removeFromPermissionList(permissions[0])
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showMessage(getString(R.string.permission_denied))
                }
                return
            }
        }
    }

    private fun removeFromPermissionList(s: String) {
        permissions.remove(s)
        enableStartButton()
    }

    private fun enableStartButton() {
        if (permissions.isEmpty())
            start.isEnabled = true
    }

    override fun askForPermission(s: String) {
        ActivityCompat.requestPermissions(this, arrayOf(s), ApplicationConstants.PERMISSION_CODE)
    }
}
