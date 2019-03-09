package com.example.sarwan.renkar.modules.chat.messaging

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.model.chat.ChatRooms
import com.example.sarwan.renkar.utils.LocationUtility
import com.example.sarwan.renkar.utils.ModelMappingUtility
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.conversation_fragment.*
import java.lang.Exception

class ConversationFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    private var conversationAdapter: ConversationAdapter? = null
    private var pActivity : ParentActivity? = null
    private var conversationList : ArrayList<ChatRooms> = ArrayList()
    var interactionListener : ConversationAdapter.ConversationFragmentListener ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.conversation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRefreshLayouts()
        initializeLayoutView()
        fetchConversation()
    }

    private fun checkEmptyRecyclerView() {
        if (conversationAdapter?.itemCount == 0) {
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
        pActivity?.let {
            conversation_recycler_view.layoutManager  = androidx.recyclerview.widget.LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            conversationAdapter = ConversationAdapter(it, conversationList, this)
            conversation_recycler_view.adapter = conversationAdapter
        }
    }

    private fun fetchConversation() {
        try {
            pActivity?.user?.email?.let {
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
                        conversationAdapter?.itemCount == 0 -> getConversations(query.documents)
                        query.documentChanges.last().type == DocumentChange.Type.ADDED && conversationAdapter?.itemCount!=query.size()
                        -> getAddedConversations(query.documentChanges.last())
                        query.documentChanges.last().type == DocumentChange.Type.REMOVED  && conversationAdapter?.itemCount!=query.size()
                        -> getConversations(query.documents)
                        query.documentChanges.last().type == DocumentChange.Type.MODIFIED  && conversationAdapter?.itemCount==query.size()
                        -> getUpdatedConversations(query.documentChanges.last())
                        else -> checkEmptyRecyclerView()
                    }
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    private fun getConversations(documents: MutableList<DocumentSnapshot>) {
        conversationAdapter?.swapAllData(ModelMappingUtility.mapOnChatRoomList(documents))
        checkEmptyRecyclerView()
    }

    private fun getAddedConversations(document: DocumentChange) {
        ModelMappingUtility.mapOnChatRoomModel(document.document)?.let {
            addItemInAdapter(it)
        }
        checkEmptyRecyclerView()
    }

    private fun getUpdatedConversations(document: DocumentChange) {
        ModelMappingUtility.mapOnChatRoomModel(document.document)?.let {
            updateAdapter(it)
        }
        checkEmptyRecyclerView()
    }

    private fun addItemInAdapter(chatRoom: ChatRooms) {
        conversationAdapter?.addItem(chatRoom)
    }

    private fun updateAdapter(chatRoom: ChatRooms) {
        conversationAdapter?.updateItem(chatRoom)
    }


    override fun onRefresh() {
        fetchConversation()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ConversationAdapter.ConversationFragmentListener) {
            interactionListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement ConversationFragmentListener")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConversationFragment().apply {
            arguments?.apply {

            }
        }
    }

}