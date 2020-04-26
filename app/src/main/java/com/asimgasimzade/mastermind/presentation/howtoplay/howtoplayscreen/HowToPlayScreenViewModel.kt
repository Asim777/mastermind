package com.asimgasimzade.mastermind.presentation.howtoplay.howtoplayscreen

import android.app.Application
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.asimgasimzade.mastermind.presentation.base.BaseViewModel
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelInputs
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelOutputs
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface HowToPlayScreenViewModeInputs : BaseViewModelInputs {}

interface HowToPlayScreenViewModeOutputs : BaseViewModelOutputs {
    fun setupUi(): Observable<Unit>
}

class HowToPlayScreenViewModel @Inject constructor(
    application: Application,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(application, schedulerProvider),
    HowToPlayScreenViewModeInputs,
    HowToPlayScreenViewModeOutputs {

    override val inputs: HowToPlayScreenViewModeInputs
        get() = this

    override val outputs: HowToPlayScreenViewModeOutputs
        get() = this

    private val setupUi = PublishSubject.create<Unit>()

    override fun onResume() {
        //TODO: Implement game setup
    }

    override fun setupUi(): Observable<Unit> = setupUi.observeOnUiAndHide()
}