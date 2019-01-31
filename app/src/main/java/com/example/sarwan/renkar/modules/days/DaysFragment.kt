package com.example.sarwan.renkar.modules.days

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.model.Days
import com.example.sarwan.renkar.modules.features.FeaturesData
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
class DaysFragment : Fragment(), DaysAdapter.DaysSelected{

    private var layoutManager : androidx.recyclerview.widget.LinearLayoutManager? = null
    private var adapter: DaysAdapter? = null
    private var selectedDays : ArrayList<String> ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.days_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    private fun initializeViews() {
        layoutManager  = androidx.recyclerview.widget.LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        adapter = DaysAdapter(activity, FeaturesData.populateDays(),this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun initDays(days : ArrayList<Days>){
        adapter?.swap(days)
    }


    override fun onSelect(selectedDay: String?, flag: DaysFragment.Action) {
        when(flag){
            Action.ADDED-> selectedDay?.let { selectedDays?.add(it) }
            Action.REMOVED-> selectedDay?.let { selectedDays?.remove(it) }
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
        fun newInstance() = DaysFragment()
    }

    enum class Action {
        ADDED,
        REMOVED
    }
}
