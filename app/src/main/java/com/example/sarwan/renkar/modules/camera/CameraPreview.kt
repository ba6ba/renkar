package com.example.sarwan.renkar.modules.camera

import android.app.Activity
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.RelativeLayout
import android.widget.Toast

/** A basic Camera preview class  */
class CameraPreview(private val activity: Activity, private val mCamera: Camera, private val currentCameraType: Int) : SurfaceView(activity), SurfaceHolder.Callback {

    private var mHolder: SurfaceHolder = holder

    init {

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder.addCallback(this)

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            val params = mCamera.parameters

            // Check what resolutions are supported by your camera
            val sizes = params?.supportedPictureSizes

            // setting small image size in order to avoid OOM error
            var cameraSize: Camera.Size? = null
            sizes?.let {
                for (size in sizes) {
                    cameraSize = size
                    break
                }
            }

            cameraSize?.let {
                params?.setPictureSize(it.width, it.height)
                mCamera.parameters = params

                val ratio = this.width * 1f / it.width
                val w = it.width * ratio
                val h = it.height * ratio
                val lp = RelativeLayout.LayoutParams(w.toInt(), h.toInt())
                this.layoutParams = lp
            }

            mCamera.setPreviewDisplay(holder)
            mCamera.startPreview()
        } catch (e: RuntimeException) {
            Toast.makeText(activity, "Device camera  is not working properly, please try after sometime.", Toast.LENGTH_LONG).show()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // empty. Take care of releasing the Camera preview in your activity.
        //mCamera.stopPreview()
        //mCamera.release()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.surface == null) {
            // preview surface does not exist
            return
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }


        // start preview with new settings

        try {

            CameraUtility().setCameraDisplayOrientation(mCamera, currentCameraType, activity)
//            mCamera.setDisplayOrientation(0)
            // set preview size and make any resize, rotate or
            // reformatting changes here

            val cameraSize = mCamera.parameters?.pictureSize

            cameraSize?.width?.let {
                val ratio = this.width * 1f / it
                val w = cameraSize.width * ratio
                val h = cameraSize.height * ratio
                val lp = RelativeLayout.LayoutParams(w.toInt(), h.toInt())
                this.layoutParams = lp
            }
            mCamera.setPreviewDisplay(holder)
            mCamera.startPreview()
        }
        catch (e: Exception) {
            Log.d("TAG", "Error starting camera preview: " + e.message)
        }
    }

    companion object {
        private val TAG = "CameraPreview"
    }
}

