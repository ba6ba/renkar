package com.example.sarwan.renkar.modules.lister.add

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.AppBarStateChangeListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.add_car_step_three.*
import kotlinx.android.synthetic.main.lister_add_car_fragment.*
import kotlinx.android.synthetic.main.add_car_step_one.*
import kotlinx.android.synthetic.main.add_car_step_two.*


class ListerAddCarActivity : ListerAddCarBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lister_add_car_fragment)
        onClickListeners()
        viewChangeListeners()
    }

    private fun viewChangeListeners(){
        address_view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                (s?.length!! >= 3).let {
                    if (it)
                        queryForAddress(s.toString())
                }
            }
        })

        color_picker_view.setColorListener { colorEnvelope ->
            saveSelectedColor(colorEnvelope)
        }

        appbar.addOnOffsetChangedListener(appBarChangeListener)

        daily_price.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                showPriceEstimation()
            }
            true
        }

        daily_price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                (s?.length!! < 4).let {
                    if (it)
                        hidePriceEstimationLayouts()
                }
            }
        })
    }

    private fun onClickListeners() {
        next.setOnClickListener {
            layoutTransitionOnNextButton()
        }

        previous.setOnClickListener {
            layoutTransitionOnPreviousButton()
        }

        address_view.onItemClickListener = autoCompleteItemClickListener
        address_view.setOnClickListener {
            if (autoCompleteModelList.isNotEmpty())
                (it as AutoCompleteTextView).showDropDown()
        }

        instantBookDesc.setOnClickListener {
            showPopupMenu(it)
        }

        capacity_two?.setOnClickListener {
            makeCapacity(it)
            changeCapacityOtherViews(capacity_four, capacity_five)
        }

        capacity_four?.setOnClickListener {
            makeCapacity(it)
        }

        capacity_five?.setOnClickListener {
            makeCapacity(it)
        }
        chip_group.setOnCheckedChangeListener { group, checkedId ->
            getCheckedChip(group, checkedId)
        }
    }

    private fun getCheckedChip(group: ChipGroup?, checkedId: Int) {
        val selectedChip = (group?.findViewById<View>(checkedId) as Chip)
        selectedChip.chipBackgroundColor = resources.getColorStateList(R.color.colorAccent)
        car?.fuelType = (selectedChip).text.toString()
    }

    private fun changeCapacityOtherViews(vararg otherViews: TextView) = this.run {
        for (otherView in otherViews){
            otherView.setTextColor(resources.getColor(R.color.colorAccent))
            otherView.background =  (resources.getDrawable(R.drawable.circle_bg_with_accent_color))
        }
    }
    private fun makeCapacity(it: View) {
        it.background = resources.getDrawable(R.drawable.selected_circle_bg_with_accent_color)
        (it as TextView).setTextColor(resources.getColor(R.color.white))
        car?.capacity = it.text.toString()
    }


    private val appBarChangeListener = object : AppBarStateChangeListener(){
        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            when(state){
                AppBarStateChangeListener.State.COLLAPSED -> changeViewWithAnimation(View.VISIBLE)
                AppBarStateChangeListener.State.EXPANDED -> changeViewWithAnimation(View.GONE)
                AppBarStateChangeListener.State.IDLE -> changeViewWithAnimation(View.GONE)
            }
        }
    }

}