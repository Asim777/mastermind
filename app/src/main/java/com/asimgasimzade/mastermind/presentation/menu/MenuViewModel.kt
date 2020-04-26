package com.asimgasimzade.mastermind.presentation.menu

import android.app.Application
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.asimgasimzade.mastermind.presentation.base.BaseViewModel
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelInputs
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelOutputs
import com.asimgasimzade.mastermind.presentation.menu.MenuViewModel.Companion.Destination
import com.asimgasimzade.mastermind.usecases.GetGameSettingsUseCase
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface MenuViewModelInputs : BaseViewModelInputs {
    fun onNewGameClicked()
    fun onMultiPlayerClicked()
    fun onSettingsClicked()
    fun onHowToPlayClicked()
    fun onExitClicked()
}

interface MenuViewModelOutputs : BaseViewModelOutputs {
    fun navigate(): Observable<Destination>
}

class MenuViewModel @Inject constructor(
    application: Application,
    schedulerProvider: SchedulerProvider,
    private val getGameSettingsUseCase: GetGameSettingsUseCase
) : BaseViewModel(application, schedulerProvider),
    MenuViewModelInputs,
    MenuViewModelOutputs {

    override val inputs: MenuViewModelInputs
        get() = this

    override val outputs: MenuViewModelOutputs
        get() = this

    private val navigate = PublishSubject.create<Destination>()

    override fun onNewGameClicked() {
        navigate.onNext(Destination.NewGame)
    }

    override fun onMultiPlayerClicked() {
        TODO("Not yet implemented")
    }

    override fun onSettingsClicked() {
        TODO("Not yet implemented")
    }

    override fun onHowToPlayClicked() {
        TODO("Not yet implemented")
    }

    override fun onExitClicked() {
        TODO("Not yet implemented")
    }

    override fun navigate(): Observable<Destination> = navigate.observeOnUiAndHide()

    companion object {
        sealed class Destination {
            object NewGame : Destination()
            object MultiPlayer : Destination()
            object Settings : Destination()
            object HowToPlay : Destination()
            object Exit : Destination()
        }
    }
}
