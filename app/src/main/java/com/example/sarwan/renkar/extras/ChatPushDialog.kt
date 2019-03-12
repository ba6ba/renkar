/*
package com.example.sarwan.renkar.extras

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.menuspring.eet.golootlo.R
import com.untilities.DateTimeUtility
import kotlinx.android.synthetic.main.dialog_recent_message.*
import model.ChatDialogBody
import model.ChatRoom

open class ChatPushDialog : BaseDialog(){
    private val TIMEOUT: Long = 3000
    private val TAG = "JobSort"
    private var conversation: ChatDialogBody? = null
    private var mListener: onMessageDialog? = null

    companion object {
        private val ARG_PARAM1 = "param1"
        private var instance : ChatPushDialog? = null
        @JvmStatic
        fun newInstance(conversation: ChatRoom?): ChatPushDialog {
            instance = ChatPushDialog()
            val args = Bundle()
            args.putSerializable(ARG_PARAM1, ChatDialogBody.makeAnObject(conversation))
            instance?.arguments = args
            return instance!! // It can't be null at this time
        }

        @JvmStatic
        fun getInstance(conversation: ChatRoom?): ChatPushDialog? {
            instance?.let {
                return null
            } ?: kotlin.run {
                return newInstance(conversation)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            conversation = getArguments()?.getSerializable(ARG_PARAM1) as ChatDialogBody
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.dialog_recent_message, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        message_text?.setText(conversation?.last_message)
        business_name?.setText(conversation?.last_message_sender?.capitalize())
        conversation?.last_message_time?.let {
            message_time?.setText(DateTimeUtility.getLocalDateTime(it))
        }

        // to Transparent the backGround
        getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(0))
        Handler().postDelayed({
            dismissAllowingStateLoss()
            instance = null
        }, TIMEOUT)
        messagePopup.setOnClickListener{
            mListener?.messageDialogClicked(conversation)
            dismissAllowingStateLoss()
            instance = null
        }
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.getWindow()?.getAttributes()?.windowAnimations = R.style.Animation_DialogMessage_Window
        dialog.getWindow()?.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
        dialog.getWindow()?.getAttributes()?.x = getActivity()?.getResources()?.getDimensionPixelSize(R.dimen.small_margin)
        dialog.getWindow()?.getAttributes()?.y = getActivity()?.getResources()?.getDimensionPixelSize(R.dimen.small_margin)

        return dialog
    }

    interface onMessageDialog {
        fun messageDialogClicked(conversation: ChatDialogBody?)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mListener = activity as onMessageDialog
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener")
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}*/
