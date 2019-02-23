package com.example.sarwan.renkar.extras

import android.net.Uri
import com.example.sarwan.renkar.firebase.FirebaseStorageManager
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

class ToFirebaseStorage {
    var listener : ToFirebaseStorageListener ? = null

    fun uploadFile(filePath: Uri, flag: Int){
        FirebaseStorageManager.image(filePath).continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation FirebaseStorageManager.reference(filePath).downloadUrl
        }).addOnSuccessListener {
            listener?.uploadDone(it, flag)
        }
    }

    interface ToFirebaseStorageListener{
        fun uploadDone(filePath: Uri, flag: Int)
    }
}