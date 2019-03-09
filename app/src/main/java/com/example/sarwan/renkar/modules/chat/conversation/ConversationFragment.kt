/*
package com.mobitribe.qulabro.modules.chat.conversation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener

import com.mobitribe.qulabro.R
import com.mobitribe.qulabro.extras.ApplicationConstants
import com.mobitribe.qulabro.extras.BottomOffsetDecoration
import com.example.sarwan.renkar.model.chat.ChatRooms
import com.example.sarwan.renkar.base.ParentActivity
import com.mobitribe.qulabro.modules.chat.ChatPushDialog
import com.mobitribe.qulabro.modules.main.MainActivity
import com.mobitribe.qulabro.utils.ModelMappingUtility
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import kotlinx.android.synthetic.main.conversation_fragment.*
import kotlinx.android.synthetic.main.search_view.*
import java.lang.Exception
import kotlin.collections.ArrayList


class ConversationFragment : androidx.fragment.app.Fragment(), androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {

    var TAG = "Conversation"
    private var layoutManager : androidx.recyclerview.widget.LinearLayoutManager? = null
    private var contactsAdapter: ConversationAdapter? = null
    private var pActivity : MainActivity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = (activity as MainActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.conversation_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRefreshLayouts()
        initializeLayoutView()
        fetchConversation()
        setListeners()
    }

    private fun setListeners() {
        search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                contactsAdapter?.filter?.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun checkEmptyRecyclerView() {

        if (contactsAdapter?.itemCount == 0) {
            swipeRefreshLayout?.visibility = View.GONE
            swipeRefreshLayoutEmpty?.visibility = View.VISIBLE

        } else {
            swipeRefreshLayout?.visibility = View.VISIBLE
            swipeRefreshLayoutEmpty?.visibility = View.GONE
        }
        stopRefreshLoader()
    }

    private fun setupRefreshLayouts() {
        swipeRefreshLayout?.setOnRefreshListener(this)
        swipeRefreshLayoutEmpty?.setOnRefreshListener(this)
        swipeRefreshLayout?.isRefreshing = true
        swipeRefreshLayoutEmpty?.isRefreshing = true
    }


    private fun stopRefreshLoader(){
        swipeRefreshLayoutEmpty?.isRefreshing = false
        swipeRefreshLayout?.isRefreshing = false
    }

    private fun initializeLayoutView() {
        layoutManager  = androidx.recyclerview.widget.LinearLayoutManager(
            activity,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        contactsAdapter = ConversationAdapter(pActivity!!, ArrayList())
        contactsRecyclerView.layoutManager = layoutManager
        contactsRecyclerView.adapter = contactsAdapter
        contactsRecyclerView?.addItemDecoration(BottomOffsetDecoration(ApplicationConstants.BOTTOM_OFFSET))
    }

    private fun fetchConversation() {
        try {
            pActivity?.profile?.id?.let {
                FirestoreQueryCenter.getConversationById(it).addSnapshotListener(queryListener)
            }
        }catch (e: Exception){
            e.localizedMessage
        }
    }

    private val queryListener = EventListener<QuerySnapshot> { querySnapshot, fireStoreException ->
        try {
            fireStoreException?.let {
                return@EventListener
            }?:kotlin.run {
                querySnapshot?.let {query->
                    when {
                        contactsAdapter?.itemCount!! == 0 -> getConversations(query.documents)
                        query.documentChanges.last().type == DocumentChange.Type.ADDED -> getAddedConversations(query.documentChanges.last())
                        query.documentChanges.last().type == DocumentChange.Type.REMOVED -> getConversations(query.documents)
                        query.documentChanges.last().type == DocumentChange.Type.MODIFIED -> getUpdatedConversations(query.documentChanges.last())
                    }
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    private fun getConversations(documents: MutableList<DocumentSnapshot>) {
        contactsAdapter?.swap(ModelMappingUtility.mapOnChatRoomList(documents))
        checkEmptyRecyclerView()
    }

    private fun getAddedConversations(document: DocumentChange) {
        addItemInAdapter(ModelMappingUtility.mapOnChatRoomModel(document))
        checkEmptyRecyclerView()
    }

    private fun getUpdatedConversations(document: DocumentChange) {
        updateAdapter(ModelMappingUtility.mapOnChatRoomModel(document),document.document.id)
        checkEmptyRecyclerView()
    }

    private fun addItemInAdapter(chatRoom: ChatRooms) {
        contactsAdapter?.addItem(chatRoom)
        showMessagePopUp(chatRoom)
    }

    private fun updateAdapter(chatRoom: ChatRooms, key: String?) {
        contactsAdapter?.updateItem(chatRoom, key)
        showMessagePopUp(chatRoom)
    }

    private fun showMessagePopUp(conversation: ChatRooms) {
        if (shouldMessagePopUpBeShown(conversation)) {
            if (activity !=null) {
                ChatPushDialog.newInstance(conversation).show(childFragmentManager, TAG)
            }
        }
    }

    private fun shouldMessagePopUpBeShown(conversation: ChatRooms): Boolean {
        conversation.last_message_sender_id?.let {
            if(it.equals(pActivity?.profile?.id))
                return false
            else if(ParentActivity.currentChatUserId.equals(it))
                return false
        }?.run {
            return false
        }
        return true
    }

    override fun onRefresh() {
        fetchConversation()
    }

    companion object {
        */
/**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CategoriesFragment.
         *//*

        @JvmStatic
        fun newInstance() = ConversationFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeListener()
    }

    private fun removeListener() {

    }
}
*/
