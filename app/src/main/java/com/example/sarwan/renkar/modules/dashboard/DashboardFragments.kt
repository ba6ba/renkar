package com.example.sarwan.renkar.modules.dashboard

import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.modules.about.AboutFragment
import com.example.sarwan.renkar.modules.account.AccountFragment
import com.example.sarwan.renkar.modules.history.HistoryFragment
import com.example.sarwan.renkar.modules.payment_method.PaymentMethodFragment
import com.example.sarwan.renkar.modules.settings.SettingsFragment

object DashboardFragments {
    fun get(activity: DashboardActivity): HashMap<String, Fragment>{
        return hashMapOf(activity.getString(R.string.settings) to SettingsFragment(),
            activity.getString(R.string.account) to AccountFragment(),
            activity.getString(R.string.history) to HistoryFragment(),
            activity.getString(R.string.payment_method) to PaymentMethodFragment(),
            activity.getString(R.string.about) to AboutFragment()
        )
    }
}