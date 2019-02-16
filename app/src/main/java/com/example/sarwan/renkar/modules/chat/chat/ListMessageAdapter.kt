/*
package com.mobitribe.qulabro.modules.chat.chat

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobitribe.qulabro.R
import com.mobitribe.qulabro.models.UserProfile
import com.mobitribe.qulabro.models.chat.ChatRooms
import com.mobitribe.qulabro.models.chat.Message

import java.util.ArrayList

class ListMessageAdapter(private val context: Context, private val messages: ArrayList<Message>,
                         private val profile: UserProfile, private val chatRooms: ChatRooms) : androidx.recyclerview.widget.RecyclerView.Adapter<ItemMessageUserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMessageUserHolder {
        val view: View
        if (viewType == ChatFragment.VIEW_TYPE_USER_MESSAGE) {
            view = LayoutInflater.from(context).inflate(R.layout.message_right, parent, false)
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.message_left, parent, false)

        }
        return ItemMessageUserHolder(view, profile, context,this,chatRooms)
    }

    override fun onBindViewHolder(holder: ItemMessageUserHolder, position: Int) {
        holder.loadData(messages[position], position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender_id == profile.id) ChatFragment.VIEW_TYPE_USER_MESSAGE else ChatFragment.VIEW_TYPE_FRIEND_MESSAGE
    }

    fun swapList(messageArrayList: ArrayList<Message>?) {
        if (messageArrayList != null){
            messages.clear()
            messages.addAll(messageArrayList)
            messages.sortBy { it.timestamp }
            notifyDataSetChanged()
        }
    }

    fun addItem(message: Message) {
        messages.add(itemCount, message)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun updateLastMessageStatus(text: String, readBy : ArrayList<Int>?) {
        messages[itemCount - 1].messageStatus = text
        messages[itemCount - 1].readBy = readBy
        notifyDataSetChanged()
    }
}
*/
