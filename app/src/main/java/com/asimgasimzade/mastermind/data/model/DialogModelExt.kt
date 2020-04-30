package com.asimgasimzade.mastermind.data.model

import com.asimgasimzade.mastermind.R

fun getWinDialogModel() = DialogModel(
    drawableResource = R.drawable.ic_dialog_win,
    titleResource = R.string.win_dialog_title,
    titleColorResource = R.color.green,
    textResource = R.string.win_dialog_text,
    showLeaveButton = true,
    showPlayAgainButton = true,
    showCloseButton = false
)

fun getLostDialogModel() = DialogModel(
    drawableResource = R.drawable.ic_dialog_lost,
    titleResource = R.string.lost_dialog_title,
    titleColorResource = R.color.red,
    textResource = R.string.lost_dialog_text,
    showLeaveButton = true,
    showPlayAgainButton = true,
    showCloseButton = false
)

fun getCodeMakerDialogModel() = DialogModel(
    drawableResource = R.drawable.ic_dialog_code_maker_turn,
    titleResource = R.string.code_maker_dialog_title,
    titleColorResource = R.color.green,
    textResource = R.string.code_maker_dialog_text,
    showLeaveButton = false,
    showPlayAgainButton = false,
    showCloseButton = true
)

fun getCodeBreakerDialogModel() = DialogModel(
    drawableResource = R.drawable.ic_dialog_code_breaker_turn,
    titleResource = R.string.code_breaker_dialog_title,
    titleColorResource = R.color.green,
    textResource = R.string.code_breaker_dialog_text,
    showLeaveButton = false,
    showPlayAgainButton = false,
    showCloseButton = true
)