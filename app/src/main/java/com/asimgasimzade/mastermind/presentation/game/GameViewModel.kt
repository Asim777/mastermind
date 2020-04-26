package com.asimgasimzade.mastermind.presentation.game

import android.app.Application
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.asimgasimzade.mastermind.presentation.base.BaseViewModel
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelInputs
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelOutputs
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface GameViewModelInputs : BaseViewModelInputs {}

interface GameViewModelOutputs : BaseViewModelOutputs {
    fun setupUi(): Observable<Unit>
}

class GameViewModel @Inject constructor(
    application: Application,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(application, schedulerProvider),
    GameViewModelInputs,
    GameViewModelOutputs {

    override val inputs: GameViewModelInputs
        get() = this

    override val outputs: GameViewModelOutputs
        get() = this

    private val setupUi = PublishSubject.create<Unit>()

    override fun onResume() {
       //TODO: Implement game setup
    }

    override fun setupUi(): Observable<Unit> = setupUi.observeOnUiAndHide()
}