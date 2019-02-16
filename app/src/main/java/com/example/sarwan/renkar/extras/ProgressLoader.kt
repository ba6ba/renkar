package com.example.sarwan.renkar.extras


import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.util.Log
import android.view.*
import com.example.sarwan.renkar.R


class ProgressLoader : androidx.fragment.app.DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.fragment_progress_loader, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.systemUiVisibility?.let { dialog?.window?.decorView?.systemUiVisibility = it }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        super.onCreateDialog(savedInstanceState).run {
            window?.attributes?.windowAnimations = R.style.ProgressLoader_Animation_Window
            window?.setGravity(Gravity.CENTER)
            window?.attributes?.x = activity?.resources?.getDimensionPixelSize(R.dimen.small_margin)
            window?.attributes?.y = activity?.resources?.getDimensionPixelSize(R.dimen.small_margin)
            return this
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
