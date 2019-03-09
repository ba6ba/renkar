package com.example.sarwan.renkar.modules.renter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.fragments.ProfileDialogFragment
import com.example.sarwan.renkar.model.User
import com.example.sarwan.renkar.modules.chat.messaging.MessagesActivity

import kotlinx.android.synthetic.main.top_bar_fragment.*

class TopBarFragment : Fragment(), ProfileDialogFragment.ProfileDialogFragmentListener {

    private lateinit var pActivity: RenterActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as RenterActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.top_bar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun init(text : String){
        renter_top_bar.visibility = if (text == User.TYPE.RENTER.name) View.VISIBLE else View.GONE
        lister_top_bar.visibility = if (text == User.TYPE.LISTER.name) View.VISIBLE else View.GONE
    }

    override fun onClick() {
        pActivity.openActivity(Intent(pActivity, MessagesActivity::class.java))
    }

    companion object {
        @JvmStatic
        fun newInsance() = RenterCarsFragment().apply {
            arguments = Bundle().apply {
            }
        }

    }
}