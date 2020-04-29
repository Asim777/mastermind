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
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

interface GameViewModelInputs : BaseViewModelInputs {
    fun onLoad(gameMode: GameMode)
    fun onPegAdded(addedPeg: CodePeg, position: Int)
    fun onPegRemoved(removedPeg: CodePeg, position: Int)
    fun onCheckButtonClicked()
}

interface GameViewModelOutputs : BaseViewModelOutputs {
    fun setupUi(): Observable<GameData>
    fun updateGuessHint(): Observable<GameData>
    fun enableCheckButton(): Observable<Boolean>
    fun showWinDialog(): Completable
    fun showLostDialog(): Completable
    fun showSecret(): Observable<List<CodePeg>>
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
    private val updateGuessHint = PublishSubject.create<GameData>()
    private val enableCheckButton = PublishSubject.create<Boolean>()
    private val showWinDialog = CompletableSubject.create()
    private val showLostDialog = CompletableSubject.create()
    private val showSecret = PublishSubject.create<List<CodePeg>>()

    private lateinit var gameData: GameData

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

    override fun onPegAdded(addedPeg: CodePeg, position: Int) {
        gameData.getCurrentGuessHint().guess[position] = addedPeg
        updateGuessHint.onNext(gameData)
        updateCheckButtonStatus()
    }

    private fun updateCheckButtonStatus() {
        if (!gameData.areBlanksAllowed) {
            enableCheckButton.onNext(
                gameData.getCurrentGuessHint().guess.none {
                    it == CodePeg(CodePegColor.EMPTY)
                }
            )
        }
    }

    override fun onPegRemoved(removedPeg: CodePeg, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCheckButtonClicked() {
        val guessHint = evaluateGuessUseCase.execute(
            gameData.guesses[gameData.currentLevel - 1],
            gameData.secret
        )

        if (guessHint.isGuessCorrect) {
            showWinDialog.onComplete()
            showSecret.onNext(gameData.secret)
        } else {
            if (gameData.isLastLevel()) {
                showLostDialog.onComplete()
                showSecret.onNext(gameData.secret)
            } else {
                gameData.guesses[gameData.currentLevel - 1] = guessHint
                gameData.guesses[gameData.currentLevel].isCurrentLevel = true
                updateGuessHint.onNext(gameData)
                gameData.currentLevel++
                updateCheckButtonStatus()
            }
        }
    }

    private fun setupGame(gameMode: GameMode, settings: GameSettings): GameData {
        val numberOfGuesses = when (settings.level) {
            GameLevel.EASY -> 15
            GameLevel.MEDIUM -> 10
            GameLevel.HARD -> 8
        }

        val guesses = MutableList(numberOfGuesses) {
            GuessHintModel(
                number = (it + 1).toString(),
                guess = Array(4) { CodePeg() },
                hint = List(4) { KeyPeg() },
                isCurrentLevel = false,
                isGuessCorrect = false
            )
        }
        guesses.first().isCurrentLevel = true

        val secret = when (gameMode) {
            GameMode.SINGLE_PLAYER ->
                generateSecretUseCase.execute(settings.areDuplicatesAllowed).blockingGet()
            GameMode.MULTIPLAYER -> TODO("Implement multi-player mode")
        }

        gameData = GameData(
            secret = secret,
            numberOfGuesses = numberOfGuesses,
            currentLevel = 1,
            areBlanksAllowed = settings.areBlanksAllowed,
            areDuplicatesAllowed = settings.areDuplicatesAllowed,
            guesses = guesses
        )
        saveGameDataUseCase.execute(gameData)

        return gameData
    }

    override fun setupUi(): Observable<GameData> = setupUi.observeOnUiAndHide()
    override fun updateGuessHint(): Observable<GameData> = updateGuessHint.observeOnUiAndHide()
    override fun enableCheckButton(): Observable<Boolean> = enableCheckButton.observeOnUiAndHide()
    override fun showWinDialog(): Completable = showWinDialog.observeOnUiAndHide()
    override fun showLostDialog(): Completable = showLostDialog.observeOnUiAndHide()
    override fun showSecret(): Observable<List<CodePeg>> = showSecret.observeOnUiAndHide()

    private fun GameData.getCurrentGuessHint() = this.guesses[this.currentLevel - 1]
    private fun GameData.isLastLevel() = this.currentLevel == this.guesses.size
}