package com.example.sarwan.renkar.modules.lister.add

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.AppBarStateChangeListener
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.google.android.material.appbar.AppBarLayout
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
        initializeListeners()
    }

    private fun viewChangeListeners(){
        address_view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.length?.let {
                    if (it>=3)
                        queryForAddress(s.toString())
                }
            }
        })

        color_picker_view.setColorListener { colorEnvelope ->
            saveSelectedColor(colorEnvelope)
        }

        //appbar.addOnOffsetChangedListener(appBarChangeListener)

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
                s?.let {
                    if (it.length < 4)
                        hidePriceEstimationLayouts()
                    else if (it.length == 4)
                        showPriceEstimation()
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

        two?.setOnClickListener {
            makeCapacity(it)
            changeOtherView(four, five)
        }

        four?.setOnClickListener {
            makeCapacity(it)
            changeOtherView(two, five)
        }

        five?.setOnClickListener {
            makeCapacity(it)
            changeOtherView(four, two)
        }

        gas?.setOnClickListener {
            makeFuelType(gasImage, it)
            changeOtherView(petrolImage, dieselImage)
        }

        petrolImage?.setOnClickListener {
            makeFuelType(petrolImage, it)
            changeOtherView(gasImage, dieselImage)
        }

        dieselImage?.setOnClickListener {
            makeFuelType(dieselImage, it)
            changeOtherView(gasImage, petrolImage)
        }

        carImage.setOnClickListener {
            showOptionsForImage()
        }
    }

    private fun makeFuelType(imageView: ImageView, it: View) {
        imageView.background = resources.getDrawable(R.drawable.ic_check_selected)
        car?.fuelType = (it as TextView).text.toString()
    }

    private fun changeOtherView(vararg otherViews: View) = this.run {
        for (otherView in otherViews){
            if(otherView is LinearLayout)
                otherView.background =  (resources.getDrawable(R.drawable.capacity_bg))
            else if (otherView is ImageView)
                otherView.background =  (resources.getDrawable(R.drawable.ic_check))
        }
    }
    private fun makeCapacity(it: View) {
        it.background = resources.getDrawable(R.drawable.capacity_bg_selected)
        car?.capacity = (it as TextView).text.toString()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let { it ->
            if (resultCode == RESULT_OK) {
                if (requestCode == ApplicationConstants.WRITE_EXTERNAL_STORAGE_CODE) {
                    it.data?.let { data->
                        selectedImage = imageUpload?.makeImageURLWithExternalStorage(data)?.let { it }?:kotlin.run { null }
                    }
                }
                else if (requestCode == ApplicationConstants.CAPTURE_IMAGE_STORAGE_CODE) {
                    setImageToLayout(it)
                }
            }
        }
    }
}