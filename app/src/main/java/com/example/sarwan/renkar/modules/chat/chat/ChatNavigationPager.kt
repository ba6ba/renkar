package com.mobitribe.qulabro.modules.chat

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.mobitribe.qulabro.modules.contact.ContactFragment
import com.mobitribe.qulabro.modules.chat.conversation.ConversationFragment

class ChatNavigationPager(val context: Context?, val fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {


        private val NUM_ITEMS = 2

        override fun getCount(): Int {
            return NUM_ITEMS
        }

        // Returns the fragment to display for that page

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> ConversationFragment.newInstance()
                1 -> ContactFragment.newInstance()
                else -> ContactFragment.newInstance()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return null
        }
}