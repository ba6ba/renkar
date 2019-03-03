package com.example.sarwan.renkar.modules.payment_method

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.model.Cards
import kotlinx.android.synthetic.main.available_cards_item_layout.view.*

class CardsAdapter(val activity : ParentActivity, var cardsList: List<Cards>?) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v  = LayoutInflater.from(parent?.context).inflate(R.layout.available_cards_item_layout, null)
        cardsList?.let {
            v.icon.background = activity.resources.getDrawable(R.drawable.visa)/*getItem(position)?.icon?.let { dr->activity.resources.getDrawable(dr) }*/
            v.name.text = getItem(position)?.name
        }
        return v
    }

    override fun getItem(position: Int): Cards? {
        cardsList?.let {
            return it[position]
        }

        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        cardsList?.let {
            return it.size
        }
        return 0
    }

}
