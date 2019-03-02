package com.example.sarwan.renkar.modules.lister.listing

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
import com.example.sarwan.renkar.modules.lister.ListerActivity
import com.example.sarwan.renkar.utils.StringUtility
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.lister_cars_fragment.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContactFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ListerCarsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    private var listerCarsAdapter: ListerCarsAdapter? = null
    private var pActivity : ParentActivity? = null
    private var carsList : ArrayList<Cars> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ListerActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.lister_cars_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRefreshLayouts()
        userIconInitial()
        initializeLayoutView()
        fetchCars()
    }

    private fun userIconInitial() {
        person_icon.text = pActivity?.user?.name
    }

    override fun onRefresh() {
        fetchCars()
    }

    private fun checkEmptyRecyclerView() {
        if (listerCarsAdapter?.itemCount == 0) {
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
            lister_cars_recycler_view.layoutManager  = androidx.recyclerview.widget.LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            listerCarsAdapter = ListerCarsAdapter(it, carsList)
            lister_cars_recycler_view.adapter = listerCarsAdapter
        }
    }

    private fun fetchCars() {
        try {
            pActivity?.user?.email?.let {
                FirestoreQueryCenter.getListerCars(it).addSnapshotListener(queryListener)
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
                        listerCarsAdapter?.itemCount!! == 0 -> getCars(query.toObjects(Cars::class.java))
                        query.documentChanges.last().type == DocumentChange.Type.ADDED -> getNewCar(query.toObjects(Cars::class.java))
                        //query.documentChanges.last().type == DocumentChange.Type.REMOVED -> getCars(query.toObjects(Cars::class.java))
                        query.documentChanges.last().type == DocumentChange.Type.MODIFIED -> getUpdatedCar(query.toObjects(Cars::class.java))
                    }
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    private fun getCars(cars: MutableList<Cars>) {
        carsList = cars  as java.util.ArrayList<Cars>
        listerCarsAdapter?.swap(carsList)
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
            listerCarsAdapter?.addItem(car)
        }
    }

    private fun updateAdapter(car: Cars, key: String?) {
            listerCarsAdapter?.updateItem(car, key)
    }

    fun removeListener(){
        pActivity?.user?.email?.let {
            FirestoreQueryCenter.getListerCars(it).addSnapshotListener(queryListener).remove()
        }
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ListerProfileFragment.
         */
        @JvmStatic
        fun newInstance() = ListerCarsFragment()
    }
}
