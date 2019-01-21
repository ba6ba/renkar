package com.example.sarwan.renkar.extras


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.util.Log
import android.view.Window
import com.example.sarwan.renkar.R


class ProgressLoader : androidx.fragment.app.DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(activity!!)

        // request a window without the designation
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        val inflater = activity!!.layoutInflater
        val parent = inflater.inflate(R.layout.fragment_progress_loader, null)
        dialog.setContentView(parent)
        dialog.setCanceledOnTouchOutside(false)
        //Set the dialog to immersive
        dialog.window!!.decorView.systemUiVisibility = activity!!.window.decorView.systemUiVisibility

        return dialog

    }

    override fun show(manager: androidx.fragment.app.FragmentManager, tag: String) {

        try {
            if (this.isAdded) {
                return  //or return false/true, based on where you are calling from
            }
            val ft = manager.beginTransaction()
            try {
                ft.add(this, TAG)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            /*try catch is used to tackle the crash
         when user close the app and some thing
         running in indicator of app*/

            ft.commitAllowingStateLoss()


        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.message)
        }

    }

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (e: IllegalStateException) {
            super.dismissAllowingStateLoss()
        }

    }

    companion object {

        private val TAG = "ProgressLoader"
        private var loader: ProgressLoader? = null

        val progressLoader: ProgressLoader
            get() {
                if (loader == null) {
                    loader = ProgressLoader()

                }
                return loader!!

            }

        fun hideProgress() {

            if (loader != null) {

                loader!!.dismissAllowingStateLoss()
            }
        }
    }
}
