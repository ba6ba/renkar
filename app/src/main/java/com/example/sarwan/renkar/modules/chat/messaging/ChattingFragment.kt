package com.example.sarwan.renkar.modules.chat.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.fragments.ConfirmationFragment
import com.example.sarwan.renkar.model.chat.ChatRooms
import com.example.sarwan.renkar.model.chat.Message
import kotlinx.android.synthetic.main.chat_fragment.*

class ChattingFragment : ChattingBaseFragment(), View.OnClickListener{

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
    }


    override fun onClick(view: View) {
        when(view.id){
            R.id.btnSend -> {
                val content = editWriteMessage.text.toString().trim { it <= ' ' }
                if (content.isNotEmpty()) {
                    checkSendingMessageType(content)
                    editWriteMessage.setText("")
                }
            }

            R.id.sorry->{
                checkSendingMessageType(sorry.text.toString())
            }
            R.id.ok->{
                checkSendingMessageType(ok.text.toString())
            }
            R.id.negotiable->{
                checkSendingMessageType(negotiable.text.toString())
            }
            R.id.done->{
                checkSendingMessageType(done.text.toString())
            }
            R.id.available_days->{
                checkSendingMessageType(available_days.text.toString())
            }
        }
    }

    private fun checkSendingMessageType(text: String) {
        when (text) {
            getString(R.string.ok) -> {
                attachDialogFragment(ConfirmationFragment.Companion.ConfirmationType.OK)
            }
            getString(R.string.done) -> {
                // TODO -- firebase message with done
                //attachDialogFragment(ConfirmationFragment.Companion.ConfirmationType.DONE)
            }
            getString(R.string.available_days) -> {
                //attachDialogFragment(ConfirmationFragment.Companion.ConfirmationType.DATE_TIME)
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