package com.example.sarwan.renkar.modules.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.modules.dashboard.DashboardActivity
import com.example.sarwan.renkar.utils.Cities
import kotlinx.android.synthetic.main.account_fragment.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AccountFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AccountFragment : Fragment(){
    
    private lateinit var pActivity : ParentActivity
    private var cities = Cities.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as DashboardActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.account_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFields()
        setupSpinners()
    }

    private fun setFields() {
        first_name.setText(pActivity.user?.first_name)
        last_name.setText(pActivity.user?.last_name)
        phone_no.setText(pActivity.user?.first_name)
        d_o_b.setText(pActivity.user?.first_name)
        address.setText(pActivity.user?.address)
        email.setText(pActivity.user?.email)
    }


    private fun setupSpinners(){
        val adapter = ArrayAdapter<String>(pActivity, R.layout.spinner_item, R.id.spinnerText, cities)
        city.adapter = adapter
        city.setSelection(0)
        city.onItemSelectedListener = onSpinnerItemSelectedListener
    }

    private val onSpinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            city.setSelection(position)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AboutFragment.
         */
        @JvmStatic
        fun newInstance() = AccountFragment()
    }
}
