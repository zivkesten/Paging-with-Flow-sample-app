package com.zk.testapp.presentation.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.onTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(editable: Editable?) {}

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, after: Int) {
            afterTextChanged.invoke(text.toString())
        }
    })
}