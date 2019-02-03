package com.example.sarwan.renkar.modules.lister.add

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.AppBarStateChangeListener
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.add_car_step_three.*
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
class ListerAddCarFragment : ListerAddCarBaseFragment(){


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
        viewChangeListeners()
    }

    private fun viewChangeListeners(){
        address_view.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                (s?.length!! >= 3).let {
                    if (it)
                        queryForAddress(s.toString())
                }
            }
        })
    }

    private fun onClickListeners() {
        next.setOnClickListener {
            layoutTransitionOnNextButton()
        }

        previous.setOnClickListener {
            layoutTransitionOnPreviousButton()
        }

        appbar.addOnOffsetChangedListener(appBarChangeListener)

        address_view.onItemClickListener = autoCompleteItemClickListener
        address_view.setOnClickListener {
            if (autoCompleteModelList.isNotEmpty())
                (it as AutoCompleteTextView).showDropDown()
        }
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
