package com.example.sarwan.renkar.modules.payment_method

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.model.PaymentMethods
import kotlinx.android.synthetic.main.payment_card_empty_item.view.*
import kotlinx.android.synthetic.main.payment_method_card_item.view.*
import java.util.*

class PaymentMethodCardsAdapter(private val activity: ParentActivity,
                                private val cardsList: ArrayList<PaymentMethods?>,
                                val listener : PaymentMethodListener
)
    : androidx.recyclerview.widget.RecyclerView.Adapter<PaymentMethodCardsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            PaymentMethodFragment.Companion.PM_VIEW_TYPE.EMPTY.ordinal->{
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.payment_card_empty_item, null))
            }
            PaymentMethodFragment.Companion.PM_VIEW_TYPE.NON_EMPTY.ordinal->{
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.payment_method_card_item, null))
            }
            else->{
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.payment_method_card_item, null))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder.itemViewType){
            PaymentMethodFragment.Companion.PM_VIEW_TYPE.EMPTY.ordinal->{
                holder.loadEmptyData(position)
            }
            PaymentMethodFragment.Companion.PM_VIEW_TYPE.NON_EMPTY.ordinal->{
                holder.loadCardData(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return cardsList[position]?.let { PaymentMethodFragment.Companion.PM_VIEW_TYPE.NON_EMPTY.ordinal }
            ?:kotlin.run { PaymentMethodFragment.Companion.PM_VIEW_TYPE.EMPTY.ordinal }
    }

    override fun getItemCount(): Int {
        return cardsList.size
    }


    fun swap(records: ArrayList<PaymentMethods?>?) {
        records?.let { it ->
            cardsList.clear()
            cardsList.addAll(it)
            cardsList.sortByDescending { it?.createdAt }
            addNullItem()
        }
    }

    private fun addNullItem() {
        cardsList.add(null)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun loadCardData(position: Int){
            setData(position)
            clickListener(position)
        }

        fun loadEmptyData(position: Int){
            emptyClickListener(position)
        }

        private fun emptyClickListener(position: Int) {
            itemView.empty_layout.setOnClickListener {
                listener.onEmptyCardSelection()
            }
        }

        private fun setData(position: Int) {
            cardsList[position]?.name?.let { name-> itemView.card_icon.setImageURI(CardsList.getIcon(activity, name))}
            itemView.card_name.text = cardsList[position]?.name
            itemView.card_number.text = cardsList[position]?.number
            (cardsList[position]?.active)?.let { itemView.active_card.visibility = if (it) View.VISIBLE else View.GONE }
        }

        private fun clickListener(position: Int) {
            itemView.tag = position
            itemView.setOnClickListener {
                listener.onCardSelection(it.tag as Int)
            }

            itemView.active_card.tag = position
            itemView.active_card.setOnClickListener {
                makeCardActive(it.tag as Int)
            }
        }
    }

    private fun makeCardActive(position: Int) {
        for (i in cardsList.indices){
            cardsList[i]?.active = i==position
        }
        notifyDataSetChanged()
    }

    interface PaymentMethodListener{
        fun onCardSelection(position: Int)
        fun onEmptyCardSelection()
    }
}
