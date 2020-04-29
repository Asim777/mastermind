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
    fun onPegAdded(addedPeg: CodePeg, position: Int)
    fun onPegRemoved(removedPeg: CodePeg, position: Int)
    fun onCheckButtonClicked()
    fun playAgain()
}

interface GameViewModelOutputs : BaseViewModelOutputs {
    fun setupUi(): Observable<GameData>
    fun updateGuessHint(): Observable<GameData>
    fun enableCheckButton(): Observable<Boolean>
    fun showWinDialog(): Observable<Unit>
    fun showLostDialog(): Observable<Unit>
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
    private val showWinDialog = PublishSubject.create<Unit>()
    private val showLostDialog = PublishSubject.create<Unit>()
    private val updateSecretView = PublishSubject.create<List<CodePeg>>()

    private lateinit var gameData: GameData
    private lateinit var gameSettings: GameSettings

    override fun onLoad(gameMode: GameMode) {
        getGameSettingsUseCase.execute()
            .doOnSubscribe { refreshing.onNext(true) }
            .doFinally { refreshing.onNext(false) }
            .compose(schedulerProvider.doOnIoObserveOnMainSingle())
            .subscribe({ settings ->
                gameSettings = settings
                val game = setupGame(gameMode, gameSettings)
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
            showWinDialog.onNext(Unit)
            updateSecretView.onNext(gameData.secret)
        } else {
            if (gameData.isLastLevel()) {
                showLostDialog.onNext(Unit)
                updateSecretView.onNext(gameData.secret)
            } else {
                gameData.guesses[gameData.currentLevel - 1] = guessHint
                gameData.guesses[gameData.currentLevel].isCurrentLevel = true
                updateGuessHint.onNext(gameData)
                gameData.currentLevel++
                updateCheckButtonStatus()
            }
        }
    }

    override fun playAgain() {
        setupGame(gameData.gameMode, gameSettings)
        setupUi.onNext(gameData)
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
        updateSecretView.onNext(List(4) { CodePeg(CodePegColor.EMPTY) })

        gameData = getNewGameData(
            secret,
            numberOfGuesses,
            gameMode,
            settings,
            guesses
        )
        saveGameDataUseCase.execute(gameData)

        return gameData
    }

    private fun getNewGameData(
        secret: List<CodePeg>,
        numberOfGuesses: Int,
        gameMode: GameMode,
        settings: GameSettings,
        guesses: MutableList<GuessHintModel>
    ) = GameData(
        secret = secret,
        numberOfGuesses = numberOfGuesses,
        gameMode = gameMode,
        currentLevel = 1,
        areBlanksAllowed = settings.areBlanksAllowed,
        areDuplicatesAllowed = settings.areDuplicatesAllowed,
        guesses = guesses
    )

    override fun setupUi(): Observable<GameData> = setupUi.observeOnUiAndHide()
    override fun updateGuessHint(): Observable<GameData> = updateGuessHint.observeOnUiAndHide()
    override fun enableCheckButton(): Observable<Boolean> = enableCheckButton.observeOnUiAndHide()
    override fun showWinDialog(): Observable<Unit> = showWinDialog.observeOnUiAndHide()
    override fun showLostDialog(): Observable<Unit> = showLostDialog.observeOnUiAndHide()
    override fun showSecret(): Observable<List<CodePeg>> = updateSecretView.observeOnUiAndHide()

    private fun GameData.getCurrentGuessHint() = this.guesses[this.currentLevel - 1]
    private fun GameData.isLastLevel() = this.currentLevel == this.guesses.size
}