package com.example.sarwan.renkar.modules.lister.listing

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.model.Cars
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
        }

        private fun setRating(position: Int) {
            itemView.ratingBar.visibility = if (carsList[position].carOwner.email!=activity.user?.email) View.VISIBLE else View.GONE
            itemView.ratingBar.rating = carsList[position].rating?.let { it }?:kotlin.run { 3f }
        }

        private fun setCarDetails(position: Int) {
            itemView.car_name.text = carsList[position].carBasic.name
            itemView.car_model.text = carsList[position].carBasic.model
            itemView.car_manufacturer.text = carsList[position].carBasic.manufacturedBy
        }


        private fun setPrice(position: Int) {
            itemView.price.text = "${carsList[position].carPrice.listerAmount} pkr/day"
        }

        private fun setCoverImage(position: Int) {
            itemView.cover.setImageURI(carsList[position].carBasic.coverImagePath)
        }

        private fun setListedByDetails(position: Int) {
            itemView.personLayout.visibility = if (carsList[position].carOwner.email!=activity.user?.email) View.VISIBLE else View.GONE
            itemView.person_name.text = carsList[position].carOwner.name
        }

        fun makeContactIcon(textView: TextView, text: String?){
            var iconName = ""

            try {
                text?.let {
                    iconName = text.get(0).toString()
                    if (text.split(" ").size > 1)
                        iconName += text.split(" ").get(1).get(0)
                }
            } catch (e: Exception)
            {
                Log.d("makeContactIcon", e.message)
            }
            textView.text = iconName
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
        value.carBasic.number = key
        for(i in 0 until carsList.size){
            if (carsList[i].carBasic.number.equals(key))
                carsList[i] = value
        }
        carsList.sortByDescending { it.createdAt }
        notifyDataSetChanged()
    }
}
