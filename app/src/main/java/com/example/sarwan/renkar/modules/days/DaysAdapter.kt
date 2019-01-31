package com.example.sarwan.renkar.modules.days

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.model.Days
import kotlinx.android.synthetic.main.days_list_item.view.*
import kotlin.collections.ArrayList

class DaysAdapter(private val activity : FragmentActivity? , private var daysList: ArrayList<Days>,
                  private var featuresSelected : DaysAdapter.DaysSelected)
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
            itemView.day.text = day.initial.toString()
            itemView.day.backgroundTintList = activity?.resources?.getColorStateList(day.colorResource?.let { it }?:kotlin.run { R.color.black })
            setOnClickListener(position)
        }

        private fun setOnClickListener(position: Int) {
            itemView.day.tag = position
            itemView.setOnClickListener {
                changeState(it.tag as Int)
            }
        }

        private fun changeState(pos: Int) {
            daysList[pos].selected = !daysList[pos].selected
            changeViewsAccordingly(pos)
        }

        private fun changeViewsAccordingly(pos: Int) {
            if (daysList[pos].selected){
                itemView.day.backgroundTintList = activity?.resources?.getColorStateList(R.color.colorAccent)
                featuresSelected.onSelect(daysList[pos].name, DaysFragment.Action.ADDED)
            }else{
                itemView.day.backgroundTintList = activity?.resources?.getColorStateList(R.color.black)
                featuresSelected.onSelect(daysList[pos].name, DaysFragment.Action.REMOVED)
            }
            notifyDataSetChanged()
        }
    }

    interface DaysSelected{
        fun onSelect(selectedDay : String?, flag : DaysFragment.Action)
    }
}
