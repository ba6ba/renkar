package com.example.sarwan.renkar.extras

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.sarwan.renkar.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class WriteIntoStorage(val activity : Activity, val imageUri : Uri) : AppCompatActivity() {

    private var mImageBitmap : Bitmap ? = null
    var imageUpload : ImageUpload.ImageUploadResponse ? = null

    private fun setImageToView()  {

        // Get the dimensions of the View
        val targetW = LinearLayout.LayoutParams.MATCH_PARENT
        val targetH = activity.resources.getDimension(R.dimen.cover_image_height)

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageUri.path, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.min(photoW / targetW.toInt(), photoH / targetH.toInt())

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        imageUri.path?.let {
            if (it.isNotEmpty()){
                mImageBitmap = BitmapFactory.decodeFile(it, bmOptions)
                mImageBitmap = ImageUtility.rotateBitmap(it, mImageBitmap)
                imageUpload?.doUpload(mImageBitmap)
            }
        }
    }

    fun saveToInternalStorageWithCompression(): Uri? {
        try {
            mImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
//            mImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(File(imageUri)))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val cw = ContextWrapper(activity.applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val myPath = File(directory, System.currentTimeMillis().toString() + "profile.jpg")

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(myPath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            // resize like ImageView
            setImageToView()
            // Now save in file
            mImageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            MediaStore.Images.Media.insertImage(activity.contentResolver, myPath.absolutePath, myPath.name, myPath.name)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                assert(fos != null)
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return Uri.fromFile(myPath)
    }
}