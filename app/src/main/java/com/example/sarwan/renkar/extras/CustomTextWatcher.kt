package com.example.sarwan.renkar.extras

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

abstract class CustomTextWatcher(var view: Any, var listener: TextWatcherListener) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        listener.onChanged(view.toString() , s,start,before,count)
    }

    interface TextWatcherListener{
        fun onChanged(view: String , s: CharSequence?, start: Int, before: Int, count: Int)
    }
}