/*
package com.mobitribe.qulabro.modules.chat.conversation

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.mobitribe.qulabro.R
import com.mobitribe.qulabro.extras.ApplicationConstants
import com.mobitribe.qulabro.models.chat.ChatRooms
import com.example.sarwan.renkar.base.ParentActivity
import com.mobitribe.qulabro.modules.chat.chat.ChatActivity
import kotlinx.android.synthetic.main.conversation_list_item.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class ConversationAdapter(private val activity: ParentActivity,
                          private var contactsList: ArrayList<ChatRooms>,
                          private var filteredContactList: ArrayList<ChatRooms> = contactsList)
    : androidx.recyclerview.widget.RecyclerView.Adapter<ConversationAdapter.ViewHolder>(), Filterable {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_item, null)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.loadData(filteredContactList.get(position),position)
    }

    override fun getItemCount(): Int {
        return filteredContactList.size
    }


    fun swap(records: ArrayList<ChatRooms>?) {
        records?.let { it ->
            contactsList.clear()
            contactsList.addAll(it)
            filteredContactList.clear()
            filteredContactList.addAll(it)
            filteredContactList.sortByDescending { it.last_message_time }
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredContactList = contactsList
                } else {
                    val filteredList = ArrayList<ChatRooms>()
                    for (row in contactsList) {

                        // title match condition. this might differ depending on your requirement
                        // here we are looking for title or phone number match
                        row.title?.let {
                            if (it.toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row)
                            }
                        }
                    }

                    filteredContactList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = filteredContactList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                filteredContactList = filterResults.values as ArrayList<ChatRooms>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun loadData(chatRoom: ChatRooms, position: Int){

            itemView.contactName.text = if (chatRoom.group) chatRoom.title ?:kotlin.run { "" } else chatRoom.chat_members.find { it.id!=activity.profile?.id }?.name
            makeContactIcon(itemView.contactIcon, if (chatRoom.group) chatRoom.title ?:kotlin.run { "" } else chatRoom.chat_members.find { it.id!=activity.profile?.id }?.name)
            checkIfReadMessage(chatRoom)
            itemView.tag = position
            itemView.setOnClickListener {
                val pos = it.tag as Int
                openChatActivity(pos)
            }

            itemView.lastMessage.text = chatRoom.last_message ?:kotlin.run { "" }

            chatRoom.last_message_time?.let {
                itemView.lastMessageTime.text = PrettyTime().format(Date(it*1000L))
            }?: run {
                itemView.lastMessageTime.text = PrettyTime().format(Date())
            }

        }

        private fun checkIfReadMessage(chatRoom: ChatRooms) {
            chatRoom.chat_members.find { it.id==activity.profile?.id }?.read?.let {
                if (it)
                    itemView.unread.visibility = View.INVISIBLE
                else if (chatRoom.last_message_sender_id != activity.profile?.id.toString())
                    itemView.unread.visibility = View.VISIBLE
            }
        }
    }

    fun makeContactIcon(textView: TextView, text: String?){
        var iconName = ""

        try {
            text?.let {
                iconName = text.get(0).toString()
                if (text.split(" ").size > 1)
                    iconName += text.split(" ").get(1).get(0)
            }
        } catch (e: Exception)
        {
            Log.d("makeContactIcon", e.message)
        }
        textView.text = iconName
    }

    private fun getItem(pos: Int): ChatRooms {
        return filteredContactList[pos]
    }

    private fun openChatActivity(pos: Int) {
        val bundle = Bundle()
        val chatRoom = getItem(pos)
        chatRoom.chat_members.remove(chatRoom.chat_members.find { it.id==activity.profile?.id })
        bundle.putSerializable(ApplicationConstants.CHAT_OBJECT, chatRoom)
        activity.openActivity(ChatActivity::class.java, bundle)
    }

    fun addItem(value: ChatRooms) {
        filteredContactList.add(value)
        filteredContactList.sortByDescending { it.last_message_time }
        notifyDataSetChanged()
    }

    fun updateItem(value: ChatRooms, key: String?) {
        value.key = key
        for(i in 0 until filteredContactList.size){
            if (filteredContactList[i].key.equals(key))
                filteredContactList[i] = value
        }
        filteredContactList.sortByDescending { it.last_message_time }
        notifyDataSetChanged()
    }
}*/
