package com.example.sarwan.renkar.modules.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import kotlinx.android.synthetic.main.dashboard_activity.*

class DashboardActivity : ParentActivity() {

    private lateinit var toInflate : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)
        getIntentData()
    }

    private fun getIntentData() {
        intent?.extras?.getString(ApplicationConstants.DASHBOARD_FRAGMENT_KEY)?.let {
            toInflate = it
            attachFragment()
            setFragmentTitle()
        }
    }

    private fun setFragmentTitle() {
        fragment_title.text = toInflate.toUpperCase()
    }

    private fun attachFragment() {
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, appropriateFragment()).commit()
    }

    private fun appropriateFragment(): Fragment {
        return map[toInflate]?.let { it }?:kotlin.run { SettingsFragment() }
    }

    val map = hashMapOf(getString(R.string.settings) to SettingsFragment(),
        getString(R.string.account) to AccountFragment(),
        getString(R.string.history) to HistoryFragment(),
        getString(R.string.payment_method) to PaymentMethodFragment(),
        getString(R.string.about) to AboutFragment())
}