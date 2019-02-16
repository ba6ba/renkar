package com.example.sarwan.renkar.modules.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.hardware.Camera
import android.util.Log
import android.widget.Toast
import com.example.sarwan.renkar.base.ParentActivity
import kotlinx.android.synthetic.main.camera_layout.*

open class CameraBaseActivity : ParentActivity() {

    protected var mCamera : Camera ? = null
    protected lateinit var mPreview : CameraPreview
    protected var currentCameraType = Camera.CameraInfo.CAMERA_FACING_BACK
    protected var  matrix : Matrix = Matrix()


    /** A safe way to get an instance of the Camera object.  */
    @Suppress("DEPRECATION")
    protected fun getCameraInstance(cameraType: Int): Camera {
        var c: Camera? = null
        try {
            c = if (Camera.getNumberOfCameras() <= 1) {
                currentCameraType = Camera.CameraInfo.CAMERA_FACING_BACK
                Camera.open() // attempt to get a Camera instance
            }
            else
                Camera.open(cameraType)
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
        }

        return c?.let { it }?:kotlin.run { Camera.open() }
    }

    protected fun releaseCamera() {
        if (mCamera != null) {
            mCamera?.stopPreview()
            mCamera?.setPreviewCallback(null)
            mPreview.holder.removeCallback(mPreview)
            mCamera?.release()        // release the camera for other applications
            mCamera = null
        }
    }

    protected fun getCameraPermissionIfRequired() {
        openPreview()
    }

    private fun openPreview() {
        // Create an instance of Camera
        try {
            mCamera = getCameraInstance(currentCameraType)
            mCamera?.let {
                // Create our Preview view and set it as the content of our activity.
                mPreview = CameraPreview(this@CameraBaseActivity, it, currentCameraType)
                cameraPreview.removeAllViews()
                cameraPreview.addView(mPreview)
            }
        } catch (e: Exception) {
            Log.d("TAG", "Error starting camera preview: " + e.message)
        }
    }
}