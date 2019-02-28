package com.example.sarwan.renkar.modules.details

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.model.Cars
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.Image
import com.here.android.mpa.common.OnEngineInitListener
import com.here.android.mpa.mapping.MapGesture
import com.here.android.mpa.mapping.MapMarker
import com.here.android.mpa.mapping.SupportMapFragment
import kotlinx.android.synthetic.main.cover_and_map_fragment.*

class CoverAndMapFragment : Fragment() {
    private val DATA_KEY = "DATA_KEY"
    private lateinit var mapFragment : SupportMapFragment
    private lateinit var map : com.here.android.mpa.mapping.Map
    private lateinit var marker: MapMarker
    private lateinit var car : Cars
    private lateinit var pActivity: CarDetailsActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as CarDetailsActivity
        arguments?.let {
            car = it.getSerializable(ApplicationConstants.CAR_DETAILS_KEY) as Cars
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cover_and_map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapInstance()
        setCoverImage()
        clickListener()
    }

    private fun clickListener() {
        pActivity.apply {
            toggle.setOnClickListener {
                when(cover.visibility){
                    View.VISIBLE -> {
                        hide(cover)
                        show(map_layout)
                    }
                    View.GONE -> {
                        show(cover)
                        hide(map_layout)
                    }
                }
            }
        }
    }

    private fun setCoverImage() {
        cover.setImageURI(car.basic.coverImagePath)
    }

    private fun getMapInstance() {
        mapFragment = childFragmentManager.findFragmentById(R.id.mapfragment) as SupportMapFragment
        initMap()
    }

    private fun initMap() {
        car.address?.apply {
            latitude?.let { latitude->
                longitude?.let { longitude->
                    mapFragment.init { error ->
                        if (error == OnEngineInitListener.Error.NONE) {
                            map = mapFragment.map
                            map.setCenter(GeoCoordinate(latitude,longitude), com.here.android.mpa.mapping.Map.Animation.NONE)
                            map.zoomLevel = (map.maxZoomLevel + map.minZoomLevel) / 2
                            mapFragment.mapGesture.addOnGestureListener(object : MapGesture.OnGestureListener.OnGestureListenerAdapter() {
                                override fun onTapEvent(p: PointF?): Boolean {
                                    return false
                                }
                                override fun onLongPressEvent(p: PointF?): Boolean {
                                    val position = map.pixelToGeo(p)
                                    dropMarker(position)
                                    return false
                                }
                            })
                        }
                        else map = com.here.android.mpa.mapping.Map()
                    }
                }
            }
        }
    }

    fun dropMarker(position: GeoCoordinate) {
        if (::marker.isInitialized) {
            map.removeMapObject(marker)
        }
        marker = MapMarker()
        marker.coordinate = position
        map.addMapObject(marker)
        Image().apply {
            setImageResource(R.drawable.car_icon)
            marker.icon = this
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(car: Cars) = CoverAndMapFragment().apply {
            arguments?.apply {
                putSerializable(DATA_KEY, car)
            }
        }
    }
}