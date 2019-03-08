package com.example.sarwan.renkar.modules.features

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.model.Features
import kotlinx.android.synthetic.main.days_layout.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FeaturesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FeaturesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FeaturesFragment : Fragment(){

    private var adapter: FeaturesAdapter? = null
    var interactionListener : FeaturesInteractionListener ? = null
    private lateinit var featuresList : ArrayList<Features>
    private var switchLayout = false

    interface FeaturesInteractionListener{
        fun onSelect(selectedFeature: Features, flag: Action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            featuresList = getSerializable(FEATURES) as ArrayList<Features>
            switchLayout = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.features_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }


    private fun initializeViews() {
        when(switchLayout){
            true->{
                horizontalView()
            }
            false->{
                gridView()
            }
        }
        recyclerView.adapter = adapter
    }

    private fun gridView() {
        adapter = FeaturesAdapter(
            activity,
            FeaturesData.populateFeatures(activity?.parent?.let { it }
                ?: kotlin.run { activity as ParentActivity }),
            this,switchLayout
        )
        recyclerView.layoutManager = GridLayoutManager(activity,2)
    }

    private fun horizontalView() {
        adapter = FeaturesAdapter(
            activity,
            featuresList,
            this,switchLayout
        )
        recyclerView.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FeaturesInteractionListener) {
            interactionListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement FeaturesInteractionListener")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ListerDashboardFragment.
         */
        @JvmStatic
        fun newInstance(features: ArrayList<Features>) = FeaturesFragment().apply {
            arguments = Bundle().apply {
                putSerializable(FEATURES, features)
            }
        }
        val FEATURES = "FEATURES"
    }

    enum class Action {
        ADDED,
        REMOVED
    }
}
