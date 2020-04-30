package com.asimgasimzade.mastermind.data.model

data class DialogModel(
    val drawableResource: Int,
    val titleResource: Int,
    val titleColorResource: Int,
    val textResource: Int,
    val showLeaveButton: Boolean,
    val showPlayAgainButton: Boolean,
    val showCloseButton: Boolean
)