package org.blueclub.util

import android.text.InputFilter
import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import org.blueclub.util.extension.getStringLength

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean?) {
    if (isVisible == null) return
    this.isVisible = isVisible
}

@BindingAdapter("maxLen")
fun EditText.cutTextToMaxLength(maxLength: Int) {
    val filterArray = arrayOfNulls<InputFilter>(1)
    addTextChangedListener {
        val str = text.toString()
        val lengthFilter =
            if (str.getStringLength() == maxLength) str.length else Int.MAX_VALUE
        filters = filterArray.apply { this[0] = InputFilter.LengthFilter(lengthFilter) }
    }
}