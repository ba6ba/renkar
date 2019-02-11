package com.example.sarwan.renkar.modules.lister.add

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.*
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
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
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.chip.Chip
import com.skydoves.colorpickerpreference.ColorEnvelope
import kotlinx.android.synthetic.main.add_car_step_one.*
import kotlinx.android.synthetic.main.add_car_step_three.*
import kotlinx.android.synthetic.main.add_car_step_two.*
import kotlinx.android.synthetic.main.lister_add_car_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class ListerAddCarBaseActivity : ParentActivity(), FeaturesFragment.FeaturesInteractionListener,
    DayFragment.DaysInteractionListener, ImageUpload.ImageUploadResponse,
        ToFirebaseStorage.ToFirebaseStorageListener {

    private var onStep : Int = 1
    private var address: com.example.sarwan.renkar.model.location.Address? = null
    private var tplMapsLocations: ArrayList<Locations> ? = null
    protected var adapter: CustomListAdapter? = null
    protected var autoCompleteModelList : ArrayList<AutoCompleteModel> = ArrayList()
    private var selectedFeatures : ArrayList<Features> ? = ArrayList()
    private var selectedDays : ArrayList<String> ? = ArrayList()
    protected var selectedImage : Uri ? = null
    protected var imageUpload : ImageUpload? = null
    private var toFirebaseStorage : ToFirebaseStorage ? = null
    protected var car : Cars? = null

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
                onStep-=1
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
        val selectedPlace = (adapterView.getItemAtPosition(pos) as AutoCompleteModel).id?.let {id->
            tplMapsLocations?.find { location-> location.fkey == id }
        }
        getAddress(selectedPlace)
        address_view.setText("")
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
                    addAddressChip(name)
                }
            }
            this.address = address
        }
    }

    private fun addAddressChip(title: String) {
        val chip = Chip(this)
        chip.text = title.capitalize()
        chipAttributes(chip)
        hide(address_view)
        address_chip_group.addView(chip as View)
        chipRemoveListener(chip)
    }

    private fun chipRemoveListener(chip: Chip) {
        chip.setOnCloseIconClickListener {
            val chipItem = it as Chip
            address_chip_group.removeView(chipItem)
            address = null
            show(address_view)
        }
    }

    private fun chipAttributes(chip: Chip) {
        chip.isCloseIconEnabled = true
        chip.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        chip.minHeight = 80
        chip.setTextColor(resources.getColor(R.color.lightest_grey))
        chip.chipBackgroundColor = resources.getColorStateList(R.color.white)
        chip.closeIconTint = resources?.getColorStateList(R.color.colorAccent)
        chip.chipStrokeColor = resources.getColorStateList(R.color.lightest_grey)
        chip.setTextColor(resources.getColor(R.color.dark_grey))
        chip.elevation = resources.getDimension(R.dimen.normal_elevation)
        chip.chipStrokeWidth = 1.5f
        chip.chipCornerRadius = resources.getDimension(R.dimen.et_corner_radius)
        // necessary to get single selection working
        chip.isClickable = true
        chip.isCheckable = false
    }

    protected fun queryForAddress(text : String){
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

    protected fun hidePriceEstimationLayouts() {
        priceEstimation.visibility = View.GONE
        renkar_cut.visibility = View.GONE
    }

    protected fun showPriceEstimation(){
        priceEstimation.visibility = View.VISIBLE
        renkar_cut.visibility = View.VISIBLE
        priceEstimation.text = "${getString(R.string.hooray)} ${PriceUtility.weeklyEarning(daily_price.text.toString(), selectedDays?.count())} ${getString(R.string.per_week)}"
        renkar_cut.text = "${getString(R.string.renkar_cut)} ${PriceUtility.dailyEarning(daily_price.text.toString()) + getString(R.string.per_day)}"
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
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinnerText, years)
        registration_city.adapter = adapter
        license_expiry.adapter = adapter
        license_expiry.tag = 1
        registration_city.tag = 0
        license_expiry.setSelection(0)
        registration_city.setSelection(0)
        license_expiry.onItemSelectedListener = onSpinnerItemSelectedListener
        registration_city.onItemSelectedListener = onSpinnerItemSelectedListener
    }

    private val onSpinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(parent?.tag){
                0-> {
                    registration_city.setSelection(position)
                    car?.registration?.registeredIn = years[position]
                    registered_in.text = "${getString(R.string.registered_in)} ${years[position]} "
                }
                1-> {
                    license_expiry.setSelection(position)
                    car?.license?.expiry = years[position]
                    expires_in.text = "${getString(R.string.expires_in)} ${years[position]} "
                }
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

    private fun showSummaryDialog() {
        address?.latitude?.let {lat->
            address?.longitude?.let { lon->
                car?.nearestKms = LocationUtility.getNearest(lat,lon)
                Toast.makeText(this, car?.nearestKms.toString(), Toast.LENGTH_LONG).show()
                uploadImageToFirebase()
            }
        }
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

    private fun uploadImageToFirebase(){
        selectedImage?.let {
            toFirebaseStorage?.uploadFile(it)
            addCarOnFirestore()
        }
    }

    private fun addCarOnFirestore(){
        car?.let {
            it.number?.let {number->
                FirestoreQueryCenter.addCar(number, it).addOnSuccessListener{result->
                    Toast.makeText(this,getString(R.string.car_add_successfully),Toast.LENGTH_LONG).show()
                }
            }
        }
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
        car?.coverImagePath = filePath
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
        carImage.setImageURI(path)
    }

    override fun doUpload(bitmap: Bitmap?) {
        carImage.setImageBitmap(bitmap)
    }

    fun clearCache(path : String){
        Fresco.getImagePipeline().evictFromCache(Uri.parse(path))
    }

    companion object {
        val TAG = "add-lister"
    }
}
