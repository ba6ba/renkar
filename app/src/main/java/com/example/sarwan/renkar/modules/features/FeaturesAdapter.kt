package com.example.sarwan.renkar.modules.features

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.model.Days
import com.example.sarwan.renkar.model.Features
import kotlinx.android.synthetic.main.feature_item_layout.view.*
import kotlin.collections.ArrayList

class FeaturesAdapter(private val activity : FragmentActivity?, private var featuresList: ArrayList<Features>,
                      private var featuresSelected : FeaturesSelected
)
    : androidx.recyclerview.widget.RecyclerView.Adapter<FeaturesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.feature_item_layout, null)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.loadData(featuresList.get(position),position)
    }

    override fun getItemCount(): Int {
        return featuresList.size
    }

    fun swap(records: ArrayList<Features>?) {
        records?.let { it ->
            featuresList.clear()
            featuresList.addAll(it)
            notifyDataSetChanged()
        }
    }


    inner class ViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun loadData(feature: Features, position: Int){
            itemView.featureIcon.background = feature.icon?.let { activity?.resources?.getDrawable(it) }
            itemView.featureName.text = feature.name?.capitalize()
            setOnClickListener(position)
        }

        private fun setOnClickListener(position: Int) {
            itemView.layout.tag = position
            itemView.setOnClickListener {
                changeState(it.tag as Int)
            }
        }

        private fun changeState(pos: Int) {
            featuresList[pos].selected = !featuresList[pos].selected
            changeViewsAccordingly(pos)
        }

        private fun changeViewsAccordingly(pos: Int) {
            if (featuresList[pos].selected){
                itemView.indicator.visibility = View.VISIBLE
                featuresSelected.onSelect(featuresList[pos].name, FeaturesFragment.Action.ADDED)
            }else{
                itemView.indicator.visibility = View.GONE
                featuresSelected.onSelect(featuresList[pos].name, FeaturesFragment.Action.REMOVED)
            }
            notifyDataSetChanged()
        }
    }

    interface FeaturesSelected{
        fun onSelect(selectedFeature : String?, flag : FeaturesFragment.Action)
    }
}
