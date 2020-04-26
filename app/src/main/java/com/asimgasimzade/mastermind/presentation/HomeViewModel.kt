package com.asimgasimzade.mastermind.presentation

import android.app.Application
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.asimgasimzade.mastermind.presentation.base.BaseViewModel
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelInputs
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelOutputs
import javax.inject.Inject

interface HomeViewModelInputs : BaseViewModelInputs

interface HomeViewModelOutputs : BaseViewModelOutputs

open class HomeViewModel @Inject constructor(
    application: Application,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(application, schedulerProvider), HomeViewModelInputs, HomeViewModelOutputs {

    override val inputs: HomeViewModelInputs
        get() = this

    override val outputs: HomeViewModelOutputs
        get() = this
}