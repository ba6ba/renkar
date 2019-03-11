package com.example.sarwan.renkar.modules.history

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.firebase.FirebaseExtras
import com.example.sarwan.renkar.fragments.ConfirmationFragment
import com.example.sarwan.renkar.model.History
import com.example.sarwan.renkar.modules.booking.BookingActivity
import com.example.sarwan.renkar.modules.chat.messaging.ChattingBaseFragment
import com.example.sarwan.renkar.modules.dashboard.DashboardActivity
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.history_fragment.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HistoryFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HistoryFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    HistoryAdapter.HistoryAdapterCallBack, BookingDialogFragment.BookingDialogFragmentCallBack
{

    private var pActivity : ParentActivity? = null
    private var adapter: HistoryAdapter? = null
    private var historyList : ArrayList<History> = ArrayList()
    private var bookingDialogFragment : BookingDialogFragment ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as DashboardActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRefreshLayouts()
        initializeLayoutView()
        fetchHistory()
    }

    override fun onRefresh() {
        fetchHistory()
    }

    private fun checkEmptyRecyclerView() {
        if (adapter?.itemCount == 0) {
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
            history_recycler_view.layoutManager  = androidx.recyclerview.widget.LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = HistoryAdapter(it, historyList, this)
            history_recycler_view.adapter = adapter
        }
    }

    private fun fetchHistory() {
        try {
            pActivity?.user?.apply {
                email?.let {email->
                    type?.let { type->
                        HistoryHelper.get(email,type).addSnapshotListener(queryListener)
                    }
                }
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
                    getHistory(query.toObjects(History::class.java))
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    private fun getHistory(history: MutableList<History>) {
        historyList = history  as java.util.ArrayList<History>
        adapter?.swap(historyList)
        checkEmptyRecyclerView()
    }

    override fun onHistoryItemClick(history: History) {
        checkForStatusType(history)
    }

    private var selectedHistory : History ? = null

    private fun checkForStatusType(history: History) {
        selectedHistory = history
        pActivity?.let {
            when(history.status){
                History.STATUS.REQUEST_APPROVED.name->{
                    if (it.user?.type==ApplicationConstants.LISTER) it.showMessage("Your car ${history.car_number} is on Booking from ${history.period}")
                    else attachDialogFragment(history.status)
                }
                History.STATUS.REQUEST_DECLINED.name->{
                    if (it.user?.type==ApplicationConstants.LISTER) attachDialogFragment(history.status)
                    else it.showMessage("Your request to book car ${history.car_number} is declined, try other car")
                }
                History.STATUS.REQUEST_PENDING.name->{
                    if (it.user?.type==ApplicationConstants.LISTER) attachDialogFragment(history.status)
                    else it.showMessage("Your request to book car ${history.car_number} is on pending, chat with ${history.listedBy} to confirm booking")
                }
                History.STATUS.ON_BOOKING.name->{
                    if (it.user?.type==ApplicationConstants.LISTER) it.showMessage("Your car ${history.car_number} is on Booking from ${history.period}")
                    else it.showMessage("This car ${history.car_number} is booked by ${history.rentedBy} from ${history.period}")
                }
            }
        }
    }



    override fun onBookingDialogClick(status: String?) {
        selectedHistory?.status = History.STATUS.REQUEST_APPROVED.name
        when(status){
            History.STATUS.REQUEST_APPROVED.name->{
                openBookingActivity()
            }
            History.STATUS.REQUEST_DECLINED.name->{
                changeStatusOfBooking()
            }
            History.STATUS.REQUEST_PENDING.name->{
                changeStatusOfBooking()
            }
        }
    }

    private fun changeStatusOfBooking() {
        pActivity?.apply {
            showProgress()
            selectedHistory?.let { HistoryHelper.create(it) }
            user?.let {
                ChattingBaseFragment().sendConfirmationMessageToFirebase(ConfirmationFragment.Companion.ConfirmationType.DONE.name,
                    ConfirmationFragment.Companion.ConfirmationType.DONE.ordinal, ConfirmationFragment.Companion.ConfirmationOption.LISTER_BOOK.ordinal,
                    it.name?:"", it.email?:"")
            }
            Handler().postDelayed({callBackHandling()},3000L)
        }
    }

    private fun openBookingActivity() {
        pActivity?.openActivity(
            Intent(activity, BookingActivity::class.java).
                putExtra(ApplicationConstants.BOOKING_OBJECT,selectedHistory))
    }


    private fun callBackHandling() {
        pActivity?.hideProgress()
        pActivity?.showMessage("Your response has been recorded, Thank you!")
    }

    private fun attachDialogFragment(status: String?) {
        status?.let {
            BookingDialogFragment.newInstance(status).run {
                bookingDialogFragment = this
                bookingDialogFragment?.initListener(this@HistoryFragment)
                if (!isAdded)
                    show(createManager(status), status)
            }
        }
    }


    private fun createManager(status: String): FragmentManager {
        childFragmentManager.beginTransaction().run {
            run {
                if ((childFragmentManager.findFragmentByTag(status) != null))
                    childFragmentManager.findFragmentByTag(status)?.let { this.remove(it) }
                addToBackStack(null)
            }
        }
        return childFragmentManager
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AboutFragment.
         */
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}
