package com.example.sarwan.renkar.modules.features

import android.app.Activity
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.model.Features
import kotlin.collections.ArrayList


class FeaturesData(){
    companion object {

        fun populateFeatures(activity: Activity) : ArrayList<Features>{
            val features :ArrayList<Features> = ArrayList()
            val feature1 = Features()
            feature1.id = 1
            feature1.name = activity.resources.getString(R.string.air_conditioned)
            feature1.icon = R.drawable.ic_aircondition
            features.add(feature1)
            val feature2 = Features()
            feature2.id = 2
            feature2.name = activity.resources.getString(R.string.alloy_rim)
            feature2.icon = R.drawable.ic_alloy_rim
            features.add(feature2)
            val feature3 = Features()
            feature3.id = 3
            feature3.name = activity.resources.getString(R.string.auto)
            feature3.icon = R.drawable.ic_auto
            features.add(feature3)
            val feature4 = Features()
            feature4.id = 4
            feature4.name = activity.resources.getString(R.string.convertible)
            feature4.icon = R.drawable.ic_convertible
            features.add(feature4)
            val feature5 = Features()
            feature5.id = 5
            feature5.name = activity.resources.getString(R.string.blue_tooth)
            feature5.icon = R.drawable.ic_blueetooth_logo
            features.add(feature5)
            val feature6 = Features()
            feature6.id = 6
            feature6.name = activity.resources.getString(R.string.gps)
            feature6.icon = R.drawable.ic_gps
            features.add(feature6)
            return features
        }
    }
}