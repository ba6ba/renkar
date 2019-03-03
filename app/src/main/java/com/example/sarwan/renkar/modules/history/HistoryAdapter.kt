package com.example.sarwan.renkar.modules.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.model.History
import com.example.sarwan.renkar.utils.RandomUtility
import kotlinx.android.synthetic.main.history_list_item.view.*
import java.util.*

class HistoryAdapter(private val activity: ParentActivity,
                     internal var historyList: ArrayList<History>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.history_list_item, null)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.loadData(position)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }


    fun swap(records: ArrayList<History>?) {
        records?.let { it ->
            historyList.clear()
            historyList.addAll(it)
            historyList.sortByDescending { it.createdAt }
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun loadData(position: Int){
            setData(position)
            clickListener(position)
        }

        private fun setData(position: Int) {
            itemView.name.text = historyList[position].name
            itemView.details.text = historyList[position].details
            itemView.duration.text = historyList[position].period
            itemView.renter.text = historyList[position].rentedBy
            itemView.ground.setBackgroundColor(RandomUtility.getRandomDarkColor())
            itemView.status.background = map[historyList[position].status]?.let { activity.resources.getDrawable(it) }
        }

        private fun clickListener(position: Int) {
            itemView.tag = position
            itemView.setOnClickListener {

            }
        }
    }

    val map = hashMapOf("done" to R.drawable.green_status_round_corner_bg, "in-progress" to R.drawable.red_status_round_corner_bg)

}
