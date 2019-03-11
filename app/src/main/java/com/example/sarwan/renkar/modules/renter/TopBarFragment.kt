package com.example.sarwan.renkar.modules.renter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.fragments.ProfileDialogFragment
import com.example.sarwan.renkar.model.User
import com.example.sarwan.renkar.modules.chat.messaging.MessagesActivity

import kotlinx.android.synthetic.main.top_bar_fragment.*

class TopBarFragment : Fragment(), ProfileDialogFragment.ProfileDialogFragmentListener {

    private lateinit var pActivity: ParentActivity
    private var type : String ? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
        arguments?.let {
            type = it.getString(TYPE, User.TYPE.LISTER.name)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.top_bar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type?.let { init(it) }
        onClickListener()
    }

    private fun updateScreen() {
        user_text_view.text = pActivity.user?.name
    }

    private fun onClickListener() {
        user_text_view.setOnClickListener {
            openProfileFragment()
        }

        user_image_view.setOnClickListener {
            openProfileFragment()
        }
    }

    private fun openProfileFragment() {
        ProfileDialogFragment().init(this).show(childFragmentManager, ProfileDialogFragment::class.java.simpleName)
    }

    fun init(text : String){
        renter_top_bar.visibility = if (text == User.TYPE.RENTER.name) View.VISIBLE else View.GONE
        lister_top_bar.visibility = if (text == User.TYPE.LISTER.name) View.VISIBLE else View.GONE
        if (text == User.TYPE.LISTER.name) updateScreen()
    }

    override fun onClick() {
        pActivity.openActivity(Intent(pActivity, MessagesActivity::class.java))
    }

    companion object {
        @JvmStatic
        fun newInstance(type : String) = TopBarFragment().apply {
            arguments = Bundle().apply {
                putString(TYPE, type)
            }
        }
        val TYPE = "TYPE"
    }
}