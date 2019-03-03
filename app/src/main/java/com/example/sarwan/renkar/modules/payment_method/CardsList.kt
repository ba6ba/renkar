package com.example.sarwan.renkar.modules.payment_method

import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.model.Cards

object CardsList {
    fun get(activity: ParentActivity) : ArrayList<Cards> {
        val list = ArrayList<Cards>()
        val card1 = Cards()
        card1.name = activity.resources.getString(R.string.master_card)
        card1.icon = R.drawable.master_card_logo
        list.add(card1)
        val card2 = Cards()
        card2.name = activity.resources.getString(R.string.visa)
        card2.icon = R.drawable.visa
        list.add(card2)
        val card3 = Cards()
        card3.name = activity.resources.getString(R.string.faysal_bank)
        card3.icon = R.drawable.faysal_bank
        list.add(card3)
        val card4 = Cards()
        card4.name = activity.resources.getString(R.string.bank_al_habib)
        card4.icon = R.drawable.bank_al_habib
        list.add(card4)
        val card5 = Cards()
        card5.name = activity.resources.getString(R.string.ubl)
        card5.icon = R.drawable.master_card_logo
        list.add(card5)
        return list
    }

    fun getIcon(activity: ParentActivity, name : String) : Int {
        return map(activity)[name]?.let { it }?:kotlin.run { R.drawable.master_card_logo }
    }

    fun map(activity: ParentActivity) = hashMapOf(activity.resources.getString(R.string.master_card) to R.drawable.master_card_logo,
        activity.resources.getString(R.string.visa) to R.drawable.visa,
        activity.resources.getString(R.string.faysal_bank) to R.drawable.faysal_bank,
        activity.resources.getString(R.string.bank_al_habib) to R.drawable.bank_al_habib,
        activity.resources.getString(R.string.ubl) to R.drawable.master_card_logo)
}