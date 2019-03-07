package com.example.sarwan.renkar.modules.payment_method

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.firebase.FirebaseExtras
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.model.Cards
import com.example.sarwan.renkar.model.ListerProfile
import com.example.sarwan.renkar.model.PaymentMethods
import com.example.sarwan.renkar.modules.dashboard.DashboardActivity
import com.example.sarwan.renkar.utils.ValidationUtility
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.payment_method_fragment.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PaymentMethodsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PaymentMethodsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class PaymentMethodFragment : Fragment(),
    PaymentMethodCardsAdapter.PaymentMethodListener,
    AllCardsFragment.AllCardsFragmentCallBack {
    
    private var pActivity : ParentActivity? = null
    private var adapter: PaymentMethodCardsAdapter? = null
    private var myCards : ArrayList<PaymentMethods?> = ArrayList()
    private var allCardsFragment : AllCardsFragment? = null
    private var allAvailableCards : ArrayList<Cards> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pActivity = activity as DashboardActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.payment_method_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllCards()
        initializeLayoutView()
        fetchPaymentMethods()
        onClickListener()
    }

    private fun onClickListener() {
        confirm.setOnClickListener {
            if (validateData()){
                addPaymentMethod()
            }
        }
    }

    private fun addPaymentMethod() {
        pActivity?.let {
            it.user?.email?.let {email->
                pActivity?.showProgress()
                FirestoreQueryCenter.addPaymentMethodToListerNode(email, makePaymentMethodObject()).
                        addOnCompleteListener { task->
                            if (task.isSuccessful){
                                paymentMethodAdded()
                            }
                            else {
                                pActivity?.hideProgress()
                            }
                        }
            }

        }
    }

    private fun paymentMethodAdded() {
        pActivity?.hideProgress()
        Toast.makeText(pActivity, getString(R.string.payment_method_added_successfully), Toast.LENGTH_LONG).show()
        hideBottomLayout()
    }

    private fun hideBottomLayout() {
        pActivity?.hide(bottom_view)
    }

    private fun makePaymentMethodObject(): Map<String, Any> {
        return mapOf(FirebaseExtras.HOLDER_NAME to card_holder_layout.editText?.text.toString(),FirebaseExtras.EXPIRY_DATE to expiry_layout.editText?.text.toString()
            ,FirebaseExtras.CCV to ccv_layout.editText?.text.toString(), FirebaseExtras.ID to UUID.randomUUID().toString(),
            FirebaseExtras.NAME to getCardName()
            ,FirebaseExtras.NUMBER to card_number_layout.editText?.text.toString(),FirebaseExtras.CREATED_AT to Calendar.getInstance().time.time)
    }

    private fun getCardName(): String {
        return (selectedCard?.name ?: selectedMethod?.name ?: "" )
    }

    private fun validateData(): Boolean {
        return ValidationUtility.isNotEmptyField(card_holder_layout.editText,card_number_layout.editText,ccv_layout.editText,expiry_layout.editText)
    }

    private fun getAllCards() {
        pActivity?.let {
            allAvailableCards = CardsList.get(it)
        }
    }

    private fun initializeLayoutView() {
        pActivity?.let {
            payment_method_rc_view.layoutManager  = androidx.recyclerview.widget.LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = PaymentMethodCardsAdapter(it, ArrayList(), this)
            payment_method_rc_view.adapter = adapter
        }
    }

    private fun fetchPaymentMethods() {
        try {
            pActivity?.user?.email?.let {
                FirestoreQueryCenter.getPaymentMethods(it).addSnapshotListener(queryListener)
            }
        }catch (e: Exception){
            e.localizedMessage
        }
    }

    private val queryListener = EventListener<DocumentSnapshot> { querySnapshot, fireStoreException ->
        try {
            fireStoreException?.let {
                return@EventListener
            }?:kotlin.run {
                querySnapshot?.let {query->
                    when {
                        adapter?.itemCount!! == 0 -> getPaymentMethod(query.toObject(ListerProfile::class.java))
                    }
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    private fun getPaymentMethod(pMethods: ListerProfile?) {
        pMethods?.let { myCards = pMethods.paymentMethod as ArrayList<PaymentMethods?>}
        myCards.sortByDescending { it?.createdAt }
        adapter?.swap(myCards)
    }


    override fun onCardSelection(position: Int) {
        selectedCard = null
        selectedMethod = myCards[position]
        pActivity?.let {
            showBottomLayout(it, myCards[position])
        }
    }

    override fun itemSelected(tag: String) {
        setNewCardFields(tag)
    }

    private var selectedCard : Cards ? = null
    private var selectedMethod : PaymentMethods ? = null

    private fun setNewCardFields(tag: String) {
        pActivity?.let { activity->
            selectedCard = allAvailableCards.find { it.name==tag }
            selectedMethod = null
            showBottomLayout(activity, selectedCard)
        }
    }


    private fun showBottomLayout(activity: ParentActivity, card: Cards?) {
        card?.name?.let {name->
            setLayoutViews(activity, null, card)
        }
    }


    private fun showBottomLayout(activity: ParentActivity, paymentMethod: PaymentMethods?) {
        paymentMethod?.name?.let {name->
            paymentMethod.apply {
                setLayoutViews(activity, this, null)
            }
        }
    }

    private fun setLayoutViews(
        activity: ParentActivity,
        paymentMethods: PaymentMethods?,
        card: Cards?
    ) {
        pActivity?.show(bottom_view)
        card_icon_main.setImageURI(CardsList.getIcon(activity, paymentMethods?.name?:card?.name?:""))
        card_holder.setText(paymentMethods?.holderName?:"")
        card_number_layout.editText?.setText(paymentMethods?.number?:"")
        expiry.setText(paymentMethods?.expiryDate?:"")
        ccv.setText(paymentMethods?.ccv?:"")
    }

    override fun onEmptyCardSelection() {
        showCardsFragment()
    }


    private fun showCardsFragment() {
        pActivity?.let {
            AllCardsFragment().run {
                allCardsFragment = this
                allCardsFragment?.initCallBack(this@PaymentMethodFragment)
                if (!isAdded)
                    show(createManager(), TAG)
            }
        }
    }

    private fun createManager(): FragmentManager {
        childFragmentManager.beginTransaction().run {
            addToBackStack(null)
        }
        return childFragmentManager
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PaymentMethodFragment.
         */
        @JvmStatic
        fun newInstance() = PaymentMethodFragment()

        val TAG = "PAYMENT_METHOD_FRAGMENT"
        enum class PM_VIEW_TYPE {EMPTY, NON_EMPTY}
    }
}
