package com.example.sarwan.renkar.modules.cars

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.modules.details.CarDetailsActivity
import kotlinx.android.synthetic.main.all_cars_list_item.view.*
import java.util.*

class AllCarsAdapter(private val activity: ParentActivity,
                     internal var carsList: ArrayList<Cars>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<AllCarsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.all_cars_list_item, null)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.loadData(position)
    }

    override fun getItemCount(): Int {
        return carsList.size
    }


    fun swap(records: ArrayList<Cars>?) {
        records?.let { it ->
            carsList.clear()
            carsList.addAll(it)
            carsList.sortByDescending { it.createdAt }
            carsList.distinctBy { it.number }
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun loadData(position: Int){
            setCoverImage(position)
            setCarDetails(position)
            clickListener(position)
        }

        private fun clickListener(position: Int) {
            itemView.tag = position
            itemView.setOnClickListener {
                activity.openActivityWithFinish(Intent(activity,CarDetailsActivity::class.java)
                    .putExtra(ApplicationConstants.CAR_DETAILS_KEY, carsList[it.tag as Int]).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }

        private fun setCarDetails(position: Int) {
            itemView.car_name.text = carsList[position].basic.name
            itemView.price.text = carsList[position].price
        }

        private fun setCoverImage(position: Int) {
            carsList[position].basic.coverImagePath.let {
                if (it.isNotEmpty())
                    itemView.image.setImageURI(it)
                else
                    itemView.image.setImageURI(ApplicationConstants.DEFAULT_CAR_IMAGE_URL)
            }
        }
    }
}
