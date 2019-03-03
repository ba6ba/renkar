package com.example.sarwan.renkar.modules.payment_method

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.model.Cards
import com.example.sarwan.renkar.modules.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.card_services_layout.*

class AllCardsFragment : DialogFragment() {

    val CARDS_DIALOG_TYPE = "CARDS_DIALOG_TYPE"
    var cardsList : ArrayList<Cards> ? = null
    private lateinit var pActivity : DashboardActivity
    private lateinit var listener : AllCardsFragmentCallBack
    private var cardsAdapter: CardsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as DashboardActivity
        arguments?.let {
            cardsList = it.getSerializable(CARDS_DIALOG_TYPE) as ArrayList<Cards>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.card_services_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogConfiguration()
        loadStatusList()
    }

    private fun dialogConfiguration() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    fun initCallBack(callBack: AllCardsFragmentCallBack){
        listener = callBack
    }

    private fun loadStatusList() {
        cardsAdapter = CardsAdapter(pActivity, cardsList)
        cards_list_view.adapter = cardsAdapter
        cards_list_view.onItemClickListener = onItemClickListener
    }

    private val onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
        listener.itemSelected(position)
    }

    override fun onResume() {
        super.onResume()
        activity?.resources?.let {
            dialog?.window?.setLayout(it.getDimension(R.dimen.dialog_width).toInt() , it.getDimension(R.dimen.dialog_height).toInt())
            dialog?.window?.setGravity(Gravity.CENTER)
        }
    }

    interface AllCardsFragmentCallBack{
        fun itemSelected(position : Int)
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
        fun newInstance(list : ArrayList<Cards>) = AllCardsFragment().apply {
            arguments = Bundle().apply {
                putSerializable(CARDS_DIALOG_TYPE,list)
            }
        }
    }
}