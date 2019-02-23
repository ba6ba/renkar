package com.example.sarwan.renkar.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.sarwan.renkar.R
import kotlinx.android.synthetic.main.confirmation_dialog.*

class ConfirmationFragment : DialogFragment() {

    val CONFIRMATION_DIALOG_TYPE = "CONFIRMATION_DIALOG_TYPE"
    var type : Int ? = null
    var listener : ConfirmationFragmentCallBack<Any> ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(CONFIRMATION_DIALOG_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.confirmation_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)

        updateScreen()

        allow.setOnClickListener {
            takeAppropriateAllowAction()
        }

        deny.setOnClickListener {
            takeAppropriateDenyAction()
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.resources?.let {
            dialog?.window?.setLayout(it.getDimension(R.dimen.dialog_width).toInt() , it.getDimension(R.dimen.dialog_height).toInt())
            dialog?.window?.setGravity(Gravity.CENTER)
        }
    }

    fun initListener(listener : ConfirmationFragment.ConfirmationFragmentCallBack<Any>){
        this.listener = listener
    }

    private fun takeAppropriateDenyAction() {
        when(type){
            ConfirmationType.CAR_UPDATE.ordinal ->{
                carUpdateAction(ConfirmationOption.DENY)
            }
            ConfirmationType.DATE_TIME.ordinal ->{}
        }
    }

    private fun updateScreen() {
        when(type){
            ConfirmationType.CAR_UPDATE.ordinal ->{
                carUpdateScreen()
            }
            ConfirmationType.DATE_TIME.ordinal ->{}
        }
    }

    private fun carUpdateScreen() {
        warning.text = activity?.resources?.getString(R.string.car_already_listed)
        image.setImageResource(R.drawable.ic_wheel)
    }

    private fun takeAppropriateAllowAction() {
        when(type){
            ConfirmationType.CAR_UPDATE.ordinal ->{
                carUpdateAction(ConfirmationOption.ALLOW)
            }
            ConfirmationType.DATE_TIME.ordinal ->{}
        }
    }

    private fun carUpdateAction(option: ConfirmationOption) {
        listener?.onAction(ConfirmationType.CAR_UPDATE.ordinal, option.ordinal)
        dismissFragment(option.ordinal)
    }

    private fun dismissFragment(ordinal: Int) {
        when(ordinal){
            ConfirmationOption.ALLOW.ordinal->{
                dismissAllowingStateLoss()
            }
            ConfirmationOption.DENY.ordinal->{
                dismissAllowingStateLoss()
                activity?.finish()
            }
        }
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

    interface ConfirmationFragmentCallBack<T>{
        fun onAction(type : T, option : T)
    }

    companion object {
        @JvmStatic
        fun newInstance(type : Int) = ConfirmationFragment().apply {
            arguments = Bundle().apply {
                putInt(CONFIRMATION_DIALOG_TYPE,type)
            }
        }

        enum class ConfirmationType {CAR_UPDATE, DATE_TIME}
        enum class ConfirmationOption {ALLOW, DENY}
    }
}