package com.example.sarwan.renkar.modules.lister.add

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.AppBarStateChangeListener
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.extras.CustomTextWatcher
import com.example.sarwan.renkar.modules.features.FeaturesFragment
import com.example.sarwan.renkar.utils.ValidationUtility
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.lister_add_car_fragment.*
import kotlinx.android.synthetic.main.add_car_step_one.*
import kotlinx.android.synthetic.main.add_car_step_two.*


class ListerAddCarActivity : ListerAddCarBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lister_add_car_fragment)
        initFragment()
        onClickListeners()
        viewChangeListeners()
        initializeClasses()
        initializeListeners()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction().add(R.id._features_fragment, FeaturesFragment()).commit()
    }

    private fun viewChangeListeners(){
        address_text_view.run {
            tag = id.toString()
            addTextChangedListener(object : CustomTextWatcher(tag,this@ListerAddCarActivity){})
        }

        daily_price.apply {
            tag = id.toString()
            addTextChangedListener(object : CustomTextWatcher(tag , this@ListerAddCarActivity){})
        }

        model.apply {
            tag = id.toString()
            addTextChangedListener(object : CustomTextWatcher(tag , this@ListerAddCarActivity){})
        }

        color_picker_view.setColorListener { colorEnvelope ->
            saveSelectedColor(colorEnvelope)
        }

        appbar.addOnOffsetChangedListener(appBarChangeListener)

        daily_price.setOnEditorActionListener { v, actionId, event ->
            onEditorActionListener(v, actionId, event , true)
        }

        number.setOnEditorActionListener { v, actionId, event ->
            onEditorActionListener(v, actionId, event , false)
        }
    }


    private fun onClickListeners() {
        next.setOnClickListener {
            layoutTransitionOnNextButton()
        }

        previous.setOnClickListener {
            layoutTransitionOnPreviousButton()
        }

        address_text_view.onItemClickListener = autoCompleteItemClickListener

        address_text_view.setOnClickListener {
            if (autoCompleteModelList.isNotEmpty())
                (it as AutoCompleteTextView).showDropDown()
        }

        instantBookDesc.setOnClickListener {
            showPopupMenu(it,getString(R.string.instant_book_desc))
        }

        deliveryDesc.setOnClickListener {
            showPopupMenu(it,getString(R.string.delivery_desc))
        }

        two?.setOnClickListener {
            makeCapacity(it, getString(R.string.two))
            changeBackgrounds(four, five)
        }

        four?.setOnClickListener {
            makeCapacity(it, getString(R.string.four))
            changeBackgrounds(two, five)
        }

        five?.setOnClickListener {
            makeCapacity(it, getString(R.string.five))
            changeBackgrounds(four, two)
        }

        gas?.setOnClickListener {
            makeFuelType(gasImage, gasText)
            changeBackgrounds(petrolImage, dieselImage)
            changeTextColors(dieselText, petrolText)
        }

        petrolImage?.setOnClickListener {
            makeFuelType(petrolImage, petrolText)
            changeBackgrounds(gasImage, dieselImage)
            changeTextColors(gasText, dieselText)
        }

        dieselImage?.setOnClickListener {
            makeFuelType(dieselImage, dieselText)
            changeBackgrounds(gasImage, petrolImage)
            changeTextColors(gasText, petrolText)
        }

        carImage.setOnClickListener {
            showOptionsForImage()
        }
    }

    private fun makeFuelType(imageView: ImageView, textView: TextView) {
        imageView.background = resources.getDrawable(R.drawable.ic_check_selected)
        textView.setTextColor(resources.getColor(R.color.colorAccent))
        makeCarFuel(textView.text.toString())
    }

    private fun changeTextColors(vararg textViews: TextView) = this.run {
        for (textView in textViews){
            textView.setTextColor(resources.getColor(R.color.dark_grey))
        }
    }

    private fun changeBackgrounds(vararg otherViews: View) = this.run {
        for (otherView in otherViews){
            if(otherView is LinearLayout)
                otherView.background =  (resources.getDrawable(R.drawable.capacity_bg))
            else if (otherView is ImageView)
                otherView.background =  (resources.getDrawable(R.drawable.ic_check))
        }
    }

    private fun makeCapacity(it: View , value: String) {
        it.background = resources.getDrawable(R.drawable.capacity_bg_selected)
        makeCarCapacity(value)
    }

    private val appBarChangeListener = object : AppBarStateChangeListener(){
        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            when(state){
                AppBarStateChangeListener.State.COLLAPSED -> changeViewWithAnimation(View.VISIBLE)
                AppBarStateChangeListener.State.EXPANDED -> changeViewWithAnimation(View.INVISIBLE)
                AppBarStateChangeListener.State.IDLE -> changeViewWithAnimation(View.INVISIBLE)
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

    override fun onBackPressed() {
        if (onStep==2){
            onStep-=1
            layoutTransitionOnPreviousButton()
        }
        else
            super.onBackPressed()
    }
}