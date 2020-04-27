package com.asimgasimzade.mastermind.presentation.game

import android.app.Application
import com.asimgasimzade.mastermind.data.model.*
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.asimgasimzade.mastermind.presentation.base.BaseViewModel
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelInputs
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelOutputs
import com.asimgasimzade.mastermind.usecases.EvaluateGuessUseCase
import com.asimgasimzade.mastermind.usecases.GenerateSecretUseCase
import com.asimgasimzade.mastermind.usecases.GetGameSettingsUseCase
import com.asimgasimzade.mastermind.usecases.SaveGameDataUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

interface GameViewModelInputs : BaseViewModelInputs {
    fun onLoad(gameMode: GameMode)
    fun onPegSelected(clickedPeg: CodePeg)
    fun onPegAdded(addedPeg: CodePeg, position: Int)
    fun onPegRemoved(removedPeg: CodePeg, position: Int)
    fun onPegReplaced(oldPeg: CodePeg, newPeg: CodePeg, position: Int)
    fun onCheckButtonClicked()
}

interface GameViewModelOutputs : BaseViewModelOutputs {
    fun setupUi(): Observable<GameData>
    fun updateViewWithHint(): Observable<GuessHintModel>
}

class GameViewModel @Inject constructor(
    application: Application,
    schedulerProvider: SchedulerProvider,
    private val getGameSettingsUseCase: GetGameSettingsUseCase,
    private val generateSecretUseCase: GenerateSecretUseCase,
    private val saveGameDataUseCase: SaveGameDataUseCase,
    private val evaluateGuessUseCase: EvaluateGuessUseCase
) : BaseViewModel(application, schedulerProvider),
    GameViewModelInputs,
    GameViewModelOutputs {

    override val inputs: GameViewModelInputs
        get() = this

    override val outputs: GameViewModelOutputs
        get() = this

    private val setupUi = PublishSubject.create<GameData>()
    private val updateViewWithHint = PublishSubject.create<GuessHintModel>()

    private lateinit var selectedPeg: CodePeg
    private var currentLevel = 1
    private lateinit var currentGuessHint: GuessHintModel

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

    override fun onPegSelected(clickedPeg: CodePeg) {
        selectedPeg = clickedPeg
    }

    override fun onPegAdded(addedPeg: CodePeg, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onPegRemoved(removedPeg: CodePeg, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onPegReplaced(oldPeg: CodePeg, newPeg: CodePeg, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCheckButtonClicked() {
        TODO("Not yet implemented")
        /*evaluateGuessUseCase.execute()
            .doOnSubscribe { refreshing.onNext(true) }
            .doFinally { refreshing.onNext(false) }
            .compose(schedulerProvider.doOnIoObserveOnMainSingle())
            .subscribe({ guessHint ->
                updateViewWithHint.onNext(guessHint)
            }, {
                Timber.d("Error while retrieving game settings. ${it.printStackTrace()}")
            }).addTo(subscriptions)*/
    }

    private fun setupGame(gameMode: GameMode, settings: GameSettings): GameData {
        val numberOfGuesses = when (settings.level) {
            GameLevel.EASY -> 15
            GameLevel.MEDIUM -> 10
            GameLevel.HARD -> 8
        }

        val guesses = MutableList(numberOfGuesses) {
            GuessHintModel(
                number = (numberOfGuesses - it).toString(),
                guess = emptyList(),
                hint = emptyList()
            )
        }

        val secret = when (gameMode) {
            GameMode.SINGLE_PLAYER ->
                generateSecretUseCase.execute(settings.areDuplicatesAllowed).blockingGet()
            GameMode.MULTIPLAYER -> TODO("Implement multi-player mode")
        }

        val gameData = GameData(
            secret = secret,
            numberOfGuesses = numberOfGuesses,
            areBlanksAllowed = settings.areBlanksAllowed,
            areDuplicatesAllowed = settings.areDuplicatesAllowed,
            guesses = guesses
        )
        saveGameDataUseCase.execute(gameData)

        return gameData
    }

    override fun setupUi(): Observable<GameData> = setupUi.observeOnUiAndHide()

    override fun updateViewWithHint(): Observable<GuessHintModel> =
        updateViewWithHint.observeOnUiAndHide()
}