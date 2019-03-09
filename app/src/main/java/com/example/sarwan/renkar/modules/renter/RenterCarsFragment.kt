package com.example.sarwan.renkar.modules.renter

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
import com.example.sarwan.renkar.model.User
import com.example.sarwan.renkar.modules.lister.listing.ListerCarsAdapter
import com.example.sarwan.renkar.utils.LocationUtility
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.renter_cars_fragment.*

class RenterCarsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var fragmentType : String
    private var renterCarsAdapter: ListerCarsAdapter? = null
    private var pActivity : ParentActivity? = null
    private var carsList : ArrayList<Cars> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentType = it.getString(TYPE, POPULAR)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.renter_cars_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRefreshLayouts()
        initializeLayoutView()
        fetchCars()
    }

    override fun onRefresh() {
        fetchCars()
    }

    private fun checkEmptyRecyclerView() {
        if (renterCarsAdapter?.itemCount == 0) {
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
            renter_cars_recycler_view.layoutManager  = androidx.recyclerview.widget.LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            renterCarsAdapter = ListerCarsAdapter(it, carsList, User.TYPE.RENTER.ordinal)
            renter_cars_recycler_view.adapter = renterCarsAdapter
        }
    }

    private fun fetchCars() {
        try {
            fireStoreListener(false)
        }catch (e: Exception){
            e.localizedMessage
        }
    }

    private fun fireStoreListener(remove: Boolean) {
        pActivity?.user?.apply {
            latitude?.let { latitude->
                longitude?.let {longitude->
                    FirestoreQueryCenter.getNearestCars(LocationUtility.getNearest(latitude,longitude)?:LocationUtility.CENTRAL)
                        .addSnapshotListener(queryListener).apply {
                            if (remove)
                                remove()
                        }
                }
            }
        }
    }

    private val queryListener = EventListener<QuerySnapshot> { querySnapshot, fireStoreException ->
        try {
            fireStoreException?.let {
                return@EventListener
            }?:kotlin.run {
                querySnapshot?.let {query->
                    when {
                        renterCarsAdapter?.itemCount!! == 0 -> getCars(query.toObjects(Cars::class.java))
                        query.documentChanges.last().type == DocumentChange.Type.ADDED -> getNewCar(query.toObjects(Cars::class.java))
                        //query.documentChanges.last().type == DocumentChange.Type.REMOVED -> getCars(query.toObjects(Cars::class.java))
                        query.documentChanges.last().type == DocumentChange.Type.MODIFIED -> getUpdatedCar(query.toObjects(
                            Cars::class.java))
                    }
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    private fun getCars(cars: MutableList<Cars>) {
        carsList = cars  as java.util.ArrayList<Cars>
        renterCarsAdapter?.swap(carsList)
        checkEmptyRecyclerView()
    }

    private fun getNewCar(cars:  MutableList<Cars>) {
        addItemInAdapter(cars.last())
        checkEmptyRecyclerView()
    }

    private fun getUpdatedCar(cars:  MutableList<Cars>) {
        updateAdapter(cars.last(), cars.last().number)
        checkEmptyRecyclerView()
    }

    private fun addItemInAdapter(car: Cars) {
        if (carsList.map { car.number }.isEmpty()){
            carsList.add(car)
            renterCarsAdapter?.addItem(car)
        }
    }

    private fun updateAdapter(car: Cars, key: String?) {
        renterCarsAdapter?.updateItem(car, key)
    }

    private fun removeListener(){
        fireStoreListener(true)
    }

    override fun onDestroy() {
        removeListener()
        super.onDestroy()
    }

    override fun onDetach() {
        removeListener()
        super.onDetach()
    }


    companion object {
        @JvmStatic
        fun newInsance(type: String) = RenterCarsFragment().apply {
            arguments = Bundle().apply {
                putString(TYPE, type)
            }
        }

        const val TYPE = "TYPE"
        const val POPULAR = "popular"
        const val NEARBY_YOU = "nearby you"
    }
}