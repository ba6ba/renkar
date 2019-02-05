package com.example.sarwan.renkar.modules.lister.add

import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.CustomListAdapter
import com.example.sarwan.renkar.model.AutoCompleteModel
import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.model.Features
import com.example.sarwan.renkar.model.MAPBOX.Addresses
import com.example.sarwan.renkar.model.MAPBOX.Feature
import com.example.sarwan.renkar.model.TPL.Locations
import com.example.sarwan.renkar.modules.days.DayFragment
import com.example.sarwan.renkar.modules.features.FeaturesFragment
import com.example.sarwan.renkar.network.NetworkConstants
import com.example.sarwan.renkar.network.RestClient
import com.example.sarwan.renkar.utils.DateTimeUtility
import com.example.sarwan.renkar.utils.PriceUtility
import com.example.sarwan.renkar.utils.ValidationUtility
import com.google.android.material.chip.Chip
import com.skydoves.colorpickerpreference.ColorEnvelope
import kotlinx.android.synthetic.main.add_car_step_one.*
import kotlinx.android.synthetic.main.add_car_step_three.*
import kotlinx.android.synthetic.main.add_car_step_two.*
import kotlinx.android.synthetic.main.lister_add_car_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContactFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
open class ListerAddCarBaseActivity : ParentActivity(), FeaturesFragment.FeaturesInteractionListener , DayFragment.DaysInteractionListener{

    private var onStep : Int = 1
    private var addressCount = 2
    private var addressList: ArrayList<com.example.sarwan.renkar.model.location.Address?> = ArrayList()
    private var mapBoxAddresses: Addresses ? = null
    protected var adapter: CustomListAdapter? = null
    protected var autoCompleteModelList : ArrayList<AutoCompleteModel> = ArrayList()
    private var selectedFeatures : ArrayList<Features> ? = ArrayList()
    private var selectedDays : ArrayList<String> ? = ArrayList()

    protected var car : Cars? = null

    protected fun layoutTransitionOnNextButton() {
        when(onStep){
            1-> {
                goToSecondStep()
            }
            2-> {
                goToThirdStep()
            }
            3-> {
                showSummaryDialog()
            }
        }
    }

    private fun goToThirdStep() {
        if(validateStepTwo()){
            makeStepTwoFields()
            step_two.visibility = View.GONE
            step_three.visibility = View.VISIBLE
            next.setImageResource(R.drawable.ic_check_black_24dp)
            onStep+=1
        }else{
            showMessage(getString(R.string.must_not_be_empty))
        }
    }

    private fun goToSecondStep() {
        if (validateStepOne()){
            makeStepOneFields()
            setupSpinners()
            step_one.visibility = View.GONE
            step_two.visibility = View.VISIBLE
            previous.visibility = View.VISIBLE
            onStep+=1
        }else {
            showMessage(getString(R.string.must_not_be_empty))
        }
    }

    protected fun layoutTransitionOnPreviousButton() {
        when(onStep){
            2-> {
                step_two.visibility = View.GONE
                step_one.visibility = View.VISIBLE
                previous.visibility = View.GONE
                onStep=-1
            }
            3-> {
                step_two.visibility = View.VISIBLE
                step_three.visibility = View.GONE
                next.setImageResource(R.drawable.ic_next)
                onStep-=1
            }
        }
    }

    private fun validateStepOne() : Boolean {
        return ValidationUtility.isNotEmptyField(name,manufactured_by,model,number)
    }

    private fun validateStepTwo() : Boolean {
        return ValidationUtility.isNotEmptyField(registration_number,driving_license_number,daily_price)
    }

    private fun initAddressView(){
        adapter = CustomListAdapter(this, R.layout.auto_complete_list_item, autoCompleteModelList)
        address_view.setAdapter(adapter)
        adapter?.notifyData()
    }

