package com.konstantinisaev.youtrack.ui.base.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

inline fun TextView.afterTextChanged(crossinline action: (String) -> Unit) {
  addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(editable: Editable) = action.invoke(editable.toString())
    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) = Unit
  })
}
