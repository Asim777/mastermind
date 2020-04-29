package com.asimgasimzade.mastermind.data.model

import android.graphics.drawable.Drawable

data class DialogModel(
    val drawableResource: Drawable?,
    val title: String,
    val titleColor: Int,
    val text: String
)