    protected val autoCompleteItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, l ->
        val selectedPlace = (adapterView.getItemAtPosition(pos) as AutoCompleteModel).id?.let {
            mapBoxAddresses?.features?.find { featuresId-> featuresId.id == it }
        }
        getAddress(selectedPlace)
        address_view.setText("")
    }

    private fun getAddress(selectedPlace: Feature?) {
        val address = com.example.sarwan.renkar.model.location.Address()
        selectedPlace?.let {
            it.place_name?.let { title-> address.address = title
                addAddressChip(title)
            }
            (it.geometry?.coordinates?.
                let { geometry-> geometry } ?:kotlin.run { null })?.let {
                coordinates->
                address.longitude = coordinates[0]
                address.latitude = coordinates[1]
            }
            address.id = addressCount
        }
        addressList.add(address)
    }

    private fun addAddressChip(title: String) {
        val chip = Chip(this)
        chip.text = title.capitalize()
        chip.isCloseIconEnabled = true
        chip.setTextColor(resources.getColor(R.color.darkest_grey))
        chip.chipBackgroundColor = resources.getColorStateList(R.color.light_grey)
        chip.closeIconTint = resources?.getColorStateList(R.color.darkest_grey)
        chip.chipStrokeColor = resources.getColorStateList(R.color.darkest_grey)
        chip.chipStrokeWidth = 1f

        // necessary to get single selection working
        chip.isClickable = true
        chip.isCheckable = false
        chip.tag = addressList.size
        address_chip_group.addView(chip as View)
        chip.setOnCloseIconClickListener {
            val chipItem = it as Chip
            address_chip_group.removeView(chipItem)
            removeAddressFromList(chipItem.text.toString())
        }
    }

    private fun removeAddressFromList(title: String) {
        addressList.remove(addressList.find { it?.address == title })
    }

    protected fun queryForAddress(text : String){
        RestClient.getRestAdapter(NetworkConstants.TPL_MAPS_BASE_URL).getAddresses(text).enqueue(object :
            Callback<ArrayList<Locations>>{
            override fun onFailure(call: Call<ArrayList<Locations>>?, t: Throwable) {
                Log.d(TAG, t.localizedMessage)
            }

            override fun onResponse(call: Call<ArrayList<Locations>>?, response: Response<ArrayList<Locations>>) {
                response.body()?.let {
                    makeAutoCompleteObject(it)
                }
            }
        })
    }

    protected fun hidePriceEstimationLayouts() {
        priceEstimation.visibility = View.GONE
        renkar_cut.visibility = View.GONE
    }

    protected fun showPriceEstimation(){
        priceEstimation.visibility = View.VISIBLE
        renkar_cut.visibility = View.VISIBLE
        priceEstimation.text = getString(R.string.hooray) +
                PriceUtility.weeklyEarning(daily_price.text.toString(), selectedDays?.count()) + getString(R.string.per_week)
        renkar_cut.text = getString(R.string.renkar_cut) + PriceUtility.dailyEarning(daily_price.text.toString()) + getString(R.string.per_day)
    }

    private fun makeStepOneFields(){
        car?.name = name.text.toString()
        car?.manufacturedBy = manufactured_by?.text?.toString()
        car?.model = model?.text?.toString()
        car?.instantBook = instantBook.isChecked
        car?.description = description?.text?.toString()
        car?.number = number?.text?.toString()
        car?.vehicleType = vehicle_type?.text?.toString()
        car?.miles = miles?.text?.toString()
        selectedDays?.let { car?.days = it }
        selectedFeatures?.let { car?.features = it }
        car?.listedBy = user?.name
    }

    private fun makeStepTwoFields(){
        car?.registration?.registerationNumber = registration_number.text?.toString()
        car?.license?.license = driving_license_number.text?.toString()
        car?.listerAmount = daily_price.text?.toString()
    }

    private var years = DateTimeUtility.getYears()

    private fun setupSpinners(){
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item, years)
        registration_city.adapter = adapter
        license_expiry.adapter = adapter
        license_expiry.tag = 1
        registration_city.tag = 0
        license_expiry.onItemSelectedListener = onSpinnerItemSelectedListener
        registration_city.onItemSelectedListener = onSpinnerItemSelectedListener

    }

    private val onSpinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(view?.tag){
                0-> car?.registration?.registeredIn = years[position]
                1-> car?.license?.expiry = years[position]
            }
        }
    }

    protected fun showPopupMenu(view: View) = PopupMenu(view.context, view).run {
        menu.add(getString(R.string.instant_book_desc))
        setOnMenuItemClickListener { item ->
            true
        }
        setOnDismissListener {
            dismiss()
        }
        show()
    }

    private fun makeAutoCompleteObject(addresses: ArrayList<Locations>){
        autoCompleteModelList.clear()
        addresses.let {
            for (i in it){
                i.lat?.let { lat->
                    i.lng?.let { lng->
                        val model = AutoCompleteModel()
                        model.title = i.name
                        model.id = i.subcat_name
                        autoCompleteModelList.add(model)
                    }
                }
            }
        }
        initAddressView()
    }

    private fun showSummaryDialog() {

    }

    protected fun changeViewWithAnimation(visibility: Int) {
        when(visibility){
            0->{addCarBar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)}
            8->{addCarBar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)}
        }
        addCarBar.visibility = visibility
    }

    protected fun saveSelectedColor(colorEnvelope: ColorEnvelope) {
        selectedColor.setBackgroundColor(colorEnvelope.color)
        car?.color = colorEnvelope.colorHtml
    }

    override fun onSelect(selectedFeature: Features, flag: FeaturesFragment.Action) {
        when(flag){
            FeaturesFragment.Action.ADDED -> selectedFeatures?.add(selectedFeature)
            FeaturesFragment.Action.REMOVED -> selectedFeatures?.remove(selectedFeature)
        }
    }

    override fun onSelect(selectedDay: String?, flag: DayFragment.Action) {
        when(flag){
            DayFragment.Action.ADDED -> selectedDay?.let { selectedDays?.add(it) }
            DayFragment.Action.REMOVED -> selectedDay?.let { selectedDays?.remove(it) }
        }
    }

    companion object {
        const val TAG = "Lister Add Fragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ListerProfileFragment.
         */
        @JvmStatic
        fun newInstance() = ListerAddCarBaseActivity()
    }
}
