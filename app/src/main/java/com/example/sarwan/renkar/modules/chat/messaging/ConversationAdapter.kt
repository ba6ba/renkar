package com.example.sarwan.renkar.modules.chat.messaging

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.model.chat.ChatRooms
import com.example.sarwan.renkar.utils.StringUtility
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.conversation_list_item.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class ConversationAdapter(private val activity: ParentActivity,
                          private var conversationsList: ArrayList<ChatRooms>,
                          private val fragment : ConversationFragment)
    : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_item, null)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.loadData(position)
    }

    override fun getItemCount(): Int {
        return conversationsList.size
    }


    fun swapAllData(records: ArrayList<ChatRooms>?) {
        records?.let { it ->
            conversationsList.clear()
            conversationsList.addAll(it)
            conversationsList.sortByDescending { it.last_message_time }
            notifyDataSetChanged()
        }
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun loadData(position: Int){

            itemView.contact_name.text = conversationsList[position].chat_members.find { it.email!=activity.user?.email }?.name
            itemView.icon.text = StringUtility.makeInitials(itemView.contact_name.text.toString())
            itemView.read.visibility = if (conversationsList[position].read?.contains(activity.user?.email) == true) View.GONE else View.VISIBLE
            itemView.tag = position
            itemView.setOnClickListener {
                val pos = it.tag as Int
                openChatActivity(pos)
            }
        }
    }

    private fun getItem(pos: Int): ChatRooms {
        return conversationsList[pos]
    }

    private fun openChatActivity(pos: Int) {
        fragment.interactionListener?.onCellClick(conversationsList[pos])
    }

    fun addItem(value: ChatRooms) {
        if (!conversationsList.contains(value)){
            conversationsList.add(value)
            conversationsList.sortByDescending { it.last_message_time }
            notifyDataSetChanged()
        }
    }

    fun updateItem(value: ChatRooms) {
        for(i in 0 until conversationsList.size){
            if (conversationsList[i].key.equals(value.key))
                conversationsList[i] = value
        }
        conversationsList.sortByDescending { it.last_message_time }
        notifyDataSetChanged()
    }

    interface ConversationFragmentListener{
        fun onCellClick(chatRooms: ChatRooms)
    }
}