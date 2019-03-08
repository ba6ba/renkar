package com.example.sarwan.renkar.modules.payment_method

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.model.Cards
import com.example.sarwan.renkar.modules.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.card_services_layout.*

class AllCardsFragment : DialogFragment() {

    private lateinit var pActivity : DashboardActivity
    private lateinit var listener : AllCardsFragmentCallBack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as DashboardActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.card_services_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogConfiguration()
        setImages()
        onClickListener()
    }

    private fun setImages() {
        faysal_bank.setImageURI(CardsList.FAYSAL_BANK)
        ubl.setImageURI(CardsList.UBL)
        bank_al_habib.setImageURI(CardsList.BAHL)
        master_card.setImageURI(CardsList.MASTER_CARD)
        visa.setImageURI(CardsList.VISA)
    }

    private fun dialogConfiguration() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    fun initCallBack(callBack: AllCardsFragmentCallBack){
        listener = callBack
    }

    private fun onClickListener() {
        faysal_bank.apply {
            setOnClickListener {
                actionOnClick(tag)
            }
        }

        ubl.apply {
            setOnClickListener {
                actionOnClick(tag)
            }
        }

        bank_al_habib.apply {
            setOnClickListener {
                actionOnClick(tag)
            }
        }

        visa.apply {
            setOnClickListener {
                actionOnClick(tag)
            }
        }

        master_card.apply {
            setOnClickListener {
                actionOnClick(tag)
            }
        }
    }

    private fun actionOnClick(tag: Any?) {
        listener.itemSelected(tag as String)
        dismissAllowingStateLoss()
    }


    override fun onResume() {
        super.onResume()
        activity?.resources?.let {
            dialog?.window?.setLayout(it.getDimension(R.dimen.dialog_width_large).toInt() , it.getDimension(R.dimen.dialog_height_large).toInt())
            dialog?.window?.setGravity(Gravity.CENTER)
        }
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
    companion object {
        @JvmStatic
        fun newInstance() = AllCardsFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    interface AllCardsFragmentCallBack{
        fun itemSelected(tag : String)
    }

}