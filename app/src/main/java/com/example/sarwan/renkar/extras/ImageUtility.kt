package com.example.sarwan.renkar.extras

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.InvocationTargetException


open class ImageUtility {
    companion object {
        fun rotateBitmap(src: String, bitmap: Bitmap?): Bitmap? {
            bitmap?.let {
                try {
                    val orientation = getExifOrientation(src)

                    if (orientation == 1) {
                        return bitmap
                    }

                    val matrix = Matrix()
                    when (orientation) {
                        2 -> matrix.setScale(-1f, 1f)
                        3 -> matrix.setRotate(180f)
                        4 -> {
                            matrix.setRotate(180f)
                            matrix.postScale(-1f, 1f)
                        }
                        5 -> {
                            matrix.setRotate(90f)
                            matrix.postScale(-1f, 1f)
                        }
                        6 -> matrix.setRotate(90f)
                        7 -> {
                            matrix.setRotate(-90f)
                            matrix.postScale(-1f, 1f)
                        }
                        8 -> matrix.setRotate(-90f)
                        else -> return bitmap
                    }

                    return try {
                        val oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                        bitmap.recycle()
                        oriented
                    } catch (e: OutOfMemoryError) {
                        e.printStackTrace()
                        bitmap
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return bitmap
            }?:kotlin.run {
                return null
            }
        }

        @Throws(IOException::class)
        private fun getExifOrientation(src: String): Int {
            var orientation = 1

            try {
                /**
                 * if your are targeting only api level >= 5
                 * ExifInterface exif = new ExifInterface(src);
                 * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                 */
                if (Build.VERSION.SDK_INT >= 5) {
                    val exifClass = Class.forName("android.media.ExifInterface")
                    val exifConstructor = exifClass.getConstructor(*arrayOf<Class<*>>(String::class.java))
                    val exifInstance = exifConstructor.newInstance(*arrayOf<Any>(src))
                    val getAttributeInt = exifClass.getMethod("getAttributeInt", *arrayOf<Class<*>>(String::class.java, Int::class.java))
                    val tagOrientationField = exifClass.getField("TAG_ORIENTATION")
                    val tagOrientation = tagOrientationField.get(null) as String
                    orientation = getAttributeInt.invoke(exifInstance, *arrayOf(tagOrientation, 1)) as Int
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }

            return orientation
        }

        fun storeImageInLocal(newBitmap: Bitmap, context: Context) : Uri? {
            val storagePath = File(Environment.getExternalStorageDirectory().absolutePath + "/Renkar/")
            storagePath.mkdirs()

            val myImage = File(storagePath, java.lang.Long.toString(System.currentTimeMillis()) + ".jpg")

            try {
                val out = FileOutputStream(myImage)
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                return FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", myImage)
            } catch (e: FileNotFoundException) {
                Log.d("In Saving File", e.localizedMessage + "")
            } catch (e: IOException) {
                Log.d("In Saving File", e.localizedMessage + "")
            }

            return null
        }

        fun createNewBitmap(options: BitmapFactory.Options, data: ByteArray, cameraMatrix: Matrix, bitmap1: Bitmap?) : Bitmap {

            calculateInSampleSize(options, bitmap1?.width?.let { it }?:kotlin.run { -1 },
                bitmap1?.height?.let { it }?:kotlin.run { -1 }).run {
                options.inSampleSize = if (this==1) 2 else this
            }

            var cameraBitmap = BitmapFactory.decodeByteArray(data, 0, data.size, options)
            val newBitmap = Bitmap.createBitmap(options.outWidth, options.outHeight, Bitmap.Config.RGB_565)
            val canvas = Canvas(newBitmap)
            cameraBitmap = Bitmap.createBitmap(cameraBitmap,0, 0, cameraBitmap.width, cameraBitmap.height, cameraMatrix, true)
            canvas.drawBitmap(cameraBitmap, 0f, 0f, null)
            recycleBitmap(cameraBitmap)

            bitmap1?.let {
                val frame = Bitmap.createScaledBitmap(it, cameraBitmap.width, cameraBitmap.height, true)
                canvas.drawBitmap(frame, 0f,0f, null)
                recycleBitmap(frame)
            }
            return newBitmap
        }

        private fun recycleBitmap(mBitmap: Bitmap?){
            mBitmap?.let {
                if (!mBitmap.isRecycled){
                    mBitmap.recycle()
                }
            }
        }

        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            // Raw height and width of image
            val (height: Int, width: Int) = options.run { outHeight to outWidth }
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {

                val halfHeight: Int = height / 2
                val halfWidth: Int = width / 2

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }
    }



}