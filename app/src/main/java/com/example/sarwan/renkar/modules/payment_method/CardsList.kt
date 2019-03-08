package com.example.sarwan.renkar.modules.payment_method

import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.model.Cards

object CardsList {
    fun get(activity: ParentActivity) : ArrayList<Cards> {
        val list = ArrayList<Cards>()
        val card1 = Cards()
        card1.name = activity.resources.getString(R.string.master_card)
        card1.icon = MASTER_CARD
        list.add(card1)
        val card2 = Cards()
        card2.name = activity.resources.getString(R.string.visa)
        card2.icon = VISA
        list.add(card2)
        val card3 = Cards()
        card3.name = activity.resources.getString(R.string.faysal_bank)
        card3.icon = FAYSAL_BANK
        list.add(card3)
        val card4 = Cards()
        card4.name = activity.resources.getString(R.string.bank_al_habib)
        card4.icon = BAHL
        list.add(card4)
        val card5 = Cards()
        card5.name = activity.resources.getString(R.string.ubl)
        card5.icon = UBL
        list.add(card5)
        return list
    }

    const val BAHL = "https://firebasestorage.googleapis.com/v0/b/renkar-android.appspot.com/o/icons%2Fbank_al_habib.png?alt=media&token=88047794-ab11-4a5a-8de4-e4e13ec3e391"
    const val UBL = "https://firebasestorage.googleapis.com/v0/b/renkar-android.appspot.com/o/icons%2Fubl_logo.png?alt=media&token=7c5a6236-d435-4a59-9565-a5eb2115decf"
    const val FAYSAL_BANK = "https://firebasestorage.googleapis.com/v0/b/renkar-android.appspot.com/o/icons%2Ffaysal_bank.png?alt=media&token=ef0b2800-3943-4497-9396-0cb941375a14"
    const val MASTER_CARD = "https://firebasestorage.googleapis.com/v0/b/renkar-android.appspot.com/o/icons%2Fmaster_card_logo.png?alt=media&token=5538fe9f-57b3-45af-809b-804a491deb20"
    const val VISA = "https://firebasestorage.googleapis.com/v0/b/renkar-android.appspot.com/o/icons%2Fvisa.png?alt=media&token=6d82aaa6-2d3f-46d1-abd2-044e3cde0b81"

    fun getIcon(activity: ParentActivity, name : String) : String {
        return map(activity)[name]?.let { it }?:kotlin.run { BAHL }
    }

    fun map(activity: ParentActivity) = hashMapOf(activity.resources.getString(R.string.master_card) to MASTER_CARD,
        activity.resources.getString(R.string.visa) to VISA,
        activity.resources.getString(R.string.faysal_bank) to FAYSAL_BANK,
        activity.resources.getString(R.string.bank_al_habib) to BAHL,
        activity.resources.getString(R.string.ubl) to UBL)
}