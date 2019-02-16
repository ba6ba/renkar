package com.example.sarwan.renkar.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

object FirebaseStorageManager {

    val IMAGES = "images"
    val metadata = StorageMetadata.Builder().setContentType("image/jpeg").build()

    fun reference(filePath: Uri): StorageReference {
        return FirebaseStorage.getInstance().reference.child("$IMAGES/${filePath.lastPathSegment}")
    }

    fun image(filePath : Uri): UploadTask {
        return FirebaseStorage.getInstance().reference.child("$IMAGES/${filePath.lastPathSegment}").putFile(filePath, metadata)
    }

}
