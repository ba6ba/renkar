package com.example.sarwan.renkar.modules.lister.listing

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.firebase.FirebaseExtras
import com.example.sarwan.renkar.model.Cars
import com.example.sarwan.renkar.modules.details.CarDetailsActivity
import kotlinx.android.synthetic.main.car_list_item.view.*
import java.util.*

class ListerCarsAdapter(private val activity: ParentActivity,
                          internal var carsList: ArrayList<Cars>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<ListerCarsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.car_list_item, null)
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
            setListedByDetails(position)
            setCoverImage(position)
            setCarDetails(position)
            setPrice(position)
            setRating(position)
            clickListener(position)
        }

        private fun clickListener(position: Int) {
            itemView.tag = position
            itemView.setOnClickListener {
                activity.openActivity(Intent(activity,CarDetailsActivity::class.java)
                    .putExtra(ApplicationConstants.CAR_DETAILS_KEY, carsList[it.tag as Int]))
            }
        }

        private fun setRating(position: Int) {
            itemView.ratingBar.visibility = if (carsList[position].owner.email!=activity.user?.email) View.VISIBLE else View.INVISIBLE
            itemView.ratingBar.rating = carsList[position].rating?.let { it }?:kotlin.run { 3f }
        }

        private fun setCarDetails(position: Int) {
            itemView.car_name.text = carsList[position].basic.name
            itemView.car_model.text = carsList[position].basic.model
            itemView.car_manufacturer.text = carsList[position].basic.manufacturedBy
        }

        private fun setPrice(position: Int) {
            itemView.price.text = "${carsList[position].price} pkr/day"
        }

        private fun setCoverImage(position: Int) {
            carsList[position].basic.coverImagePath.let {
                if (it.isNotEmpty())
                    itemView.coverFrame.setImageURI(it)
                else
                    itemView.coverFrame.setImageURI(ApplicationConstants.DEFAULT_CAR_IMAGE_URL)
            }
        }

        private fun setListedByDetails(position: Int) {
            itemView.personLayout.visibility = if (carsList[position].owner.email==activity.user?.email) View.VISIBLE else View.INVISIBLE
            itemView.person_name.text = carsList[position].owner.name
            (carsList[position].owner.image)?.let { if (it.isNotEmpty()) itemView.person_icon.setImageURI(it)}
        }
    }

    private fun getItem(pos: Int): Cars {
        return carsList[pos]
    }

    fun addItem(value: Cars) {
        carsList.add(value)
        carsList.sortByDescending { it.createdAt }
        notifyDataSetChanged()
    }

    fun updateItem(value: Cars, key: String?) {
        value.number = key
        for(i in 0 until carsList.size){
            if (carsList[i].number.equals(key))
                carsList[i] = value
        }
        carsList.sortByDescending { it.createdAt }
        carsList.distinctBy { it.number }
        notifyDataSetChanged()
    }
}
