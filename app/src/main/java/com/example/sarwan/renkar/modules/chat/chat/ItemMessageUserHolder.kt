/*
package com.mobitribe.qulabro.modules.chat.chat

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mobitribe.qulabro.R
import com.mobitribe.qulabro.models.UserProfile
import com.mobitribe.qulabro.models.chat.ChatMembers
import com.mobitribe.qulabro.models.chat.ChatRooms
import com.mobitribe.qulabro.models.chat.Message
import com.mobitribe.qulabro.utils.DateTimeUtility
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ItemMessageUserHolder(itemView: View, private val profile: UserProfile,
                            private val activity: Context, private val listMessageAdapter: ListMessageAdapter,
                            private val chatRoom: ChatRooms) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    var messageText: TextView
    var username: TextView? = null
    var messageTime: TextView
    var messageStatus: LinearLayout
    var isLeft : Boolean = false
    lateinit var memberImage: ImageView
    lateinit var memberIcon: TextView
    var cardView: LinearLayout

    init {
        messageText = itemView.findViewById(R.id.textMessage)
        messageTime = itemView.findViewById(R.id.messageTime)
        cardView = itemView.findViewById(R.id.chatCardView)
        messageStatus = itemView.findViewById(R.id.message_status)
        itemView.findViewById<TextView>(R.id.icon)?.let {
            isLeft = true
            memberIcon = it
        }
        itemView.findViewById<ImageView>(R.id.image)?.let {
            isLeft = true
            memberImage = it
        }
    }

    fun loadData(message: Message, position: Int) {
        setMessage(message,position)
        checkForLeftIcon(message)
        setReadStatus(message)
        readStatusVisibility(message)
    }

    private fun setMessage(message: Message, position: Int) {
        messageText.text = message.message
        messageTime.text = DateTimeUtility.getLocalDateTime(message.timestamp)
        if (profile.id == message.sender_id) {
            cardView.background = activity.resources.getDrawable(R.drawable.message_bg)
            messageText.setTextColor(ContextCompat.getColor(activity, android.R.color.white))
        } else {
            cardView.background = activity.resources.getDrawable(R.drawable.message_bg_3)
            messageText.setTextColor(ContextCompat.getColor(activity, R.color.black))
        }
    }

    private fun readStatusVisibility(message: Message) {
        val lastPosition = listMessageAdapter.itemCount - 1
        messageStatus.visibility = if (position == lastPosition &&
                message.sender_id?.equals(profile.id)!!) View.VISIBLE else View.GONE
    }

    private fun setReadStatus(message: Message) {
        if(message.messageStatus=="sent") {
            makeStatusIcon(null)
        }
        else if(message.messageStatus=="read") {
            addReadMemberIcons(message)
        }
    }

    private fun checkForLeftIcon(message: Message) {
        if (chatRoom.group && isLeft)
            setContactIcon(chatRoom, message)
    }

    private fun addReadMemberIcons(message: Message) {
        removeViews()
        message.readBy?.let {messages->
            for (i in messages){
                val member = chatRoom.chat_members.find { chatMember-> chatMember.id==i }
                member?.image?.let {imagePath->
                    if(imagePath.isNotEmpty()){
                        makeSeenContactImage(imagePath)
                    }
                }?:kotlin.run {
                    makeStatusIcon(member)
                }
            }
        }
    }

    private fun makeSeenContactImage(imagePath: String) {
        val image = CircleImageView(activity)
        val params  = LinearLayout.LayoutParams(activity.resources.getDimension(R.dimen.message_seen_status_image_size).toInt(),
        activity.resources.getDimension(R.dimen.message_seen_status_image_size).toInt())
        params.setMargins(5,5,5,5)
        image.layoutParams = params
        image.maxHeight = activity.resources.getDimension(R.dimen.message_seen_status_image_size).toInt()
        image.maxWidth = activity.resources.getDimension(R.dimen.message_seen_status_image_size).toInt()
        image.scaleType = ImageView.ScaleType.CENTER_CROP
        messageStatus.addView(image)
        Picasso.get().load(imagePath).fit().into(image)
    }

    private fun makeStatusIcon(member: ChatMembers?) {
        val textView = TextView(activity)
        val params  = LinearLayout.LayoutParams(activity.resources.getDimension(R.dimen.message_seen_status_icon_size).toInt(),
                activity.resources.getDimension(R.dimen.message_seen_status_icon_size).toInt())
        params.setMargins(5,5,5,5)
        textView.layoutParams = params
        textView.background = activity.resources.getDrawable(R.drawable.orange_circle)
        textView.setTextColor(activity.resources.getColorStateList(R.color.white))
        textView.textSize = 8f
        textView.gravity = Gravity.CENTER
        member?.let {
            messageStatus.addView(textView)
            makeContactIcon(textView,member?.name)
        }?:kotlin.run {
            removeViews()
            messageStatus.addView(textView)
            textView.text = activity.resources.getString(R.string.check_mark)
        }
    }

    private fun removeViews() {
        messageStatus.removeAllViewsInLayout()
        messageStatus.removeAllViews()
    }

    private fun setContactIcon(contact: ChatRooms, message: Message) {
        contact.chat_members.find { message.sender_id==it.id }?.let  {
            it.image?.let { image->
                if (image.isNotEmpty()){
                    changeView(View.VISIBLE, View.GONE)
                    Picasso.get().load(image).fit().into(memberImage)
                }else {
                    changeView(View.GONE, View.VISIBLE)
                    makeContactIcon(memberIcon, it.name)
                }
            }?:kotlin.run {
                changeView(View.GONE, View.VISIBLE)
                makeContactIcon(memberIcon, it.name)
            }
        }?:kotlin.run {
            changeView(View.GONE, View.VISIBLE)
            memberIcon.text = ""
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

    private fun changeView(imageVisibility : Int, iconVisibility : Int) {
        memberImage.visibility = imageVisibility
        memberIcon.visibility = iconVisibility
    }

}
*/
