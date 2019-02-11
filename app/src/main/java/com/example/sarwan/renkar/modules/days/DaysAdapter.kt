package com.example.sarwan.renkar.modules.days

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.model.Days
import com.example.sarwan.renkar.utils.StringUtility
import kotlinx.android.synthetic.main.days_list_item.view.*
import kotlin.collections.ArrayList

class DaysAdapter(private val activity : FragmentActivity? , private var daysList: ArrayList<Days>,
                  private var fragment : DayFragment)
    : androidx.recyclerview.widget.RecyclerView.Adapter<DaysAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysAdapter.ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.days_list_item, null)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: DaysAdapter.ViewHolder, position: Int) {
        holder.loadData(daysList[position],position)
    }

    override fun getItemCount(): Int {
        return daysList.size
    }

    fun swap(records: ArrayList<Days>?) {
        records?.let { it ->
            daysList.clear()
            daysList.addAll(it)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun loadData(day: Days, position: Int){
            itemView.day.text = StringUtility.getFirstTwoChars(day.name)
            if (day.selected) itemView.day.backgroundTintList = activity?.resources?.getColorStateList(R.color.colorAccent)
            else itemView.day.backgroundTintList = activity?.resources?.getColorStateList(R.color.dark_grey)
            setOnClickListener(position)
        }

        private fun setOnClickListener(position: Int) {
            itemView.tag = position
            itemView.setOnClickListener {
                changeState(it.tag as Int)
            }
        }

        private fun changeState(pos: Int) {
            for (i in daysList.indices){
                daysList[i].selected.run {
                    if (i==pos)
                        daysList[i].selected = !daysList[i].selected
                }
            }
            notifyDataSetChanged()
            changeViewsAccordingly(pos)
        }

        private fun changeViewsAccordingly(pos: Int) {
            if (daysList[pos].selected){
                fragment.interactionListener?.onSelect(daysList[pos].name, DayFragment.Action.ADDED)
            }else{
                fragment.interactionListener?.onSelect(daysList[pos].name, DayFragment.Action.REMOVED)
            }

        }
    }
}
