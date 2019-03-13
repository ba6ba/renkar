package com.example.sarwan.renkar.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.BaseDialog
import kotlinx.android.synthetic.main.confirmation_dialog.*
import kotlinx.android.synthetic.main.confirmation_dialog_chat.*

class ConfirmationFragment : BaseDialog() {

    val CONFIRMATION_DIALOG_TYPE = "CONFIRMATION_DIALOG_TYPE"
    var type : Int ? = null
    var listener : ConfirmationFragmentCallBack<Any> ? = null
    private lateinit var pActivity :ParentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
        arguments?.let {
            type = it.getInt(CONFIRMATION_DIALOG_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return when(type){
            ConfirmationType.CAR_UPDATE.ordinal->{
                inflater.inflate(R.layout.confirmation_dialog, container, false)
            }
            ConfirmationType.BOOKING.ordinal->{
                inflater.inflate(R.layout.confirmation_dialog, container, false)
            }
            else -> inflater.inflate(R.layout.confirmation_dialog_chat, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)

        updateScreen()
        clickListeners()
    }

    private fun clickListeners() {
        when(type){
            ConfirmationType.CAR_UPDATE.ordinal->{
                allow.setOnClickListener {
                    takeAppropriateAllowAction()
                }

                deny.setOnClickListener {
                    takeAppropriateDenyAction()
                }
            }
            ConfirmationType.BOOKING.ordinal->{
                allow.setOnClickListener {
                    takeAppropriateAllowAction()
                }

                deny.setOnClickListener {
                    takeAppropriateDenyAction()
                }
            }
            else->{
                lister_end.setOnClickListener {
                    takeEndAction(ConfirmationOption.LISTER_END)
                }

                lister_book.setOnClickListener {
                    takeBookAction(ConfirmationOption.LISTER_BOOK)
                }

                renter_end.setOnClickListener {
                    takeEndAction(ConfirmationOption.RENTER_END)
                }

                renter_book.setOnClickListener {
                    takeBookAction(ConfirmationOption.RENTER_BOOK)
                }
            }
        }
    }

    private fun takeBookAction(option: ConfirmationOption) {
        listener?.onAction(type as Int, option.ordinal)
        dismissFragment(option.ordinal)
    }

    private fun takeEndAction(option: ConfirmationOption) {
        listener?.onAction(type as Int, option.ordinal)
        dismissFragment(option.ordinal)
    }

    override fun onResume() {
        super.onResume()
        activity?.resources?.let {
            dialog?.window?.setLayout(it.getDimension(R.dimen.dialog_width_large).toInt() , it.getDimension(R.dimen.dialog_height_large).toInt())
            dialog?.window?.setGravity(Gravity.CENTER)
        }
    }

    fun initListener(listener : ConfirmationFragment.ConfirmationFragmentCallBack<Any>) : ConfirmationFragment{
        this.listener = listener
        return this
    }

    private fun takeAppropriateDenyAction() {
        when(type){
            ConfirmationType.CAR_UPDATE.ordinal ->{
                carUpdateAction(ConfirmationOption.DENY)
            }
            ConfirmationType.BOOKING.ordinal ->{
                carBookAction(ConfirmationOption.DENY)
            }
        }
    }

    private fun updateScreen() {
        when(type){
            ConfirmationType.CAR_UPDATE.ordinal ->{
                carUpdateScreen()
            }
            ConfirmationType.BOOKING.ordinal ->{
                bookingUpdateScreen()
            }
            ConfirmationType.OK.ordinal ->{
                okUpdateScreen()
            }
            ConfirmationType.DONE.ordinal ->{
                doneUpdateScreen()
            }
        }
    }

    private fun bookingUpdateScreen() {
        warning.text = getString(R.string.are_you_sure)
    }

    private fun doneUpdateScreen() {
        chat_option_text.text = getString(R.string.done_chat_msg)
        pActivity.show(car, lister_options_layout)
    }

    private fun okUpdateScreen() {
        chat_option_text.text = getString(R.string.ok_chat_msg)
        pActivity.show(chat, renter_options_layout)
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
            ConfirmationType.BOOKING.ordinal ->{
                carBookAction(ConfirmationOption.ALLOW)
            }
        }
    }

    private fun carBookAction(option: ConfirmationFragment.Companion.ConfirmationOption) {
        listener?.onAction(ConfirmationType.BOOKING.ordinal, option.ordinal)
        dismissFragment(option.ordinal)
    }

    private fun carUpdateAction(option: ConfirmationOption) {
        listener?.onAction(ConfirmationType.CAR_UPDATE.ordinal, option.ordinal)
        dismissFragment(option.ordinal)
    }

    private fun dismissFragment(ordinal: Int) {
        instance = null
        when(ordinal){
            ConfirmationOption.DENY.ordinal->{
                dismissAllowingStateLoss()
                activity?.finish()
            }
            else->{
                dismissAllowingStateLoss()
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

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }

    override fun onDetach() {
        instance = null
        super.onDetach()
    }


    interface ConfirmationFragmentCallBack<T>{
        fun onAction(type : T, option : T)
    }

    companion object {
        private var instance : ConfirmationFragment? = null
        @JvmStatic
        fun newInstance(type : Int) = ConfirmationFragment().apply {
            instance = this
            arguments = Bundle().apply {
                putInt(CONFIRMATION_DIALOG_TYPE,type)
            }
            return instance!! // It can't be null at this time
        }


        @JvmStatic
        fun getInstance(type: Int): ConfirmationFragment? {
            instance?.let {
                return null
            } ?: kotlin.run {
                return newInstance(type)
            }
        }

        enum class ConfirmationType {CAR_UPDATE, DATE_TIME, OK, DONE, BOOKING}
        enum class ConfirmationOption {ALLOW, DENY, LISTER_BOOK, LISTER_END , RENTER_BOOK, RENTER_END}
    }

}