package com.example.sarwan.renkar.modules.lister.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.CustomListAdapter
import com.example.sarwan.renkar.model.AutoCompleteModel
import com.example.sarwan.renkar.model.MAPBOX.Addresses
import com.example.sarwan.renkar.model.MAPBOX.Feature
import com.example.sarwan.renkar.model.TPL.Locations
import com.example.sarwan.renkar.network.NetworkConstants
import com.example.sarwan.renkar.network.RestClient
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.add_car_step_three.*
import kotlinx.android.synthetic.main.lister_add_car_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContactFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
open class ListerAddCarBaseFragment : Fragment(){

    protected var onStep : Int = 1
    private var addressCount = 2
    private var addressList: ArrayList<com.example.sarwan.renkar.model.location.Address?> = ArrayList()
    private var mapBoxAddresses: Addresses ? = null
    protected var adapter: CustomListAdapter? = null
    protected var autoCompleteModelList : ArrayList<AutoCompleteModel> = ArrayList()

    protected lateinit var pActivity : ParentActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as ParentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.lister_add_car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    protected fun layoutTransitionOnNextButton() {
        when(onStep){
            1-> {
                step_one.visibility = View.GONE
                step_two.visibility = View.VISIBLE
                previous.visibility = View.VISIBLE
                onStep+=1
            }
            2-> {
                step_two.visibility = View.GONE
                step_three.visibility = View.VISIBLE
                next.setImageResource(R.drawable.ic_check_black_24dp)
                onStep+=1
            }
            3-> {
                showSummaryDialog()
            }
        }
    }


    protected fun layoutTransitionOnPreviousButton() {
        when(onStep){
            2-> {
                step_two.visibility = View.GONE
                step_one.visibility = View.VISIBLE
                previous.visibility = View.GONE
                onStep=-1
            }
            3-> {
                step_two.visibility = View.VISIBLE
                step_three.visibility = View.GONE
                next.setImageResource(R.drawable.next)
                onStep-=1
            }
        }
    }

    private fun initAddressView(){
        adapter = CustomListAdapter(pActivity, R.layout.auto_complete_list_item, autoCompleteModelList)
        address_view.setAdapter(adapter)
        adapter?.notifyData()
    }

    protected val autoCompleteItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, l ->
        val selectedPlace = (adapterView.getItemAtPosition(pos) as AutoCompleteModel).id?.let {
            mapBoxAddresses?.features?.find { featuresId-> featuresId.id == it }
        }
        getAddress(selectedPlace)
        address_view.setText("")
    }

    private fun getAddress(selectedPlace: Feature?) {
        val address = com.example.sarwan.renkar.model.location.Address()
        selectedPlace?.let {
            it.place_name?.let { title-> address.address = title
                addAddressChip(title)
            }
            (it.geometry?.coordinates?.
                let { geometry-> geometry } ?:kotlin.run { null })?.let {
                coordinates->
                address.longitude = coordinates[0]
                address.latitude = coordinates[1]
            }
            address.id = addressCount
        }
        addressList.add(address)
    }

    private fun addAddressChip(title: String) {
        val chip = Chip(context)
        chip.text = title.capitalize()
        chip.isCloseIconEnabled = true
        chip.setTextColor(resources.getColor(R.color.darkest_grey))
        chip.chipBackgroundColor = resources.getColorStateList(R.color.light_grey)
        chip.closeIconTint = context?.resources?.getColorStateList(R.color.darkest_grey)
        chip.chipStrokeColor = resources.getColorStateList(R.color.darkest_grey)
        chip.chipStrokeWidth = 1f

        // necessary to get single selection working
        chip.isClickable = true
        chip.isCheckable = false
        chip.tag = addressList.size
        address_chip_group.addView(chip as View)
        chip.setOnCloseIconClickListener {
            val chipItem = it as Chip
            address_chip_group.removeView(chipItem)
            removeAddressFromList(chipItem.text.toString())
        }
    }

    private fun removeAddressFromList(title: String) {
        addressList.remove(addressList.find { it?.address == title })
    }

    protected fun queryForAddress(text : String){
        pActivity.showProgress()
        RestClient.getRestAdapter(NetworkConstants.TPL_MAPS_BASE_URL).getAddresses(text).enqueue(object :
            Callback<ArrayList<Locations>>{
            override fun onFailure(call: Call<ArrayList<Locations>>?, t: Throwable) {
                pActivity.hideProgress()
                Log.d(TAG, t.localizedMessage)
            }

            override fun onResponse(call: Call<ArrayList<Locations>>?, response: Response<ArrayList<Locations>>) {
                pActivity.hideProgress()
                response.body()?.let {
                    makeAutoCompleteObject(it)
                }
            }
        })
    }

    private fun makeAutoCompleteObject(addresses: ArrayList<Locations>){
        autoCompleteModelList.clear()
        addresses.let {
            for (i in it){
                val model = AutoCompleteModel()
                model.title = i.name
                model.id = i.subcat_name
                autoCompleteModelList.add(model)
            }
        }
        initAddressView()
    }

    protected fun showSummaryDialog() {

    }

    companion object {
        const val TAG = "Lister Add Fragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ListerProfileFragment.
         */
        @JvmStatic
        fun newInstance() = ListerAddCarBaseFragment()
    }
}
