/*
package com.mobitribe.qulabro.modules.chat.chat

import android.os.Bundle
import android.util.Log
import android.view.*
import com.mobitribe.qulabro.R
import com.mobitribe.qulabro.models.FCMMessage
import com.mobitribe.qulabro.models.chat.ChatRooms
import com.mobitribe.qulabro.models.chat.Message
import com.mobitribe.qulabro.models.response.GeneralResponse
import com.example.sarwan.renkar.base.ParentActivity.Companion.currentChatUserId
import com.mobitribe.qulabro.network.RestClient
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import kotlinx.android.synthetic.main.chat_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatFragment : ChatBaseFragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chatModel = it.getSerializable(CHAT_ROOM) as? ChatRooms
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chat_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatModel?.let {
           makeChatScreen()
        }
        setViewListeners()
    }

    private fun setViewListeners() {
        editWriteMessage.clearFocus()
        btnSend.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        if (view.id == R.id.btnSend) {
            val content = editWriteMessage.text.toString().trim { it <= ' ' }
            if(memberBlocked()){
                showBlockedDialog()
            } else if (content.isNotEmpty()) {
                editWriteMessage.setText("")
                sendMessage(makeMessageObject(content))
            }
        }
    }

    private fun memberBlocked(): Boolean {
        return chatModel?.chat_members?.any { it.id==blockUserId }!!
    }

    private fun sendMessage(message: Message) {

        if (adapter?.itemCount == 0) setChatRoomFields(message)
        else {
            FirestoreQueryCenter.setLastMessageOfConversation(roomId, message.message!! ,
                    pActivity.profile?.userName!!, pActivity.profile?.id!!, chatModel?.title!!, (chatModel?.chat_members?.map { it.id } as ArrayList<Int>))
        }

        adapter?.addItem(message)
        adapter?.updateLastMessageStatus("sending...", null)
        recyclerChat.scrollToPosition(adapter!!.itemCount - 1)
        FirestoreQueryCenter.getMessageQuery(roomId).add(message).addOnCompleteListener {
            if (it.isSuccessful){
                adapter?.updateLastMessageStatus("sent",null)
            }
        }

        if (!isOpponentOnline)
            sendNotification(message)
    }

    private fun sendNotification(myMessage: Message) {
        val message = FCMMessage()
        message.body = myMessage.message
        message.title = pActivity.profile?.userName
        val members = chatModel?.chat_members?.filter { it.id!=pActivity.profile?.id }?.let {
            for (i in it){
                message.user_ids?.add(i.id!!)
            }
        }
        message.chat_room_id = roomId
        RestClient.getRestAuthenticatedAdapter(pActivity.profile?.token).sendPushNotification(message).enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>?, t: Throwable?) {
                Log.d(TAG, "sendNotification: onFailure" + t?.message)
            }

            override fun onResponse(call: Call<GeneralResponse>?, response: Response<GeneralResponse>?) {
                Log.d(TAG, "sendNotification: onRespone" + response?.message())
            }
        })
    }

    companion object {
        val VIEW_TYPE_USER_MESSAGE = 0
        val VIEW_TYPE_FRIEND_MESSAGE = 1

        */
/**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Chat Fragment.
         *//*

        @JvmStatic
        fun newInstance(chatRoom : ChatRooms?) = ChatFragment().apply {
            arguments = Bundle().apply {
                putSerializable(CHAT_ROOM, chatRoom)
            }
        }
        val TAG = "Chat"
    }

    override fun onResume() {
        super.onResume()
        chatModel?.chat_members?.filter { it.id!=pActivity.profile?.id!! }?.let {
            if (it.isNotEmpty())
                currentChatUserId = it.first().id!!
        }
    }

    override fun onPause() {
        super.onPause()
        currentChatUserId = -1
    }
}

*/
