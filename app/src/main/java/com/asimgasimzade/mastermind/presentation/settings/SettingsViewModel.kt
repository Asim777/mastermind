package com.asimgasimzade.mastermind.presentation.settings

import android.app.Application
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.asimgasimzade.mastermind.presentation.base.BaseViewModel
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelInputs
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelOutputs
import javax.inject.Inject

interface SettingsViewModelInputs : BaseViewModelInputs {}

interface SettingsViewModelOutputs : BaseViewModelOutputs {}

class SettingsViewModel @Inject constructor(
    application: Application,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(application, schedulerProvider),
    SettingsViewModelInputs,
    SettingsViewModelOutputs {

    override val inputs: SettingsViewModelInputs
        get() = this

    override val outputs: SettingsViewModelOutputs
        get() = this
}