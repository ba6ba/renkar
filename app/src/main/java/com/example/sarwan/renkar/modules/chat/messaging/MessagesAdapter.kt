package com.example.sarwan.renkar.modules.chat.messaging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.fragments.ConfirmationFragment
import com.example.sarwan.renkar.model.User
import com.example.sarwan.renkar.model.chat.ChatRooms
import com.example.sarwan.renkar.model.chat.Message
import kotlinx.android.synthetic.main.my_message_layout.view.*

import java.util.ArrayList

class MessagesAdapter(private val activity: ParentActivity, private val messages: ArrayList<Message>,
                      private val user: User, private val chatRooms: ChatRooms,
                      private val listener : MessagesAdapterCallBack) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    private var sizeOfList : Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when(viewType){
            ChattingFragment.Companion.MESSAGE_TYPE.MY_MESSAGE.ordinal->{
                MessageViewHolder(
                    LayoutInflater.from(activity).inflate(R.layout.my_message_layout, parent, false))
            }
            else -> MessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.opponent_message_layout, parent, false))
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.loadData(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender_email == user.email) ChattingFragment.Companion.MESSAGE_TYPE.MY_MESSAGE.ordinal
        else ChattingFragment.Companion.MESSAGE_TYPE.OPPONENT_MESSAGE.ordinal
    }

    fun swapList(messageArrayList: ArrayList<Message>?) {
        if (messageArrayList != null){
            messages.clear()
            messages.addAll(messageArrayList)
            setSizeOfList(messages.size)
            messages.sortBy { it.timestamp }
            notifyDataSetChanged()
        }
    }

    fun addItem(message: Message) {
        messages.add(itemCount, message)
        notifyDataSetChanged()
    }

    private fun setSizeOfList(size : Int){
        sizeOfList = size
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun updateLastMessageStatus(text: String, readBy : ArrayList<String>?) {
        messages[itemCount - 1].messageStatus = text
        messages[itemCount - 1].readBy = readBy
        notifyItemChanged(itemCount-1)
    }

    inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun loadData(position: Int) {
            setMessage(messages[position])
            checkForConfirmationMessages(messages[position])
            if (position == sizeOfList -1){
                listener.onLastMessage(messages[position],position)
                setReadStatus(messages[position])
            }
        }

        private fun checkForConfirmationMessages(message: Message) {
            itemView.chatCardView.background = map[message.message]?.let {
                activity.resources.getDrawable(it) }?:
                    itemView.chatCardView.background
        }

        private fun setMessage(message: Message) {
            itemView.text_message.text = message.message
        }

        private fun setReadStatus(message: Message) {
            if (message.sender_email==user.email)
                itemView.read_message.visibility =
                        if (message.readBy?.contains(chatRooms.chat_members.find { it.email != user.email }?.email) == true) View.VISIBLE else View.GONE
        }
    }

    val map = hashMapOf(ConfirmationFragment.Companion.ConfirmationOption.DENY.name to R.drawable.deny_message_bg,
        ConfirmationFragment.Companion.ConfirmationType.OK.name to R.drawable.ok_message_bg,
        ConfirmationFragment.Companion.ConfirmationType.DONE.name to R.drawable.done_message_bg)

    interface MessagesAdapterCallBack{
        fun onLastMessage(message: Message, position: Int)
    }
}
