package com.example.sarwan.renkar.modules.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.model.User
import com.example.sarwan.renkar.modules.dashboard.DashboardActivity
import com.example.sarwan.renkar.utils.Cities
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.account_fragment.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AccountFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AccountFragment : Fragment(){
    
    private lateinit var pActivity : ParentActivity
    private var cities = Cities.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as DashboardActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.account_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setFields()
        setupSpinners()
        onClickListener()
    }

    private var viewsMap = emptyMap<Int, TextInputEditText>()

    private fun initViews() {
        viewsMap = hashMapOf(first_name_edit.id to first_name,
            last_name_edit.id to last_name,
            phone_no_edit.id to phone_no,
            address_edit.id to address_tv,
            license_edit.id to license)

    }

    private fun onClickListener() {
        first_name_edit.setOnClickListener {
            onEditMode(it as ImageView)
        }

        first_name_edit.setOnClickListener {
            onEditMode(it as ImageView)
        }

        last_name_edit.setOnClickListener {
            onEditMode(it as ImageView)
        }

        phone_no_edit.setOnClickListener {
            onEditMode(it as ImageView)
        }

        license_edit.setOnClickListener {
            onEditMode(it as ImageView)
        }

        address_edit.setOnClickListener {
            onEditMode(it as ImageView)
        }

        update.setOnClickListener {
            if (dataUpdated()){
                updateProfile()
            }
        }
    }

    private fun onEditMode(imageView: ImageView) {
        viewsMap[imageView.id]?.apply {
            isEnabled = !isEnabled
            requestFocus()
            imageView.setImageResource(if (isEnabled) R.drawable.ic_tick else R.drawable.ic_highlighter)
        }
    }

    private fun dataUpdated(): Boolean {
        var validated = false
        pActivity.user?.apply {
            (firstName.toString().contentEquals(first_name?.text.toString())).apply {
                if (this) validated = !this else return !this
            }
            lastName.toString().contentEquals(last_name?.text.toString()).apply {
                if (this) validated = !this else return !this
            }
            phoneNo.toString().contentEquals(phone_no?.text.toString()).apply {
                if (this) validated = !this else return !this
            }
            licenseNo.toString().contentEquals(license?.text.toString()).apply {
                if (this) validated = !this else return !this
            }
            address.toString().contentEquals(address_tv.text.toString()).apply {
                if (this) validated = !this else return !this
            }
        }
        return validated
    }

    private fun updateProfile() {
        pActivity.user?.apply {
            email?.let {
                pActivity.showProgress()
                FirestoreQueryCenter.updateUserInDB(it, makeUserObject(this)).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        fetchUpdatedData(it)
                    }
                }
            }
        }
    }

    private fun makeUserObject(user: User): User {
        user.firstName = first_name?.text?.toString()
        user.lastName = last_name?.text?.toString()
        user.address = address_tv?.text?.toString()
        user.licenseNo = license?.text?.toString()
        user.phoneNo = phone_no?.text?.toString()
        return user
    }

    private fun fetchUpdatedData(email: String) {
        FirestoreQueryCenter.getUser(email).addOnSuccessListener { it ->
            it?.data?.let { data->
                pActivity.user = it.toObject(User::class.java)
                pActivity.user?.isLogin = true
                pActivity.saveUserInSharedPreferences()
                pActivity.hideProgress()
                Toast.makeText(pActivity, getString(R.string.profile_updated), Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setFields() {
        first_name.setText(pActivity.user?.firstName)
        last_name.setText(pActivity.user?.lastName)
        phone_no.setText(pActivity.user?.phoneNo)
        license.setText(pActivity.user?.licenseNo)
        address_tv.setText(pActivity.user?.address)
        email.setText(pActivity.user?.email)
    }


    private fun setupSpinners(){
        val adapter = ArrayAdapter<String>(pActivity, R.layout.spinner_item, R.id.spinnerText, cities)
        city.adapter = adapter
        city.setSelection(0)
        city.onItemSelectedListener = onSpinnerItemSelectedListener
    }

    private val onSpinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            city.setSelection(position)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AboutFragment.
         */
        @JvmStatic
        fun newInstance() = AccountFragment()
    }
}
