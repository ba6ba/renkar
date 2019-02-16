package com.example.sarwan.renkar.modules.camera

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.os.Bundle
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.ImageUtility
import kotlinx.android.synthetic.main.camera_layout.*

class CameraActivity : CameraBaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_layout)
        setOrientation()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        if (mCamera == null)
        {
            getCameraPermissionIfRequired()
        }
    }

    override fun onPause() {
        super.onPause()
        releaseCamera()
    }

    private fun setListeners() {
        imageCapture.setOnClickListener {
            takePicture()
        }
    }

    private fun takePicture() {
        mCamera?.takePicture(null, null, cameraPictureCallbackJpeg)
    }

    private fun takePictureFromCache(): Bitmap? {
        enableCaching()
        val bm = getDrawingCache()
        return bm
    }

    private fun setOrientation() {
        resources.configuration.orientation = Configuration.ORIENTATION_LANDSCAPE
    }

    private fun enableCaching(){
        cameraOverlay.isDrawingCacheEnabled = true
    }

    private fun disableCaching(){
        cameraOverlay.isDrawingCacheEnabled = false
    }

    private fun getDrawingCache(): Bitmap? {
        return cameraOverlay.drawingCache
    }

    private var cameraPictureCallbackJpeg: Camera.PictureCallback = Camera.PictureCallback { data, camera ->
        setImageSizes(data,camera, BitmapFactory.Options())
    }

    private fun setImageSizes(data: ByteArray, camera: Camera, options: BitmapFactory.Options) {
        val nm = Matrix()
        /*val cameraEyeValue = CameraUtility.getPhotoOrientation(this, currentCameraType) // CameraID = 1 : front 0:back
        if (currentCameraType == Camera.CameraInfo.CAMERA_FACING_FRONT) { // As Front camera is Mirrored so Fliping the Orientation
            if (cameraEyeValue == 270) {
                nm.postRotate(90f)
            } else if (cameraEyeValue == 90) {
                nm.postRotate(270f)
            }
        } else {
            //nm.postRotate(cameraEyeValue.toFloat()) // CameraEyeValue is default to Display Rotation
        }
*/
        nm.postScale(-1.0f,-1.0f)

        val uri = ImageUtility.storeImageInLocal(ImageUtility.createNewBitmap(options, data, nm, takePictureFromCache()), this)
        disableCaching()
        setResult(Activity.RESULT_OK, Intent().setData(uri))
        finish()
    }
}