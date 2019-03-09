package com.example.sarwan.renkar.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import kotlinx.android.synthetic.main.profile_dialog_fragment.*

class ProfileDialogFragment : DialogFragment() {

    private lateinit var pActivity : ParentActivity
    private var listener : ProfileDialogFragmentListener ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(true)

        updateScreen()
    }

    private fun updateScreen() {
        pActivity.user?.imageUrl?.apply {
            if (this.isNotEmpty())
                my_image.setImageURI(this)
        }
        pActivity.user?.apply {
            this@ProfileDialogFragment.name.text = name
            this@ProfileDialogFragment.email.text = email
        }

        messages.setOnClickListener {
            dismissFragment()
        }
    }

    private fun dismissFragment() {
        listener?.onClick()
        dismissAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()
        activity?.resources?.let {
            dialog?.window?.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, it.getDimension(R.dimen.dialog_height_large).toInt())
            dialog?.window?.setGravity(Gravity.CENTER)
        }
    }

    interface ProfileDialogFragmentListener{
        fun onClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState).run {
            window?.attributes?.windowAnimations = R.style.Animation_DialogMessage_Window
            window?.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            window?.attributes?.x = activity?.resources?.getDimensionPixelSize(R.dimen.small_margin)
            window?.attributes?.y = activity?.resources?.getDimensionPixelSize(R.dimen.small_margin)
            return this
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileDialogFragment().apply {
            arguments = Bundle().apply {

            }
        }

    }
}