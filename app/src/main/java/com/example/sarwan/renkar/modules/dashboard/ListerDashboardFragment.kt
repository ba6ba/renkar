package com.example.sarwan.renkar.modules.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.modules.lister.ListerActivity
import kotlinx.android.synthetic.main.lister_profile_fragment.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContactFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ListerDashboardFragment : Fragment(){

    private var clickNo = 0
    private lateinit var pActivity: ListerActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ListerActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.lister_profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListener()
    }

    private fun onClickListener() {
        settings.setOnClickListener {
            checkForClickAction(it as TextView)
        }

        account.setOnClickListener {
            checkForClickAction(it as TextView)
        }

        payment_method.setOnClickListener {
            checkForClickAction(it as TextView)
        }

        about.setOnClickListener {
            checkForClickAction(it as TextView)
        }

        history.setOnClickListener {
            checkForClickAction(it as TextView)
        }
    }

    private fun checkForClickAction(textView: TextView) {
        unSelectLayouts(settings, account, about, payment_method, history)
        selectLayout(textView)
    }


    private fun openActivity(textView: TextView) {
        pActivity.openActivity(Intent(pActivity, DashboardActivity::class.java).
            putExtra(ApplicationConstants.DASHBOARD_FRAGMENT_KEY, textView.text))
    }

    private fun selectLayout(textView: TextView) {
        textView.backgroundTintList = pActivity.resources.getColorStateList(R.color.colorAccent)
        textView.setTextColor(pActivity.resources.getColor(R.color.white))
        openActivity(textView)
    }

    private fun unSelectLayouts(vararg textViews: TextView) {
        for(textView in textViews){
            textView.backgroundTintList = pActivity.resources.getColorStateList(R.color.white)
            textView.setTextColor(pActivity.resources.getColor(R.color.grey))
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
        fun newInstance() = ListerDashboardFragment()
    }
}
