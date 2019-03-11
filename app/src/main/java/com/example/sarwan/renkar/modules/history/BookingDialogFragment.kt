package com.example.sarwan.renkar.modules.history

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.model.History
import kotlinx.android.synthetic.main.booking_dialog_fragment.*

class BookingDialogFragment : DialogFragment() {

    var listener : BookingDialogFragmentCallBack? = null
    private var status : String ? = null
    private lateinit var pActivity :ParentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
        arguments?.let {
            status = it.getString(STATUS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.booking_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)

        updateScreen()
        onClickListener()
    }

    private fun onClickListener() {
        accept.setOnClickListener {
            buttonAction(BUTTON_ACTION.ACCEPT)
        }

        reject.setOnClickListener {
            buttonAction(BUTTON_ACTION.REJECT)
        }
    }

    private fun buttonAction(action: BookingDialogFragment.Companion.BUTTON_ACTION) {
        when(action){
            BUTTON_ACTION.REJECT->{
                dismissAllowingStateLoss()
            }
            BUTTON_ACTION.ACCEPT->{
                listener?.onBookingDialogClick(status)
                dismissAllowingStateLoss()
            }
        }
    }

    private fun updateScreen() {
        when(status){
            History.STATUS.REQUEST_APPROVED.name->{
                message.text = pActivity.getString(R.string.request_approved_message)
            }
            History.STATUS.REQUEST_DECLINED.name->{
                message.text = pActivity.getString(R.string.request_declined_message)
            }
            History.STATUS.REQUEST_PENDING.name->{
                message.text = pActivity.getString(R.string.request_pending_message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.resources?.let {
            dialog?.window?.setLayout(it.getDimension(R.dimen.dialog_width_large).toInt() , it.getDimension(R.dimen.dialog_height_large).toInt())
            dialog?.window?.setGravity(Gravity.CENTER)
        }
    }

    fun initListener(listener : BookingDialogFragmentCallBack){
        this.listener = listener
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

    interface BookingDialogFragmentCallBack{
        fun onBookingDialogClick(status: String?)
    }

    companion object {
        @JvmStatic
        fun newInstance(status : String) = BookingDialogFragment().apply {
            arguments = Bundle().apply {
                putString(STATUS, status)
            }
        }
        val STATUS = "STATUS"

        enum class BUTTON_ACTION {ACCEPT, REJECT}
    }
}