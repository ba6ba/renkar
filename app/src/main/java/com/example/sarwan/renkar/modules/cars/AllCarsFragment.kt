package com.example.sarwan.renkar.modules.cars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.modules.details.CarDetailsActivity
import kotlinx.android.synthetic.main.related_cars_layout.*

class AllCarsFragment : Fragment() {

    private var carsList : ArrayList<Cars>  = ArrayList()
    private lateinit var pActivity: CarDetailsActivity
    private lateinit var adapter : AllCarsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as CarDetailsActivity
        arguments?.let {
            carsList = it.getSerializable(ApplicationConstants.ALL_CARS) as ArrayList<Cars>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.related_cars_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeLayoutView()
    }

    private fun initializeLayoutView() {
        pActivity.let {
            all_cars_rc_view.layoutManager  = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = AllCarsAdapter(it, filterCars())
            all_cars_rc_view.adapter = adapter
        }
    }

    private fun filterCars(): ArrayList<Cars> {
        return carsList.filter { it.owner.email!=pActivity.user?.email } as ArrayList<Cars>
    }

    companion object {
        @JvmStatic
        fun newInstance(cars: ArrayList<Cars>?) = AllCarsFragment().apply {
            arguments= Bundle().apply {
                cars?.let {
                    putSerializable(ApplicationConstants.ALL_CARS, it)
                }
            }
        }
    }
}