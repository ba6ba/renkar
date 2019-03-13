package com.example.sarwan.renkar.utils

import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.firebase.FirebaseExtras
import com.example.sarwan.renkar.model.Booking
import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.model.History
import com.example.sarwan.renkar.model.User
import com.example.sarwan.renkar.model.chat.ChatMembers
import com.example.sarwan.renkar.model.chat.ChatRooms
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import kotlin.collections.ArrayList

class ModelMappingUtility {
    companion object {

        fun mapFields(email : String , phone_no : String, first_name : String, last_name : String,
                              username : String, type: String) : Any {
            val user = User()
            user.email = email
            user.phoneNo = phone_no
            user.firstName = first_name
            user.lastName = last_name
            user.userName = username
            user.type = type
            return user
        }


        private fun mapOnChatRoomObject(i: ChatMembers): HashMap<String,Any?> {
            return hashMapOf(
                FirebaseExtras.CHAT_USER_NAME to i.name ,
                FirebaseExtras.CHAT_USER_EMAIL to i.email,
                FirebaseExtras.CHAT_USER_IMAGE to i.image)
        }

         fun mapOnChatRoomModel(i: DocumentSnapshot): ChatRooms? {
            var chatRoom : ChatRooms? = ChatRooms()
            i.data?.let { it ->
                chatRoom = i.toObject(ChatRooms::class.java)
                chatRoom?.chat_members?.addAll(addMembersFromFireStore(it.keys, it))
                chatRoom?.key = i.id
            }
            return chatRoom
        }

        fun mapOnChatRoomList(list: MutableList<DocumentSnapshot>): ArrayList<ChatRooms> {
            val chatRoom : ArrayList<ChatRooms> = ArrayList()
            for (i in list){
                mapOnChatRoomModel(i)?.let {
                    chatRoom.add(it)
                }
            }
            return chatRoom
        }


        private fun addMembersFromFireStore(keys: MutableSet<String>, snapshot: MutableMap<String, Any>): ArrayList<ChatMembers> {
            val chatMembers : ArrayList<ChatMembers> = ArrayList()
            try {
                for (i in keys){
                    if(snapshot[i] !is String && snapshot[i] !is Long && snapshot[i]!=null && i!=FirebaseExtras.READ && i!=FirebaseExtras.CAR_AVAILABILITY_DAYS){
                        chatMembers.add(mapOnChatMembers(snapshot[i] as (MutableMap<String, Any>)))
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
            return chatMembers
        }

        private fun mapOnChatMembers(values: MutableMap<String, Any>): ChatMembers {
            val chatMembers = ChatMembers()
            chatMembers.email = values[FirebaseExtras.CHAT_USER_EMAIL]?.toString()
            chatMembers.image = values[FirebaseExtras.CHAT_USER_IMAGE]?.toString() ?:kotlin.run { null }
            chatMembers.name = values[FirebaseExtras.CHAT_USER_NAME]?.toString()
            return chatMembers
        }

        fun makeChatMember(user: User?) : ChatMembers{
            val chatMember = ChatMembers()
            user?.let {
                chatMember.email = user.email
                chatMember.name = user.name
                chatMember.image = user.imageUrl
            }
            return chatMember
        }

        fun makeChatRoom(
            i: User?,
            me: User?,
            car: Cars?
        ): ChatRooms {
            val chatRoom = ChatRooms()
            i?.let {
                chatRoom.title = """${i.name}-${me?.name}"""
                chatRoom.chat_members.add(makeChatMember(i))
                chatRoom.chat_members.add(makeChatMember(me))
                chatRoom.key = makeChatRoomId(me?.email, i.email,car?.number)
                chatRoom.carAvailabilityDays = car?.days
                chatRoom.car_number = car?.number
            }
            return chatRoom
        }

        private fun makeChatRoomId(myEmail: String?, opponentEmail: String?, number: String?) : String{
            return """cr-$myEmail-$opponentEmail-$number"""
        }

        fun createChatRoom(members: ArrayList<ChatMembers>?, title: String?, message: String? = null,
                           senderName: String? = null, senderEmail: String? = null, messageTime : Long ? = null, carNumber : String?=null,
                           carAvailabilityDays : ArrayList<Int>?=null):
                HashMap<String, Any?> {
            val map : HashMap<String, Any?> = hashMapOf()
            members?.let {member->
                for (i in members){
                    map[HashUtility.md5New(i.email.toString())] = ModelMappingUtility.mapOnChatRoomObject(i)
                }
            }
            map[FirebaseExtras.TITLE] = title
            map[FirebaseExtras.LAST_MESSAGE] = message
            map[FirebaseExtras.LAST_MESSAGE_SENDER] = senderName
            map[FirebaseExtras.LAST_MESSAGE_SENDER_ID] = senderEmail
            map[FirebaseExtras.CAR_NUMBER] = carNumber
            map[FirebaseExtras.CAR_AVAILABILITY_DAYS] = carAvailabilityDays
            map[FirebaseExtras.LAST_MESSAGE_TIME] = messageTime ?:kotlin.run { Calendar.getInstance().timeInMillis / 1000 }
            return map
        }

        fun mapCarOwnerToUser(owner: Cars.Owner?): User? {
            return User().apply {
                owner?.let {
                    imageUrl = it.image
                    name = it.name
                    email = it.email
                }
            }
        }

        fun createHistory(
            chatRoom: ChatRooms?,
            user: User?,
            status : History.STATUS
        ): History {
            return History().apply {
                chatRoom?.let {

                    when(user?.type){
                        ApplicationConstants.RENTER->{
                            rentedBy = user.email
                            listedBy = it.chat_members.find {find-> find.email!=user?.email }?.email
                        }
                        ApplicationConstants.LISTER->{
                            listedBy = user.email
                            rentedBy = it.chat_members.find {find-> find.email!=user?.email }?.email
                        }
                    }

                    name = it.key
                    details = it.title
                    car_number = it.car_number
                    period = it.timePeriod
                    this.status = status.name
                }
            }
        }

        fun makeBookingObject(carNumber: String?, listedBy : String?, rentedBy : String?, period : String?, price : String?) : Booking{
            return Booking().apply {
                this.carNumber = carNumber?:""
                this.listedBy = listedBy?:""
                this.rentedBy = rentedBy?:""
                this.period = period?:""
                this.price = price?:""
            }
        }


    }
}