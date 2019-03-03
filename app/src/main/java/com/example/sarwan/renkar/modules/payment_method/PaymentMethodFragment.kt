package com.example.sarwan.renkar.modules.payment_method

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.firebase.FirebaseExtras
import com.example.sarwan.renkar.firebase.FirestoreQueryCenter
import com.example.sarwan.renkar.model.Cards
import com.example.sarwan.renkar.model.PaymentMethods
import com.example.sarwan.renkar.modules.dashboard.DashboardActivity
import com.example.sarwan.renkar.utils.ValidationUtility
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.payment_method_fragment.*


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
                FirestoreQueryCenter.addPaymentMethodToListerNode(email, makePaymentMethodObject())
            }

        }
    }

    private fun makePaymentMethodObject(): Any {
        val obj = PaymentMethods()
        obj.ccv = ccv.text.toString()
        obj.expiryDate = expiry.text.toString()
        obj.number = card_number.text.toString()
        obj.holderName = card_holder.text.toString()
        obj.name = selectedCard?.name ?:kotlin.run { selectedMethod?.name }
        return obj
    }

    private fun validateData(): Boolean {
        return ValidationUtility.isNotEmptyField(card_holder,card_number,ccv,expiry)
    }

    private fun getAllCards() {
        pActivity?.let {
            allAvailableCards = CardsList.get(it)
        }
    }


    private fun initializeLayoutView() {
        pActivity?.let {
            payment_method_rc_view.layoutManager  = androidx.recyclerview.widget.LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = PaymentMethodCardsAdapter(it, myCards, this)
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
                        adapter?.itemCount!! == 0 -> getPaymentMethod(query[FirebaseExtras.PAYMENT_METHOD] as ArrayList<PaymentMethods>?)
                    }
                }
            }
        }catch (e : Exception){
            e.localizedMessage
        }
    }

    private fun getPaymentMethod(pMethods: MutableList<PaymentMethods>?) {
        pMethods?.let { myCards = it as ArrayList<PaymentMethods?> }
        adapter?.swap(myCards)
    }


    override fun onCardSelection(position: Int) {
        selectedCard = null
        selectedMethod = myCards[position]
        pActivity?.let {
            showBottomLayout(it, myCards[position])
        }
    }

    override fun itemSelected(position: Int) {
        setNewCardFields(position)
    }

    private var selectedCard : Cards ? = null
    private var selectedMethod : PaymentMethods ? = null

    private fun setNewCardFields(position: Int) {
        pActivity?.let { activity->
            selectedCard = allAvailableCards[position]
            selectedMethod = null
            showBottomLayout(activity, allAvailableCards[position])
        }
    }


    private fun showBottomLayout(activity: ParentActivity, card: Cards) {
        card.name?.let {name->
            pActivity?.show(bottom_view)
            card_icon.background = activity.resources.getDrawable(
                CardsList.getIcon(
                    activity,
                    name
                )
            )
        }
    }


    private fun showBottomLayout(activity: ParentActivity, paymentMethod: PaymentMethods?) {
        paymentMethod?.name?.let {name->
            pActivity?.show(bottom_view)
            card_icon.background = activity.resources.getDrawable(
                CardsList.getIcon(
                    activity,
                    name
                )
            )
            card_holder.setText(paymentMethod.holderName)
            card_number.setText(paymentMethod.number)
            expiry.setText(paymentMethod.expiryDate)
            ccv.setText(paymentMethod.ccv)
        }
    }

    override fun onEmptyCardSelection() {
        showAllCards()
    }


    private fun showAllCards() {
        pActivity?.let {
            AllCardsFragment.newInstance(allAvailableCards).run {
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
