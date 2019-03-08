package com.example.sarwan.renkar.modules.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.modules.about.AboutFragment
import com.example.sarwan.renkar.modules.account.AccountFragment
import com.example.sarwan.renkar.modules.history.HistoryFragment
import com.example.sarwan.renkar.modules.payment_method.PaymentMethodFragment
import com.example.sarwan.renkar.modules.settings.SettingsFragment
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
        return DashboardFragments.get(this)[toInflate]?.let { it }?:kotlin.run { SettingsFragment() }
    }
}