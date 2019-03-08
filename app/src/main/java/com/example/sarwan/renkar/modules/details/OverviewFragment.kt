package com.example.sarwan.renkar.modules.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.model.Features
import com.example.sarwan.renkar.modules.features.FeaturesData
import com.example.sarwan.renkar.modules.features.FeaturesFragment
import com.example.sarwan.renkar.utils.BooleanUtility
import kotlinx.android.synthetic.main.overview_fragment.*

class OverviewFragment : Fragment() {

    private var car : Cars ? = null
    private lateinit var pActivity: CarDetailsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as CarDetailsActivity
        arguments?.let {
            car = it.getSerializable(ApplicationConstants.CAR_DETAILS_KEY) as Cars
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.overview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeFragment()
    }

    private fun makeFragment() {
        updateLayouts()
        checkForUser()
        onClickListener()
    }

    fun init(car: Cars){
        this.car = car
        makeFragment()
    }
    private fun onClickListener() {
        bookNow.setOnClickListener {
            //TODO -- Booking flow
        }
    }

    private fun checkForUser() {
        pActivity.user?.let {
            it.lister?.let {lister->

            }?:kotlin.run {
                bookNow.visibility = View.VISIBLE
                ratingBar.visibility = View.VISIBLE
            }
        }
    }

    private fun updateLayouts() {
        addFeaturesFragment()
        setImage()
        setAddress()
        setOwner()
        setRegistration()
        setRating()
        setBasicDetails()
        setSpecifications()
        setPrice()
    }

    private fun setPrice() {
        price.text = "${car?.price}PKR/day"
        updateView()
    }

    private fun updateView() {
        temp_layout.visibility = View.GONE
        pActivity.hideProgress()
    }

    private fun setSpecifications() {
        fuel.text = "Fuel Type : ${car?.specifications?.fuelType}"
        capacity.text = "Capacity : ${car?.specifications?.capacity} persons "
        BooleanUtility.toMeaningfulString(car?.specifications?.delivery)?.let { delivery.text = "Delivery : $it" }
        BooleanUtility.toMeaningfulString(car?.specifications?.instantBook)?.let { instant_book.text = "Instant Book : $it" }
    }

    private fun addFeaturesFragment() {
        childFragmentManager.beginTransaction().add(R.id.features_fragment, FeaturesFragment.newInstance(getFeatures())).commit()
    }

    private fun getFeatures(): ArrayList<Features> {
        val features : ArrayList<Features> = ArrayList()
        car?.features?.apply {
            for (i in this)
                FeaturesData.populateFeatures(pActivity).find { it.name == i.name }?.let { features.add(it) }
        }
        return features
    }

    private fun setRating() {
        ratingBar.rating = car?.rating?.let { it }?:kotlin.run { 3f }
    }

    private fun setBasicDetails() {
        number.text = car?.number
        details.text = "${car?.basic?.manufacturedBy} - ${car?.basic?.model} - ${car?.specifications?.vehicleType}"
        car_name.text = car?.basic?.name
        color.setBackgroundColor(Color.parseColor("#${car?.specifications?.color}"))
        description.text = car?.basic?.description
    }

    private fun setRegistration() {
        registration.text = "Reg # ${car?.registration?.number} in the year ${car?.registration?.registeredIn}"
    }

    private fun setOwner() {
        owner.text = "${car?.owner?.name} (${car?.owner?.email})"
    }

    private fun setAddress() {
        address_tv.text = car?.address?.address
    }

    private fun setImage() {
        icon_image.setImageURI(car?.basic?.coverImagePath)
    }

    companion object {
        @JvmStatic
        fun newInstance(car: Cars) = OverviewFragment().apply {
            arguments= Bundle().apply {
                putSerializable(ApplicationConstants.CAR_DETAILS_KEY, car)
            }
        }
    }
}