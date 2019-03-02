package com.example.sarwan.renkar.modules.days

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.model.Days
import kotlinx.android.synthetic.main.days_layout.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContactFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DayFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DayFragment : Fragment() {

    private var adapter: DaysAdapter? = null
    var interactionListener : DayFragment.DaysInteractionListener? = null

    interface DaysInteractionListener{
        fun onSelect(selectedDay : String?, flag : DayFragment.Action)
    }

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
        adapter = DaysAdapter(activity, DaysData.populateDays(), this)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    fun initFeatures(days : ArrayList<Days>){
        adapter?.swap(days)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DaysInteractionListener) {
            interactionListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement DaysInteractionListener")
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
        fun newInstance() = DayFragment()
    }

    enum class Action {
        ADDED,
        REMOVED
    }
}
