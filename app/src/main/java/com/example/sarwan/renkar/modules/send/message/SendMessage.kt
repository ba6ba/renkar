package com.example.sarwan.renkar.modules.send.message

import android.content.Intent
import android.os.Handler
import android.widget.Toast
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.fragments.ConfirmationFragment
import com.example.sarwan.renkar.model.History
import com.example.sarwan.renkar.model.chat.ChatRooms
import com.example.sarwan.renkar.model.chat.Message
import com.example.sarwan.renkar.modules.history.HistoryHelper
import com.example.sarwan.renkar.modules.lister.ListerActivity
import com.example.sarwan.renkar.modules.renter.RenterActivity
import com.example.sarwan.renkar.modules.renter.RenterDashBoardActivity
import com.example.sarwan.renkar.utils.ModelMappingUtility

class SendMessage(private val pActivity : ParentActivity,var listener : SendMessageCallBack ? = null) {

    private var roomId : String ? = null
    private var chatRoom : ChatRooms ? = null
    private var firstMessage = false


    fun setChatRoom(chatRoom: ChatRooms) : SendMessage{
        this.chatRoom = chatRoom
        return this
    }

    fun setRoomId(roomId : String) : SendMessage {
        this.roomId = roomId
        return this
    }


    /*
    * firebase Confirmation type message callback
    * */
    fun checkMessageType(message: Message) {
        message.apply {
            when(confirmation_type){
                ConfirmationFragment.Companion.ConfirmationType.OK.ordinal->{
                    takeOkMessageAction(confirmation_option)
                }
                ConfirmationFragment.Companion.ConfirmationType.DONE.ordinal->{
                    takeDoneMessageAction(confirmation_option)
                }
            }
        }
    }

    private fun takeDoneMessageAction(confirmation_option: Int?) {
        when(confirmation_option){
            ConfirmationFragment.Companion.ConfirmationOption.LISTER_BOOK.ordinal->{
                takeDoneBookMessageAction()
            }

            ConfirmationFragment.Companion.ConfirmationOption.LISTER_END.ordinal->{
                takeDoneEndMessageAction()
            }
        }
    }

    private fun takeDoneBookMessageAction() {
        pActivity.showMessage("Renter's request to book a car has been accepted")
        openListerActivity()
    }

    private fun openListerActivity() {
        Handler().postDelayed({
            pActivity.openActivityWithFinish(Intent(pActivity,
                if (pActivity.user?.type == ApplicationConstants.LISTER) ListerActivity::class.java else RenterDashBoardActivity::class.java).
                putExtra(ApplicationConstants.PAGER_NUMBER, 2), Intent.FLAG_ACTIVITY_NEW_TASK)
        },2000L)
    }

    private fun takeDoneEndMessageAction() {
        if (pActivity.user?.type == ApplicationConstants.RENTER){
            pActivity.showMessage("Lister denied for booking")
        }else
            pActivity.showMessage("You denied for booking")
    }

    private fun takeOkMessageAction(confirmation_option: Int?) {
        when(confirmation_option){
            ConfirmationFragment.Companion.ConfirmationOption.RENTER_BOOK.ordinal->{
                if (pActivity.user?.type == ApplicationConstants.LISTER){
                    listener?.attachFragmentRequest(ConfirmationFragment.Companion.ConfirmationType.DONE)
                }
            }
        }
    }

    fun sendConfirmationMessageToFirebase(typeName : String, typeOrdinal: Int, optionOrdinal: Int,
                                          name : String = pActivity.user?.name?.let { it }?:"" , email : String = pActivity.user?.email?.let { it }?:"") {
        if (name.isNotEmpty() && email.isNotEmpty())
            performMessageSendingTasks(Message((typeName), email, name, typeOrdinal, optionOrdinal))
    }

    fun setAdapterCount(count: Boolean) : SendMessage {
        firstMessage = count
        return this
    }

    fun performMessageSendingTasks(message: Message){
        setLastMessageOfChatRoom(message)
        sendMessage(message)
    }

    private fun sendMessage(message: Message) {
        roomId?.let {
            FirestoreQueryCenter.getMessageQuery(it).add(message)
        }
    }

    private fun setLastMessageOfChatRoom(message: Message) {
        if (firstMessage) setChatRoomFields(message)
        else {
            roomId?.let {
                FirestoreQueryCenter.setLastMessageOfConversation(it, message.message ,
                    pActivity.user?.name, pActivity.user?.email, chatRoom?.title)
            }
        }
    }

    /**
     * @param first
     * @usage It sets the fields on firebase and make room id on the basis of who is first and second in the openent
     *          the big one is always be a first and the small one will be called as second.
     */
    private fun setChatRoomFields(message: Message) {
        roomId?.let {roomId->
            chatRoom?.chat_members.let { members->
                members?.add(ModelMappingUtility.makeChatMember(pActivity.user))
                FirestoreQueryCenter.addChatRoom(roomId, ModelMappingUtility.createChatRoom(members,
                    chatRoom?.title, message.message, pActivity.user?.name, pActivity.user?.email,
                    message.timestamp, chatRoom?.car_number, chatRoom?.carAvailabilityDays))
            }
        }
    }

    fun takeOkConfirmationAction(type : Int , option : Int){
        when(option){
            ConfirmationFragment.Companion.ConfirmationOption.RENTER_BOOK.ordinal->{
                Toast.makeText(pActivity, pActivity.getString(R.string.ok_msg), Toast.LENGTH_LONG).show()
                sendConfirmationMessageToFirebase(ConfirmationFragment.Companion.ConfirmationType.OK.name, type, option)
                HistoryHelper.create(ModelMappingUtility.createHistory(chatRoom, pActivity.user, History.STATUS.REQUEST_PENDING))
            }
            ConfirmationFragment.Companion.ConfirmationOption.RENTER_END.ordinal->{
                pActivity.finish()
            }
        }
    }

    fun takeDoneConfirmationAction(type: Int, option: Int) {
        when(option){
            ConfirmationFragment.Companion.ConfirmationOption.LISTER_BOOK.ordinal->{
                sendConfirmationMessageToFirebase(ConfirmationFragment.Companion.ConfirmationType.DONE.name, type, option)
                HistoryHelper.create(ModelMappingUtility.createHistory(chatRoom, pActivity.user, History.STATUS.REQUEST_APPROVED))
            }
            ConfirmationFragment.Companion.ConfirmationOption.LISTER_END.ordinal->{
                sendConfirmationMessageToFirebase(ConfirmationFragment.Companion.ConfirmationOption.DENY.name, type, option)
                HistoryHelper.create(ModelMappingUtility.createHistory(chatRoom, pActivity.user, History.STATUS.REQUEST_DECLINED))
            }
        }
    }


    interface SendMessageCallBack{
        fun attachFragmentRequest(type: ConfirmationFragment.Companion.ConfirmationType)
    }

}