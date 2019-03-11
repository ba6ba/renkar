package com.example.sarwan.renkar.modules.chat.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.fragments.ConfirmationFragment
import com.example.sarwan.renkar.model.chat.ChatRooms
import com.example.sarwan.renkar.model.chat.Message
import kotlinx.android.synthetic.main.chat_fragment.*

class ChattingFragment : ChattingBaseFragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chatRoom = it.getSerializable(CHAT_ROOM) as ChatRooms
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chat_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatRoom?.let {
            makeChatScreen()
        }
        updateScreen()
        onClickListener()
    }

    private fun updateScreen() {
        if (pActivity.user?.type == ApplicationConstants.LISTER) pActivity.hide(responses_layout)
    }

    private fun onClickListener() {
        btnSend.setOnClickListener {
            val content = editWriteMessage.text.toString().trim { it <= ' ' }
            if (content.isNotEmpty()) {
                checkSendingMessageType(content)
                editWriteMessage.setText("")
            }
        }

        sorry.setOnClickListener {
            checkSendingMessageType(sorry.text.toString())
        }

        ok.setOnClickListener {
            chatRoom?.timePeriod?.let {days->
                checkSendingMessageType(ok.text.toString())
            }?:kotlin.run {
                attachDaysFragment()
            }
        }

        done.setOnClickListener {
            chatRoom?.timePeriod?.let { days->
                checkSendingMessageType(done.text.toString())
            }?:kotlin.run {
                attachDaysFragment()
            }
        }

        negotiable.setOnClickListener {
            checkSendingMessageType(negotiable.text.toString())
        }

        available_days.setOnClickListener {
            checkSendingMessageType(available_days.text.toString())
        }
    }


    private fun checkSendingMessageType(text: String) {
        when (text) {
            getString(R.string.ok) -> {
                attachDialogFragment(ConfirmationFragment.Companion.ConfirmationType.OK)
            }
            getString(R.string.available_days) -> {
                attachDaysFragment()
            }
            else -> performMessageSendingTasks(makeMessageObject(text))
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(chatRooms: ChatRooms) = ChattingFragment().apply {
            arguments = Bundle().apply {
                putSerializable(CHAT_ROOM, chatRooms)
            }
        }

        enum class MESSAGE_TYPE {MY_MESSAGE, OPPONENT_MESSAGE }
    }
}