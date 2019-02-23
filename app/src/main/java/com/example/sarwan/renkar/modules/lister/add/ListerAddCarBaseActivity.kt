package com.example.sarwan.renkar.modules.lister.add

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.*
import com.example.sarwan.renkar.firebase.FirebaseExtras
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.fragments.ConfirmationFragment
import com.example.sarwan.renkar.model.AutoCompleteModel
import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.model.Features
import com.example.sarwan.renkar.model.TPL.Locations
import com.example.sarwan.renkar.modules.camera.CameraActivity
import com.example.sarwan.renkar.modules.days.DayFragment
import com.example.sarwan.renkar.modules.features.FeaturesFragment
import com.example.sarwan.renkar.network.NetworkConstants
import com.example.sarwan.renkar.network.RestClient
import com.example.sarwan.renkar.utils.DateTimeUtility
import com.example.sarwan.renkar.utils.LocationUtility
import com.example.sarwan.renkar.utils.PriceUtility
import com.example.sarwan.renkar.utils.ValidationUtility
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.chip.Chip
import com.skydoves.colorpickerpreference.ColorEnvelope
import kotlinx.android.synthetic.main.add_car_step_one.*
import kotlinx.android.synthetic.main.add_car_step_two.*
import kotlinx.android.synthetic.main.lister_add_car_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


