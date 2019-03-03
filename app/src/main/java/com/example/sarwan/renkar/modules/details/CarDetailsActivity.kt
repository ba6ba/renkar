package com.example.sarwan.renkar.modules.details

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.model.Features
import com.example.sarwan.renkar.modules.cars.AllCarsFragment
import com.example.sarwan.renkar.modules.features.FeaturesFragment
import com.example.sarwan.renkar.utils.ModelMappingUtility
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.car_details_layout.*

class CarDetailsActivity : ParentActivity(), FeaturesFragment.FeaturesInteractionListener {

    override fun onSelect(selectedFeature: Features, flag: FeaturesFragment.Action) {

    }

    private lateinit var car : Cars
    private var relatedCars : ArrayList<Cars> ? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_details_layout)
        showProgress()
        getIntentData()
        queryForRelatedCars()
        setBottomSheet()
    }

    private fun queryForRelatedCars() {
        FirestoreQueryCenter.getCars().get().addOnCompleteListener {
            it.result?.apply {
                if (!isEmpty) {
                    relatedCars?.addAll(toObjects(Cars::class.java))
                    initRelatedCarsFragment()
                }
            }
        }
    }

    private fun initRelatedCarsFragment() {
        supportFragmentManager.beginTransaction().add(R.id.related_cars_fragment, AllCarsFragment.newInstance(relatedCars)).commit()
    }

    private fun getIntentData() {
        intent?.extras?.getSerializable(ApplicationConstants.CAR_DETAILS_KEY)?.let {
            car = it as Cars
            queryForData()
        }
    }

    private fun queryForData() {
        car.number?.let { it ->
            FirestoreQueryCenter.getSpecifications(it).get().addOnCompleteListener { task->
                car.specifications = task.result?.toObjects(Cars.Specifications::class.java)?.first()
            }

            FirestoreQueryCenter.getRegistration(it).get().addOnCompleteListener { taskk->
                car.registration = taskk.result?.toObjects(Cars.Registration::class.java)?.first()
                initFragment()
            }
        }
    }

    private fun initFragment() {
        hideProgress()
        supportFragmentManager.beginTransaction().add(R.id.overview_fragment, OverviewFragment.newInstance(car)).commit()
    }


    private fun setBottomSheet() {
        BottomSheetBehavior.from(bottomSheet).apply {
            isHideable = false
            isFitToContents = true
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when(newState){
                        BottomSheetBehavior.STATE_EXPANDED->{
                            com.example.sarwan.renkar.extras.BottomSheetBehavior.setStatusBarDim(this@CarDetailsActivity,true)
                        }
                        BottomSheetBehavior.STATE_HIDDEN->{}
                        BottomSheetBehavior.STATE_COLLAPSED->{}
                        BottomSheetBehavior.STATE_HALF_EXPANDED->{}
                        BottomSheetBehavior.STATE_DRAGGING->{}
                        BottomSheetBehavior.STATE_SETTLING->{}
                    }
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
