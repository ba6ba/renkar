package com.example.sarwan.renkar.modules.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.model.Days
import com.example.sarwan.renkar.model.Features
import com.example.sarwan.renkar.modules.lister.ListerAddCarFragment
import kotlinx.android.synthetic.main.days_layout.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContactFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FeaturesFragment : Fragment(), FeaturesAdapter.FeaturesSelected {

    private var adapter: FeaturesAdapter? = null
    private var selectedFeatures : ArrayList<String> ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        adapter = FeaturesAdapter(activity, FeaturesData.populateFeatures(activity?.parent?.let { it }?:kotlin.run { activity as ParentActivity }),this)
        recyclerView.layoutManager = GridLayoutManager(activity,2)
        recyclerView.adapter = adapter
    }

    fun initFeatures(features : ArrayList<Features>){
        adapter?.swap(features)
    }


    override fun onSelect(selectedFeature: String?, flag: Action) {
        when(flag){
            Action.ADDED -> selectedFeature?.let { selectedFeatures?.add(it) }
            Action.REMOVED -> selectedFeature?.let { selectedFeatures?.remove(it) }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ListerProfileFragment.
         */
        @JvmStatic
        fun newInstance() = FeaturesFragment()
    }

    enum class Action {
        ADDED,
        REMOVED
    }
}
