package com.example.sarwan.renkar.modules.renter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import kotlinx.android.synthetic.main.custom_tab_layout.*

class CustomTabLayoutFragment : Fragment() {

    private lateinit var pActivity: RenterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as RenterActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.custom_tab_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListener()
    }

    private fun onClickListener() {
        popular.setOnClickListener {
            setChildFragment(it as TextView)
        }

        nearby_you.setOnClickListener {
            setChildFragment(it as TextView)
        }
    }

    private fun setChildFragment(textView: TextView) {
        RenterCarsFragment.newInsance(textView.text.toString().toLowerCase()).apply {
            attachFragment(this)
        }
        changeTextViewsAppearance(textView, popular, nearby_you)
    }

    private fun changeTextViewsAppearance(currentTextView: TextView, vararg textViews: TextView?) {
        for (textView in textViews){
            textView?.apply {
                setTextColor(pActivity.resources.getColor(R.color.light_grey))
                textSize = pActivity.resources.getDimension(R.dimen.medium_text_size)
            }
        }
        currentTextView.apply {
            setTextColor(pActivity.resources.getColor(R.color.black))
            textSize = pActivity.resources.getDimension(R.dimen.large_text_size)
        }
    }

    private fun attachFragment(renterCarsFragment : Fragment) {
        fragmentManager?.apply {
            beginTransaction().replace(R.id.fragment_container_tabs, renterCarsFragment).
                setCustomAnimations(R.anim.popup_enter, R.anim.pop_up_exit).
                addToBackStack(null).commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInsance() = CustomTabLayoutFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}