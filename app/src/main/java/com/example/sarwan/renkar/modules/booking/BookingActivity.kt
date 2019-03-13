package com.example.sarwan.renkar.modules.booking

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.fragments.ConfirmationFragment
import com.example.sarwan.renkar.model.*
import com.example.sarwan.renkar.modules.history.HistoryHelper
import com.example.sarwan.renkar.modules.payment_method.CardsList
import com.example.sarwan.renkar.utils.DateTimeUtility
import com.example.sarwan.renkar.utils.ModelMappingUtility
import com.example.sarwan.renkar.utils.ModelMappingUtility.Companion.makeBookingObject
import kotlinx.android.synthetic.main.booking_activity.*
import java.util.*

class BookingActivity : ParentActivity(), ConfirmationFragment.ConfirmationFragmentCallBack<Any> {

    private lateinit var intentObject : History
    private lateinit var car : Cars

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booking_activity)
        getIntentData()
        clickListener()
    }

    private fun clickListener() {
        book.setOnClickListener {
            ConfirmationFragment.getInstance(ConfirmationFragment.Companion.ConfirmationType.BOOKING.ordinal)?.
                initListener(this)?.show(supportFragmentManager, ConfirmationFragment::class.java.simpleName)
        }
    }

    private fun getIntentData() {
        intentObject = intent?.extras?.getSerializable(ApplicationConstants.BOOKING_OBJECT) as History
        fetchPaymentMethod()
        fetchCarDetails()
    }

    private fun fetchCarDetails(){
        intentObject.car_number?.let {
            FirestoreQueryCenter.getCar(it).get().addOnCompleteListener { task->
                task.result?.toObject(Cars::class.java)?.let { car->
                    this.car = car
                    setCarDetails()
                    setPrice()
                }
            }
        }
    }


    private fun setCarDetails() {
        number.text = car?.number
        details.text = "${car?.basic?.manufacturedBy} - ${car?.basic?.model}"
        car_name.text = car?.basic?.name
        address_tv.text = car.address?.address
        icon_image.setImageURI(car?.basic.coverImagePath)
    }

    private fun setPrice() {
        price.text = car.price
        price_total.text = (car.price?.toInt()?.times(car.days?.count()?:3).toString())
    }

    private fun setPaymentMethod(){
        paymentMethods?.let {
            if (it.isNotEmpty()){
                it.first().apply {
                    name?.let { name->card_icon.setImageURI(CardsList.getIcon(this@BookingActivity, name))}
                    card_name.text = name
                    payment_method_card_number.text = number
                }
            }
            else {
                noPaymentMethodFound()
            }
        }?:kotlin.run {
            noPaymentMethodFound()
        }
    }

    private fun noPaymentMethodFound() {
        book.visibility = View.GONE
        showMessage("You must add payment method to continue booking process")
    }

    private var paymentMethods: ArrayList<PaymentMethods>? = null

    private fun fetchPaymentMethod() {
        intentObject.rentedBy?.let {
            FirestoreQueryCenter.getRenter(it).addOnCompleteListener {task->
                paymentMethods = task.result?.toObject(RenterProfile::class.java)?.paymentMethod
                user?.renter?.cars = task.result?.toObject(RenterProfile::class.java)?.cars
                setPaymentMethod()
            }
        }
    }

    override fun onAction(type: Any, option: Any) {
        if (type as Int == ConfirmationFragment.Companion.ConfirmationType.BOOKING.ordinal
            && option as Int == ConfirmationFragment.Companion.ConfirmationOption.ALLOW.ordinal){
            bookCarOnFirestore()
        }
    }

    private fun bookCarOnFirestore() {
        intentObject?.apply {
            showProgress()
            FirestoreQueryCenter.makeBooking(makeBookingId(),
                makeBookingObject(car_number,listedBy,rentedBy,period,price_total.text.toString())).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    setHistory()
                    setRentedCar()
                    showMessage("Congratulations! You successfully rented ${intentObject.car_number}. Enjoy ahead!!")
                }
                else showMessage(getString(R.string.internet_connectivity))
                hideProgress()
                book.isEnabled = false
            }
        }
    }

    private fun setRentedCar() {
        car.number?.let {number->
            user?.email?.let{
                FirestoreQueryCenter.addCarToNode(user?.type,it,number)
            }
        }
    }

    private fun setHistory() {
        val nextDate = Calendar.getInstance().apply {
            set(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE) + car?.days?.size!!)
            }

        intentObject.apply {
            status = History.STATUS.ON_BOOKING.name
            period = """${DateTimeUtility.format.format(Calendar.getInstance().time)} - ${DateTimeUtility.format.format(nextDate.time)}"""
            HistoryHelper.create(this)
        }
    }

    private fun makeBookingId(): String {
        return """${intentObject.listedBy}-${intentObject.rentedBy}-${intentObject.car_number}"""
    }

}