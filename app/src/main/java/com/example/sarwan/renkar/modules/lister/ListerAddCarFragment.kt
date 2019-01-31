package com.example.sarwan.renkar.modules.lister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.AppBarStateChangeListener
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.lister_add_car_fragment.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContactFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ListerAddCarFragment : Fragment(){

    private var onStep : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.lister_add_car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
    }

    private fun onClickListeners() {
        next.setOnClickListener {
            layoutTransitionOnNextButton()
        }

        previous.setOnClickListener {
            layoutTransitionOnPreviousButton()
        }

        appbar.addOnOffsetChangedListener(appBarChangeListener)
    }

    private val appBarChangeListener = object : AppBarStateChangeListener(){
        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            when(state){
                AppBarStateChangeListener.State.COLLAPSED -> addCarBar.visibility = View.VISIBLE
                AppBarStateChangeListener.State.EXPANDED -> addCarBar.visibility = View.GONE
                AppBarStateChangeListener.State.IDLE -> addCarBar.visibility = View.GONE
            }
        }
    }

    private fun layoutTransitionOnNextButton() {
        when(onStep){
            1-> {
                step_one.visibility = View.GONE
                step_two.visibility = View.VISIBLE
                previous.visibility = View.VISIBLE
                onStep+=1
            }
            2-> {
                step_two.visibility = View.GONE
                step_three.visibility = View.VISIBLE
                next.setImageResource(R.drawable.ic_check_black_24dp)
                onStep+=1
            }
            3-> {
                showSummaryDialog()
            }
        }
    }


    private fun layoutTransitionOnPreviousButton() {
        when(onStep){
            2-> {
                step_two.visibility = View.GONE
                step_one.visibility = View.VISIBLE
                previous.visibility = View.GONE
                onStep=-1
            }
            3-> {
                step_two.visibility = View.VISIBLE
                step_three.visibility = View.GONE
                next.setImageResource(R.drawable.next)
                onStep-=1
            }
        }
    }

    private fun showSummaryDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ListerProfileFragment.
         */
        @JvmStatic
        fun newInstance() = ListerAddCarFragment()
    }
}
