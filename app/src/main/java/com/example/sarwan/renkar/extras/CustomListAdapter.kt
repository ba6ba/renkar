package com.example.sarwan.renkar.extras

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.annotation.NonNull
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.model.AutoCompleteModel

import java.util.ArrayList

class CustomListAdapter(private val mContext: Context, private val itemLayout: Int,
                        private var dataList: MutableList<AutoCompleteModel>) : ArrayAdapter<AutoCompleteModel> (mContext, itemLayout, dataList) {

    private val listFilter = ListFilter()
    private var dataListAllItems: ArrayList<AutoCompleteModel>? = null

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): AutoCompleteModel? {
        return dataList[position]
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var localView = view

        if (view == null) {
            localView = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        }

        val strName = view?.findViewById<TextView>(R.id.autoComplete)
        strName?.text = getItem(position)?.title
        return localView?.let { it }?:kotlin.run { View(mContext) }
    }


    fun swap(data: ArrayList<AutoCompleteModel>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun notifyData() {
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return listFilter
    }

    inner class ListFilter : Filter() {
        private val lock = Any()

        override fun performFiltering(prefix: CharSequence?): Filter.FilterResults {
            val results = Filter.FilterResults()
            if (dataListAllItems == null) {
                synchronized(lock) {
                    dataListAllItems = ArrayList<AutoCompleteModel>(dataList)
                }
            }

            if (prefix == null || prefix.isEmpty()) {
                synchronized(lock) {
                    results.values = dataListAllItems
                    dataListAllItems?.let { results.count = it.size }
                }
            }
            else {
                val searchStrLowerCase = prefix.toString().toLowerCase()

                val matchValues = ArrayList<AutoCompleteModel>()

                dataListAllItems?.let {
                    for (dataItem in it) {
                        dataItem.title?.let {string->
                            if (string.toLowerCase().startsWith(searchStrLowerCase)) {
                                matchValues.add(dataItem)
                            }
                        }
                    }

                }

                results.values = matchValues
                results.count = matchValues.size
            }

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults?) {

            dataList = results?.let {
                results.values as ArrayList<AutoCompleteModel>
            } ?:kotlin.run {
                ArrayList<AutoCompleteModel>()
            }
            results?.let {
                if(count>0)
                    notifyDataSetChanged()
                else
                    notifyDataSetInvalidated()
            }
        }

    }
} 