package com.example.sarwan.renkar.extras

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import java.net.MalformedURLException
import java.net.URL

class ImageUpload(val activity : ParentActivity) {

    var imageUri : Uri ? = null
    var isUrl : Boolean ?= null
    var imageUploadResponse : ImageUploadResponse? = null
    private var writeIntoStorage : WriteIntoStorage ? = null
    private var mImageBitmap : Bitmap ? = null

    fun openGalleryForImage(requestCode : Int) {
        val externalStorageIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        externalStorageIntent.type = "image/*"
        activity.startActivityForResult(Intent.createChooser(externalStorageIntent, "Select Avatar"), requestCode)
    }

    fun openCameraForImage(requestCode : Int) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity.startActivityForResult(cameraIntent, requestCode)
    }

    fun makeImageURLWithExternalStorage(uri: Uri) : Uri? {
        imageUri = Uri.parse(FileManagerUtility().getPath(activity, uri))
        imageUri?.let {
            try {
                val url = URL(imageUri?.path)
                isUrl = true
                imageUploadResponse?.doUpload(imageUri)
            } catch (e: MalformedURLException) {
                /*invalid URL*/
                isUrl = false
                writeIntoStorage = WriteIntoStorage(activity, it)
                writeIntoStorage?.imageUpload = this.imageUploadResponse
                imageUri = writeIntoStorage?.saveToInternalStorageWithCompression()
            }
        }?:kotlin.run {
            activity.showMessage(activity.resources.getString(R.string.some_thing_went_wrong))
        }
        return imageUri?.let { it }?:kotlin.run { null }
    }

    fun makeImageURLWithCamera(data: Intent) : Uri? {
        run {
            mImageBitmap = data.extras?.get("data") as Bitmap
            try {
                mImageBitmap?.let { bitmap->
                    imageUploadResponse?.doUpload(bitmap)
                    imageUri = Uri.parse(FileManagerUtility().getRealPathFromURI(activity, bitmap))
                }
            } catch (e: Exception) {
                Log.e("TAG", "onActivityResult in camera" + e.message)
            }
            isUrl = false
            return imageUri?.let { it }?:kotlin.run { null }
        }
    }

    fun uploadOnAmazon() : Boolean? {
        return isUrl?.let { !it }?:kotlin.run { null }
    }


    interface ImageUploadResponse{
        fun doUpload(path: Uri?)
        fun doUpload(bitmap: Bitmap?)
    }

}