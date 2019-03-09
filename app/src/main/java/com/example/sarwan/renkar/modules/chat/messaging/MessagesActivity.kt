package com.example.sarwan.renkar.modules.chat.messaging

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.model.chat.ChatRooms

class MessagesActivity : ParentActivity(), ConversationAdapter.ConversationFragmentListener{

    var isChat = false
    private var chatRoom : ChatRooms ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.messages_activity)
        getIntentData()
    }

    private fun getIntentData() {
        isChat = intent?.extras?.getBoolean(ApplicationConstants.IS_CHAT)?:false
        chatRoom = intent?.extras?.getSerializable(ApplicationConstants.CHAT_ROOM) as ChatRooms
        attachAppropriateFragment()
    }

    private fun attachAppropriateFragment() {
            attachFragment(if (isChat) ChattingFragment.newInstance(chatRoom?.let { it }?: ChatRooms()
            ) else ConversationFragment.newInstance())
    }

    private fun attachFragment(fragment: Fragment) {
        supportFragmentManager.apply {
            beginTransaction().replace(R.id.messages_fragment_container, fragment)
                .setCustomAnimations(R.anim.popup_enter, R.anim.pop_up_exit).
                    addToBackStack(null).commit()
        }
    }

    override fun onCellClick(chatRooms: ChatRooms) {
        attachFragment(ChattingFragment.newInstance(chatRooms))
    }

}