open class ListerAddCarBaseActivity : ParentActivity(), FeaturesFragment.FeaturesInteractionListener,
    DayFragment.DaysInteractionListener, ImageUpload.ImageUploadResponse,
        ToFirebaseStorage.ToFirebaseStorageListener, CustomTextWatcher.TextWatcherListener,
    FirebaseExtras.Companion.PutObjectCallBack, ConfirmationFragment.ConfirmationFragmentCallBack<Any>
{

    protected var onStep : Int = 1
    private var tplMapsLocations: ArrayList<Locations> ? = null
    protected var adapter: CustomListAdapter? = null
    protected var autoCompleteModelList : ArrayList<AutoCompleteModel> = ArrayList()
    private var selectedFeatures : ArrayList<Features> ? = ArrayList()
    private var selectedDays : ArrayList<String> ? = ArrayList()
    protected var selectedImage : Uri ? = null
    protected var imageUpload : ImageUpload? = null
    private var toFirebaseStorage : ToFirebaseStorage ? = null
    protected var car : Cars? = null
    private var carSpecs : Cars.Specifications? = null
    private var carReg : Cars.Registration? = null
    private var summaryFragment : SummaryFragment ? = null
    private var confirmationFragment : ConfirmationFragment ? = null

    protected fun initializeClasses(){
        car = Cars()
        carSpecs = Cars.Specifications()
        carReg = Cars.Registration()
        toFirebaseStorage = ToFirebaseStorage()
        imageUpload = ImageUpload(this)
    }

    protected fun initializeListeners() {
        imageUpload?.imageUploadResponse = this
        toFirebaseStorage?.listener = this
    }

    protected fun layoutTransitionOnNextButton() {
        when(onStep){
            1-> {
                goToSecondStep()
            }
            2-> {
                makeDataNodes()
            }
        }
    }

    private fun goToSecondStep() {
        if (validateStepOne()){
            makeStepOneFields()
            setupSpinners()
            step_one.visibility = View.GONE
            step_two.visibility = View.VISIBLE
            previous.visibility = View.VISIBLE
            next.setImageResource(R.drawable.ic_check_black_24dp)
            onStep+=1
        }
    }

    protected fun layoutTransitionOnPreviousButton() {
        when(onStep){
            2-> {
                step_two.visibility = View.GONE
                step_one.visibility = View.VISIBLE
                previous.visibility = View.INVISIBLE
                next.setImageResource(R.drawable.ic_next)
                onStep-=1
            }
        }
    }

    private fun validateStepOne() : Boolean {
        var validated = (ValidationUtility.isNotEmptyField(name,manufactured_by,model,number))

        (ValidationUtility.yearRangeValidation(model)).apply {
            if (this)
                validated = this
            else
                ValidationUtility.setErrors(this@ListerAddCarBaseActivity, getString(R.string.model_warning), model)
        }
        return validated
    }

    private fun validateStepTwo() : Boolean {
        return ValidationUtility.isNotEmptyField(registration_number,daily_price)
    }

    private fun initAddressView(){
        address_text_view.setDropDownBackgroundResource(R.color.v_lightest_grey)
        adapter = CustomListAdapter(this, R.layout.auto_complete_list_item, autoCompleteModelList)
        address_text_view.setAdapter(adapter)
        adapter?.notifyData()
    }

    protected val autoCompleteItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, l ->
        val selectedPlace = (adapterView.getItemAtPosition(pos) as AutoCompleteModel).id?.let {id->
            tplMapsLocations?.find { location-> location.fkey == id }
        }
        getAddress(selectedPlace)
        address_text_view.setText("")
    }

    private fun getAddress(selectedPlace: Locations?) {
        val address = com.example.sarwan.renkar.model.location.Address()
        selectedPlace?.let {
            address.latitude = it.lat
            address.longitude = it.lng
            address.id = it.fkey
            it.name?.run {
                let {name->
                    address.address = name + "," +it.compound_address_parents
                    addAddressChip(name + "," +it.compound_address_parents)
                }
            }
            car?.address?.let {map->
                map[FirebaseExtras.ADDRESS] = address.address as String
                map[FirebaseExtras.LATITUDE] = address.latitude as Double
                map[FirebaseExtras.LONGITUDE] = address.longitude as Double
            }
        }
    }

    private fun addAddressChip(title: String) {
        val chip = Chip(this)
        chip.text = title.capitalize()
        chipAttributes(chip)
        hide(address_text_view)
        address_chip_group.addView(chip as View)
        chipRemoveListener(chip)
    }

    private fun chipRemoveListener(chip: Chip) {
        chip.setOnCloseIconClickListener {
            val chipItem = it as Chip
            address_chip_group.removeView(chipItem)
            car?.address = hashMapOf()
            show(address_text_view)
        }
    }

    private fun chipAttributes(chip: Chip) {
        chip.isCloseIconEnabled = true
        chip.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        chip.minHeight = 100
        chip.setTextColor(resources.getColor(R.color.lightest_grey))
        chip.chipBackgroundColor = resources.getColorStateList(R.color.white)
        chip.closeIconTint = resources?.getColorStateList(R.color.colorAccent)
        chip.chipStrokeColor = resources.getColorStateList(R.color.lightest_grey)
        chip.setTextColor(resources.getColor(R.color.dark_grey))
        chip.chipStrokeWidth = 2.5f
        chip.chipCornerRadius = resources.getDimension(R.dimen.et_corner_radius)
        // necessary to get single selection working
        chip.isClickable = true
        chip.isCheckable = false
    }

    private fun queryForAddress(text : String){
        RestClient.getRestAdapter(NetworkConstants.TPL_MAPS_BASE_URL).getAddresses(text).enqueue(object :
            Callback<ArrayList<Locations>>{
            override fun onFailure(call: Call<ArrayList<Locations>>?, t: Throwable) {
                Log.d(TAG, t.localizedMessage)
            }

            override fun onResponse(call: Call<ArrayList<Locations>>?, response: Response<ArrayList<Locations>>) {
                response.body()?.let {
                    tplMapsLocations = it
                    makeAutoCompleteObject()
                }
            }
        })
    }

    private fun hidePriceEstimationLayouts() {
        priceEstimation.visibility = View.GONE
    }

    private fun showPriceEstimation(){
        hideKeyboard(daily_price)
        priceEstimation.visibility = View.VISIBLE
        priceEstimation.text = "${getString(R.string.hooray)} ${PriceUtility.weeklyEarning(daily_price?.text?.toString()?.let { it }?:kotlin.run { "7" }
            , selectedDays?.count())} " + "${getString(R.string.per_week)} | " +
                "${PriceUtility.dailyEarning(daily_price?.text?.toString()?.let{it}?:kotlin.run { "1" })} ${getString(R.string.per_day)}"
    }

    private fun makeStepOneFields(){
        makeCarBasicData()
        makeCarSpecifications()
        makeCarAvailabilityDays()
        makeCarFeatures()
        makeOwnerData()
    }

    private fun makeCarBasicData() {
        car?.basic?.let {map->
            map[FirebaseExtras.NAME] = name.text.toString()
            map[FirebaseExtras.MANUFACTURED_BY] = manufactured_by?.text.toString()
            map[FirebaseExtras.MODEL] = model?.text.toString()
            map[FirebaseExtras.DESCRIPTION] = description?.text.toString()
            map[FirebaseExtras.MILES] = miles?.text.toString()
        }
        car?.number = number?.text?.toString()
    }


    private fun makeCarSpecifications() {
        carSpecs?.vehicleType = vehicle_type?.text?.toString()
        carSpecs?.instantBook = instantBook.isChecked
        carSpecs?.delivery = delivery.isChecked
    }

    private fun makeCarAvailabilityDays() {
        selectedDays?.let { car?.days = it }
    }

    private fun makeCarFeatures() {
        selectedFeatures?.let { car?.features = it }
    }

    protected fun makeCarCapacity(value: String) {
        carSpecs?.capacity = value
    }

    private fun makeOwnerData() {
        car?.owner?.let {map->
            map[FirebaseExtras.EMAIL] = user?.email.toString()
            map[FirebaseExtras.NAME] = user?.name.toString()
            map[FirebaseExtras.IMAGE] = user?.image_url.toString()
        }
    }

    protected fun makeCarFuel(fuel: String) {
        carSpecs?.fuelType = fuel
    }

    private fun makeStepTwoFields(){
        makeCarRegNumber(registration_number.text?.toString())
        makeCarPrice()
    }

    private fun makeCarRegYear(year : String){
        carReg?.registeredIn = year
    }

    private fun makeCarRegNumber(number : String?) {
        carReg?.number = number
    }

    private fun makeCarPrice() {
        car?.price = daily_price.text?.toString()
    }

    private var years = DateTimeUtility.getYears()

    private fun setupSpinners(){
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinnerText, years)
        registration_city.adapter = adapter
        registration_city.setSelection(0)
        registration_city.onItemSelectedListener = onSpinnerItemSelectedListener
    }

    private val onSpinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            registration_city.setSelection(position)
            makeCarRegYear(years[position])
            registered_in.text = "${getString(R.string.registered_in)} ${years[position]} "
        }
    }

    protected fun showPopupMenu(view: View, text: String) = PopupMenu(view.context, view).run {
        menu.add(text)
        setOnMenuItemClickListener { item ->
            true
        }
        setOnDismissListener {
            dismiss()
        }
        show()
    }

    private fun makeAutoCompleteObject(){
        autoCompleteModelList.clear()
        tplMapsLocations?.let {
            for (i in it){
                i.lat?.let { lat->
                    i.lng?.let { lng->
                        val model = AutoCompleteModel()
                        model.title = i.name + "," + i.compound_address_parents
                        model.id = i.fkey
                        autoCompleteModelList.add(model)
                    }
                }
            }
        }

        if (autoCompleteModelList.isNotEmpty())
            initAddressView()
    }

    private fun makeDataNodes() {
        if(validateStepTwo() && car?.address?.isNotEmpty()!!){
            makeStepTwoFields()
            try {
                car?.address?.let {
                    it[FirebaseExtras.LATITUDE]?.let { lat->
                        it[FirebaseExtras.LONGITUDE]?.let { lon->
                            makeNearestKms(lat.toString().toDouble(), lon.toString().toDouble())
                        }
                    }
                }
            }catch (e: Exception){
                e.localizedMessage
            }
        }
    }

    private fun makeNearestKms(lat: Double, lon: Double) {
        car?.nearestFrom = LocationUtility.getNearest(lat,lon)
        checkForCarAvailability()

    }

    private fun checkForCarAvailability() {
        car?.let {
            it.number?.let {number->
                showProgress()
                FirestoreQueryCenter.checkIfCarExists(number, this)
            }
        }
    }

    protected fun changeViewWithAnimation(visibility: Int) {
        if(onStep == 1){
            when(visibility){
                0->{addCarBar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)}
                8->{addCarBar.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)}
            }
            addCarBar.visibility = visibility
        }
    }

    protected fun saveSelectedColor(colorEnvelope: ColorEnvelope) {
        selectedColor.setBackgroundColor(colorEnvelope.color)
        makeCarColor(colorEnvelope.colorHtml)
    }

    private fun makeCarColor(colorHtml: String) {
        carSpecs?.color = colorHtml
    }

    private fun uploadImageToFirebase() {
        selectedImage?.let {
            toFirebaseStorage?.uploadFile(it)
        }?:kotlin.run {
            uploadCar()
        }
    }


    private fun showSummaryDialog() {
        attachFragment(SUMMARY_FRAGMENT)
    }

    private fun attachFragment(fragment_tag: String) {
        hideProgress()
        when(fragment_tag){
            SUMMARY_FRAGMENT->{
                SummaryFragment().run {
                    summaryFragment = this
                    if (!isAdded)
                        show(createManager(), fragment_tag)
                }
            }
            CONFIRMATION_FRAGMENT->{
                ConfirmationFragment.newInstance(ConfirmationFragment.Companion.ConfirmationType.CAR_UPDATE.ordinal).run {
                    confirmationFragment = this
                    confirmationFragment?.initListener(this@ListerAddCarBaseActivity)
                    if (!isAdded)
                        show(createManager(), fragment_tag)
                }
            }
        }
    }

    private fun createManager(): FragmentManager {
            supportFragmentManager.beginTransaction().run {
            run {
                if ((supportFragmentManager.findFragmentByTag(SUMMARY_FRAGMENT) != null))
                    supportFragmentManager.findFragmentByTag(SUMMARY_FRAGMENT)?.let { this.remove(it) }
                addToBackStack(null)
            }
        }
        return supportFragmentManager
    }

    protected fun showOptionsForImage() {
        val items = arrayOf<CharSequence>("Capture an Image", "Choose from Library")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            when (item) {
                0 -> openCameraForImage()

                1 -> openGalleryForImage()

            }
        }
        builder.show()
    }

    private fun openGalleryForImage() {
        imageUpload?.openGalleryForImage(ApplicationConstants.WRITE_EXTERNAL_STORAGE_CODE)
    }

    private fun openCameraForImage() {
        openActivityForResults(Intent(this, CameraActivity::class.java), ApplicationConstants.CAPTURE_IMAGE_STORAGE_CODE)
    }

    protected fun setImageToLayout(data: Intent) {
        data.data?.let {
            it.toString().isNotEmpty().apply {
                if (this){
                    selectedImage = it
                    carImage.setImageURI(it.toString())
                }
            }
        }
    }

    override fun uploadDone(filePath: Uri) {
        car?.basic?.let {
            it[FirebaseExtras.COVER_IMAGE] = filePath.toString()
            uploadCar()
        }
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

    override fun doUpload(path: Uri?) {
        carImage.setImageURI(path.toString())
    }

    override fun doUpload(bitmap: Bitmap?) {
        carImage.setImageBitmap(bitmap)
    }

    fun clearCache(path : String){
        Fresco.getImagePipeline().evictFromCache(Uri.parse(path))
    }

    private fun addressWatcher(s: CharSequence?) {
        s?.length?.let {
            if (it>=2)
                queryForAddress(s.toString())
        }
    }

    private fun dailyPriceWatcher(s: CharSequence?) {
        s?.let {
            if (it.length < 3)
                hidePriceEstimationLayouts()
            else if (it.length == 3)
                showPriceEstimation()
        }
    }

    private fun carModelNumberValidation(s: CharSequence?) {
        s?.apply {
            if (!startsWith("20"))
                model.error = resources.getString(R.string.model_warning)
            else
                model.error = null
        }
    }

    override fun onChanged(view: String, s: CharSequence?, start: Int, before: Int, count: Int) {
        when(view){
            address_text_view.id.toString()-> {
                addressWatcher(s)
            }

            daily_price.id.toString()->{
                dailyPriceWatcher(s)
            }
            model.id.toString()->{
                carModelNumberValidation(s)
            }

        }
    }

    protected fun onEditorActionListener(v: TextView, actionId: Int, event: KeyEvent?, default: Boolean): Boolean {
        return when (actionId) {
            EditorInfo.IME_ACTION_NEXT -> when(v.id){
                number.id->{
                    checkValidAlphaNumericField(v)
                }
                else-> default
            }
            EditorInfo.IME_ACTION_DONE -> when(v.id){
                daily_price.id->{
                    showPriceEstimation()
                    default
                }
                else-> default
            }
            else -> default
        }
    }

    private fun checkValidAlphaNumericField(v: TextView) : Boolean {
        return ValidationUtility.isValidRegex(v).apply {
            if (this)
                return false
            else
                v.error = getString(R.string.must_contain_alpha_numeric) ; return true
        }
    }
    private fun uploadCar() {
        car?.let {car->
            car.number?.let {
                FirestoreQueryCenter.batchWrite(it,car, carReg as Any, carSpecs as Any,this)
            }
        }
    }


    override fun onPutSuccess(code: Int) {
        when(code){
            FirebaseExtras.CAR_EXISTS->{
                uploadImageToFirebase()
            }
            FirebaseExtras.UPLOAD_SUCCESS->{
                showSummaryDialog()
            }
        }
    }

    override fun onPutFailure(code: Int) {
        hideProgress()
        when(code){
            FirebaseExtras.CAR_EXISTS ->{
                attachFragment(CONFIRMATION_FRAGMENT)
            }
            FirebaseExtras.UPLOAD_FAILURE -> {
                Toast.makeText(this, getString(R.string.some_thing_went_wrong),Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onAction(type: Any, option: Any) {
        when(option){
            ConfirmationFragment.Companion.ConfirmationOption.ALLOW.ordinal -> {
                when(type){
                    ConfirmationFragment.Companion.ConfirmationType.CAR_UPDATE.ordinal -> {
                        updateCar()
                    }
                }
            }
            ConfirmationFragment.Companion.ConfirmationOption.DENY.ordinal -> {
                when(type){
                    ConfirmationFragment.Companion.ConfirmationType.CAR_UPDATE.ordinal -> {
                        finishScreen()
                    }
                }
            }
        }
    }

    private fun updateCar() {
        showProgress()
        uploadImageToFirebase()
    }

    private fun finishScreen(){
        Toast.makeText(this, getString(R.string.car_already_exists),Toast.LENGTH_LONG).show()
    }


    companion object {
        val TAG = "add-lister"
        val SUMMARY_FRAGMENT = "summary-fragment"
        val CONFIRMATION_FRAGMENT = "confirmation-fragment"
    }
}
