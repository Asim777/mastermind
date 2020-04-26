package com.asimgasimzade.mastermind.presentation.menu

import android.app.Application
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.asimgasimzade.mastermind.presentation.base.BaseViewModel
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelInputs
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelOutputs
import javax.inject.Inject

interface MenuViewModelInputs : BaseViewModelInputs {}

interface MenuViewModelOutputs : BaseViewModelOutputs {}

class MenuViewModel @Inject constructor(
    application: Application,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(application, schedulerProvider),
    MenuViewModelInputs,
    MenuViewModelOutputs {

    override val inputs: MenuViewModelInputs
        get() = this

    override val outputs: MenuViewModelOutputs
        get() = this
}