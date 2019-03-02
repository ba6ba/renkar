package com.example.sarwan.renkar.modules.features

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.model.Features
import kotlinx.android.synthetic.main.feature_item_layout.view.*
import kotlin.collections.ArrayList

class FeaturesAdapter(private val activity : FragmentActivity?, private var featuresList: ArrayList<Features>,
                      private var fragment : FeaturesFragment, private val switchLayout : Boolean
)
    : androidx.recyclerview.widget.RecyclerView.Adapter<FeaturesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (switchLayout)
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.feature_item_layout_vertical, null))
        else
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.feature_item_layout, null))
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
            selectedAppearance(feature.selected)
            disableClickIfRequired()
            setOnClickListener(position)
        }

        private fun disableClickIfRequired() {
            itemView.isClickable = !switchLayout
            itemView.isEnabled = !switchLayout
        }

        private fun selectedAppearance(selected: Boolean) {
            if (selected){
                activity?.resources?.getColor(R.color.colorAccent)?.let {
                    itemView.featureName.setTextColor(it)
                }
                activity?.resources?.getColorStateList(R.color.colorAccent)?.let {
                    itemView.featureIcon.backgroundTintList = it
                }
            }else {
                activity?.resources?.getColor(R.color.dark_grey)?.let {
                    itemView.featureName.setTextColor(it)
                }
                activity?.resources?.getColorStateList(R.color.dark_grey)?.let {
                    itemView.featureIcon.backgroundTintList = it
                }
            }
        }

        private fun setOnClickListener(position: Int) {
            itemView.tag = position
            itemView.setOnClickListener {
                changeState(it.tag as Int)
            }
        }

        private fun changeState(pos: Int) {
            for (i in featuresList.indices){
                featuresList[i].selected.run {
                    if (i==pos)
                        featuresList[i].selected = !featuresList[i].selected
                }
            }
            notifyDataSetChanged()
            changeViewsAccordingly(pos)
        }

        private fun changeViewsAccordingly(pos: Int) {
            if (featuresList[pos].selected){
                fragment.interactionListener?.onSelect(featuresList[pos],
                    FeaturesFragment.Action.ADDED
                )
            }else{
                fragment.interactionListener?.onSelect(featuresList[pos],
                    FeaturesFragment.Action.REMOVED
                )
            }

        }
    }
}
