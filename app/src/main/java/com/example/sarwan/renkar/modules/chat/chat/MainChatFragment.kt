package com.mobitribe.qulabro.modules.chat

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mobitribe.qulabro.R
import kotlinx.android.synthetic.main.fragment_chat.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainChatFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainChatFragment : androidx.fragment.app.Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabLayout()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment MainChatFragment.
         */
        @JvmStatic
        fun newInstance() = MainChatFragment()
    }

    private var chatNavigationPager: ChatNavigationPager? = null

    private fun setTabLayout() {
        chatNavigationPager = ChatNavigationPager(activity, childFragmentManager)
        chatViewPager.adapter = chatNavigationPager
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout.setOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(chatViewPager))
        chatViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        chatViewPager.offscreenPageLimit = 1
    }
}
