/*
package com.mobitribe.qulabro.modules.chat.chat

import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.mobitribe.qulabro.R
import com.mobitribe.qulabro.extras.ApplicationConstants
import com.mobitribe.qulabro.models.FCMProfile
import com.mobitribe.qulabro.models.chat.ChatRooms
import com.example.sarwan.renkar.base.ParentActivity
import com.mobitribe.qulabro.utils.DateTimeUtility
import firebase.VFirebaseQueryManager
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import kotlinx.android.synthetic.main.chat_activity.*
import java.lang.Exception
import java.util.*

class ChatActivity : ParentActivity()  {

    private var chatRoom: ChatRooms? = null
    private var chatFragment : ChatFragment ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)
        setSupportActionBar(chatToolbar)
        initializeView()
        getIntentData()
        onClickListeners()
    }

    private fun onClickListeners() {
        chatToolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.block) {
                // do something
                if (chatFragment?.blockUserId == 0)
                    chatFragment?.showBlockWarning()
                else chatFragment?.unBlockTheUser()
            }
            false
        }
    }

    private fun initializeView() {

        //Fragment
        chatFragment = supportFragmentManager.findFragmentById(R.id.chatFragment) as ChatFragment

        // Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setToolbarTitle() {
        supportActionBar?.name = if (chatRoom?.group!!) chatRoom?.name?.capitalize() else chatRoom?.chat_members?.find { it.id!=profile?.id }?.name?.capitalize()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getIntentData() {
        chatRoom = intent.getSerializableExtra(ApplicationConstants.CHAT_OBJECT) as? ChatRooms
        chatRoom?.let {
            if (it.key==null){
                chatRoom?.chat_members?.let {member->
                    it.key = ApplicationConstants.CHAT_ROOM_PREFIX+getIdForOneToOne(profile?.id, member.first().id)
                }
            }
            initFragment(it)
            setToolbarTitle()
        }
    }

    private fun getIdForOneToOne(myId: Int?, contactId: Int?): String {
        return if (myId?.toLong()!! > contactId?.toLong()!!)
            "$myId-$contactId"
        else
            "$contactId-$myId"
    }

    private fun initFragment(room: ChatRooms) {
        chatFragment?.initChat(room)
    }

    private fun updateMenuOptions(group: Boolean) {
        if (group) chatToolbar.menu.getItem(0).isVisible = false
        else checkIfUserBlocked()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        updateMenuOptions(chatRoom?.group!!)
        return true
    }

    */
/**
     * @usage It checks that I have blocked the Oponent or Oponent has blocked me for the conversation
     *        If any one is blocked so it changes the view accordingly
     *//*

    private fun checkIfUserBlocked() {
        chatRoom?.key?.let {
            FirestoreQueryCenter.checkIfBlockUser(it).addSnapshotListener(documentListener)
        }
    }

    private val documentListener = EventListener<DocumentSnapshot> { documentSnapshot, fireStoreException ->
        try {
            fireStoreException?.let {
                return@EventListener
            }?:kotlin.run {
                documentSnapshot?.let {query->
                    getBlockStatus(query.data!!, query.id)
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    private fun getBlockStatus(documents: MutableMap<String, Any>, key: String) {
        chatFragment?.blockUserId = documents[FirestoreQueryCenter.BLOCK_USER]?.toString()?.toInt()?:kotlin.run { 0 }
            if (chatFragment?.blockUserId == profile?.id)
            {
                chatFragment?.visibleBlockUserLayout()
                chatToolbar.menu.getItem(0).isVisible = false
            } else if (chatRoom?.chat_members?.any { member-> member.id == chatFragment?.blockUserId }!!) {
                try {
                    chatToolbar.menu.getItem(0).name = "Unblock"
                } catch (e:Exception){
                    Log.d("","")
                }
            } else {
                checkOpponentOnlineStatus()
                chatFragment?.invisibleBlockUserLayout()
                chatToolbar.menu.getItem(0).name = "Block"
            }
        hideProgress()
    }

    private fun checkOpponentOnlineStatus() {
        chatRoom?.chat_members?.let {
            if (it.size == 1){
                VFirebaseQueryManager.getUserReference(it.find { member-> member.id!=profile?.id }?.id?.toLong()!!).
                        addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(snapshot: DataSnapshot) {

                                val profile = snapshot.getValue(FCMProfile::class.java)
                                if (profile != null) {
                                    if (profile.online) {
                                        chatFragment?.isOpponentOnline = true
                                        supportActionBar?.subtitle = "Online"
                                    } else {
                                        chatFragment?.isOpponentOnline = false
                                        supportActionBar?.subtitle = "Last online : ${onlineDate(profile.last_online)}"
                                        chatFragment?.isOpponentLogin = profile.login
                                    }
                                }
                            }
                        })
            }
        }
    }

    private fun onlineDate(last_online: Long): String {
        return if((Date(last_online).date.equals(Date().date))
                && (Date(last_online).month.plus(1).equals(Date().month.plus(1)))
                && (Date(last_online).year.equals(Date().year))){
            DateTimeUtility.timeFormat.format(last_online)
        }
        else {
            DateTimeUtility.dateFormat.format(last_online)
        }
    }

    override fun onResume() {
        super.onResume()
        goOnline()
    }

    override fun onDestroy() {
        super.onDestroy()
        goOffline()
    }
}*/
