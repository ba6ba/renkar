/*
package com.mobitribe.qulabro.modules.chat

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobitribe.qulabro.R
import com.mobitribe.qulabro.dialogs.BaseDialog
import com.mobitribe.qulabro.models.chat.ChatRoom
import com.mobitribe.qulabro.models.chat.ChatRooms
import com.mobitribe.qulabro.utils.DateTimeUtility
import kotlinx.android.synthetic.main.dialog_recent_message.*

open class ChatPushDialog : BaseDialog(){
    private val TIMEOUT: Long = 3000
    private val TAG = "JobSort"
    private var conversation: ChatRooms? = null
    private var mListener: onMessageDialog? = null

    companion object {
        private val ARG_PARAM1 = "param1"

        @JvmStatic
        fun newInstance(conversation: ChatRooms?): ChatPushDialog {
            val dialogMessagePush = ChatPushDialog()
            val args = Bundle()
            args.putSerializable(ARG_PARAM1, conversation)
            dialogMessagePush.arguments = args
            return dialogMessagePush
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            conversation = getArguments()?.getSerializable(ARG_PARAM1) as ChatRooms
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.dialog_recent_message, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        message_text.text = conversation?.last_message
        business_name.text = conversation?.last_message_sender?.capitalize()
        message_time.text = DateTimeUtility.getLocalDateTime(conversation?.last_message_time!!)

        // to Transparent the backGround
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        Handler().postDelayed({ dismissAllowingStateLoss() }, TIMEOUT)
        messagePopup.setOnClickListener{
            mListener!!.messageDialogClicked(conversation)
            dismissAllowingStateLoss()
        }
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)


        dialog.window!!.attributes.windowAnimations = R.style.Animation_DialogMessage_Window
        dialog.window!!.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
        dialog.window!!.attributes.x = getActivity()?.getResources()?.getDimensionPixelSize(R.dimen.small_margin)!!
        dialog.window!!.attributes.y = getActivity()?.getResources()?.getDimensionPixelSize(R.dimen.small_margin)!!

        return dialog
    }

    interface onMessageDialog {
        fun messageDialogClicked(conversation: ChatRooms?)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mListener = activity as onMessageDialog
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement MainInteractionListener")
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}*/
