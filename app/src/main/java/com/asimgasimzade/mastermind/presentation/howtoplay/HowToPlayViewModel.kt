package com.asimgasimzade.mastermind.presentation.howtoplay

import android.app.Application
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.asimgasimzade.mastermind.presentation.base.BaseViewModel
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelInputs
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelOutputs
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface HowToPlayViewModeInputs : BaseViewModelInputs {}

interface HowToPlayViewModeOutputs : BaseViewModelOutputs {
    fun setupUi(): Observable<Unit>
}

class HowToPlayViewModel @Inject constructor(
    application: Application,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(application, schedulerProvider),
    HowToPlayViewModeInputs,
    HowToPlayViewModeOutputs {

    override val inputs: HowToPlayViewModeInputs
        get() = this

    override val outputs: HowToPlayViewModeOutputs
        get() = this

    private val setupUi = PublishSubject.create<Unit>()

    override fun onResume() {
        //TODO: Implement game setup
    }

    override fun setupUi(): Observable<Unit> = setupUi.observeOnUiAndHide()
}