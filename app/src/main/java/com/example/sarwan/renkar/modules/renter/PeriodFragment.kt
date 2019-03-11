package com.example.sarwan.renkar.modules.renter

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.fragments.ConfirmationFragment
import com.example.sarwan.renkar.utils.DateTimeUtility
import kotlinx.android.synthetic.main.period_fragment_dialog.*
import java.util.*

class PeriodFragment : DialogFragment() {

    var days : ArrayList<Int> ? = null
    var listener : PeriodFragmentCallBack? = null
    private lateinit var pActivity :ParentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
        arguments?.let {
            days = it.getIntegerArrayList(DAYS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.period_fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        updateScreen()
        onClickListener()
    }

    private fun onClickListener() {
        rent_from.setOnClickListener {
            openRentFromPickerDialog()
        }

        rent_to.setOnClickListener {
            openRentToPickerDialog()
        }
    }

    private var rentFrom : Calendar = Calendar.getInstance()
    private var rentTo : Calendar = Calendar.getInstance()

    var rentToDate: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        rentTo.set(Calendar.YEAR, year)
        rentTo.set(Calendar.MONTH, monthOfYear)
        rentTo.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateRentToLabel()
    }

    private fun updateRentToLabel() {
        rent_to.text = String.format(/*"%1\$tA,*/"%1\$td-%1\$tb-%1\$tY", this.rentTo)
        if (rentTo.after(rentFrom)){
            dismissFragment()
        }else{
            Toast.makeText(pActivity, "Rent to date must be greater than rent from date", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateRentFromLabel(){
        rent_from.text = String.format(/*"%1\$tA,*/"%1\$td-%1\$tb-%1\$tY", this.rentFrom)
    }

    var rentFromDate: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        rentFrom.set(Calendar.YEAR, year)
        rentFrom.set(Calendar.MONTH, monthOfYear)
        rentFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateRentFromLabel()
    }


    private fun dismissFragment() {
        Toast.makeText(pActivity, "Our system will remove days that are not mentioned above from selected range", Toast.LENGTH_LONG).show()
        listener?.onSelection( """${rent_from.text} - ${rent_to.text}""")
        dismissAllowingStateLoss()
    }

    private fun openRentFromPickerDialog() {
        val datePickerDialog = DatePickerDialog(pActivity, rentFromDate, rentFrom.get(Calendar.YEAR), rentFrom.get(Calendar.MONTH),
            rentFrom.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    private fun openRentToPickerDialog() {
        val datePickerDialog = DatePickerDialog(pActivity, rentToDate, rentTo.get(Calendar.YEAR), rentTo.get(Calendar.MONTH),
            rentTo.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    private fun updateScreen() {
        for (i in 0 until 7){
            if (days?.contains(i) == true){
                if (i==1) pActivity.show(sunday)
                if (i==2) pActivity.show(monday)
                if (i==3) pActivity.show(tuesday)
                if (i==4) pActivity.show(wednesday)
                if (i==5) pActivity.show(thursday)
                if (i==6) pActivity.show(friday)
                if (i==7) pActivity.show(saturday)
            }
        }
/*DateTimeUtility.getNextAvailableDayFromToday(days)*/
        rent_from.text = String.format(/*"%1\$tA,*/"%1\$td-%1\$tb-%1\$tY",rentFrom )
        rent_to.text = String.format(/*"%1\$tA,*/"%1\$td-%1\$tb-%1\$tY", rentTo)
    }

    fun initListener(listener : PeriodFragmentCallBack){
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState).run {
            window?.attributes?.windowAnimations = R.style.Animation_DialogMessage_Window
            window?.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            window?.attributes?.x = activity?.resources?.getDimensionPixelSize(R.dimen.small_margin)
            window?.attributes?.y = activity?.resources?.getDimensionPixelSize(R.dimen.small_margin)
            return this
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.resources?.let {
            dialog?.window?.setLayout(it.getDimension(R.dimen.dialog_width).toInt() , it.getDimension(R.dimen.dialog_height).toInt())
            dialog?.window?.setGravity(Gravity.TOP)
        }
    }

    interface PeriodFragmentCallBack{
        fun onSelection(period : String)
    }

    companion object {
        @JvmStatic
        fun newInstance(days : ArrayList<Int>) = PeriodFragment().apply {
            arguments = Bundle().apply {
                putIntegerArrayList(DAYS, days)
            }
        }

        val DAYS = "DAYS"
    }
}