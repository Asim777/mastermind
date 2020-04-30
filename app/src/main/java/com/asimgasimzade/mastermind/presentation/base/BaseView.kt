package com.asimgasimzade.mastermind.presentation.base

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel

interface BaseView<T : ViewModel> {
    val viewModel: T
    val bindingLayout: Int
}

@BindingAdapter("visible")
@Suppress("UNCHECKED_CAST")
fun setViewVisibleOrGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
