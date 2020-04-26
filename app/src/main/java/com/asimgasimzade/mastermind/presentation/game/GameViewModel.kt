package com.asimgasimzade.mastermind.presentation.game

import android.app.Application
import com.asimgasimzade.mastermind.data.model.*
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.asimgasimzade.mastermind.presentation.base.BaseViewModel
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelInputs
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelOutputs
import com.asimgasimzade.mastermind.usecases.GenerateSecretUseCase
import com.asimgasimzade.mastermind.usecases.GetGameSettingsUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

interface GameViewModelInputs : BaseViewModelInputs {
    fun onLoad(gameMode: GameMode)
}

interface GameViewModelOutputs : BaseViewModelOutputs {
    fun setupUi(): Observable<GameModel>
}

class GameViewModel @Inject constructor(
    application: Application,
    schedulerProvider: SchedulerProvider,
    private val getGameSettingsUseCase: GetGameSettingsUseCase,
    private val generateSecretUseCase: GenerateSecretUseCase
) : BaseViewModel(application, schedulerProvider),
    GameViewModelInputs,
    GameViewModelOutputs {

    override val inputs: GameViewModelInputs
        get() = this

    override val outputs: GameViewModelOutputs
        get() = this

    private val setupUi = PublishSubject.create<GameModel>()

    override fun onLoad(gameMode: GameMode) {

        getGameSettingsUseCase.execute()
            .doOnSubscribe { refreshing.onNext(true) }
            .doFinally { refreshing.onNext(false) }
            .compose(schedulerProvider.doOnIoObserveOnMainSingle())
            .subscribe({ settings ->
                val game = setupGame(gameMode, settings)
                setupUi.onNext(game)
            }, {
                Timber.d("Error while retrieving game settings. ${it.printStackTrace()}")
            }).addTo(subscriptions)

    }

    private fun setupGame(gameMode: GameMode, settings: GameSettings): GameModel {
        val numberOfGuesses = when (settings.level) {
            GameLevel.EASY -> 15
            GameLevel.MEDIUM -> 10
            GameLevel.HARD -> 8
        }

        val guesses = MutableList(numberOfGuesses) {
            GuessHintModel(
                guess = emptyList(),
                hint = emptyList()
            )
        }

        val secret = when (gameMode) {
            GameMode.SINGLE_PLAYER -> generateSecretUseCase.execute().blockingGet()
            GameMode.MULTIPLAYER -> TODO("Implement multi-player mode")
        }

        return GameModel(
            secret = secret,
            numberOfGuesses = numberOfGuesses,
            areBlanksAllowed = settings.areBlanksAllowed,
            areDuplicatesAllowed = settings.areDuplicatesAllowed,
            guesses = guesses
        )
    }

    override fun setupUi(): Observable<GameModel> = setupUi.observeOnUiAndHide()
}