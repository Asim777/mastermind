package com.asimgasimzade.mastermind.presentation.game

import android.app.Application
import com.asimgasimzade.mastermind.data.model.*
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.asimgasimzade.mastermind.presentation.base.BaseViewModel
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelInputs
import com.asimgasimzade.mastermind.presentation.base.BaseViewModelOutputs
import com.asimgasimzade.mastermind.usecases.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

interface GameViewModelInputs : BaseViewModelInputs {
    fun onLoad(gameMode: GameMode)
    fun onGuessPegAdded(addedPeg: CodePeg, position: Int)
    fun onSecretPegAdded(addedPeg: CodePeg, position: Int)
    fun onPegRemoved(removedPeg: CodePeg, position: Int)
    fun onActionButtonClicked()
    fun playAgain()
    fun onTurnDialogClosed(player: Player)
}

interface GameViewModelOutputs : BaseViewModelOutputs {
    fun setupUi(): Observable<GameData>
    fun updateCurrentLevel(): Observable<Pair<GameData, Int>>
    fun updateNewLevel(): Observable<Pair<GameData, Int>>
    fun enableCheckButton(): Observable<Boolean>
    fun showWinDialog(): Observable<Unit>
    fun showLostDialog(): Observable<Unit>
    fun updateSecretView(): Observable<List<CodePeg>>
    fun showCodeMakerTurnDialog(): Observable<Unit>
    fun showCodeBreakerTurnDialog(): Observable<Unit>
    fun setupUiForCodeMaker(): Observable<List<CodePeg>>
    fun setupUiForCodeBreaker(): Observable<GameData>
    fun removeDuplicatePegFromSecret(): Observable<Int>
}

class GameViewModel @Inject constructor(
    application: Application,
    schedulerProvider: SchedulerProvider,
    private val getGameSettingsUseCase: GetGameSettingsUseCase,
    private val generateSecretUseCase: GenerateSecretUseCase,
    private val getSavedGameDataUseCase: GetSavedGameDataUseCase,
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
    private val updateCurrentLevel = PublishSubject.create<Pair<GameData, Int>>()
    private val updateNewLevel = PublishSubject.create<Pair<GameData, Int>>()
    private val enableActionButton = PublishSubject.create<Boolean>()
    private val showWinDialog = PublishSubject.create<Unit>()
    private val showLostDialog = PublishSubject.create<Unit>()
    private val updateSecretView = PublishSubject.create<List<CodePeg>>()
    private val showCodeMakerTurnDialog = PublishSubject.create<Unit>()
    private val showCodeBreakerTurnDialog = PublishSubject.create<Unit>()
    private val setupUiForCodeMaker = PublishSubject.create<List<CodePeg>>()
    private val setupUiForCodeBreaker = PublishSubject.create<GameData>()
    private val removeDuplicatePegFromSecret = PublishSubject.create<Int>()

    private lateinit var gameData: GameData
    private lateinit var gameSettings: GameSettings
    private var isNewGame = false
    private var currentSecret = Array(4) { CodePeg(CodePegColor.EMPTY) }

    override fun onLoad(gameMode: GameMode) {
        getGameSettingsUseCase.execute()
            .doOnSubscribe { refreshing.onNext(true) }
            .compose(schedulerProvider.doOnIoObserveOnMainSingle())
            .subscribe({ settings ->
                gameSettings = settings
                if (!isNewGame) {
                    continueSavedGame(gameMode)
                } else {
                    setupNewGame(gameMode)
                }
            }, {
                Timber.d("Error while retrieving game settings. ${it.printStackTrace()}")
            }).addTo(subscriptions)
    }

    private fun continueSavedGame(gameMode: GameMode) {
        getSavedGameDataUseCase.execute()
            .doFinally { refreshing.onNext(false) }
            .compose(schedulerProvider.doOnIoObserveOnMainSingle())
            .subscribe({ savedGameData ->
                gameData = savedGameData ?: setupGame(gameMode, gameSettings)
                setupUi.onNext(gameData)
            }, {
                Timber.d("Error while retrieving game data. ${it.printStackTrace()}")
            }).addTo(subscriptions)
    }

    override fun onGuessPegAdded(addedPeg: CodePeg, position: Int) {
        gameData.getCurrentGuess()[position] = addedPeg
        updateCurrentLevel.onNext(gameData to gameData.guesses.size - gameData.currentLevel)
        updateActionButtonStatus(gameData.getCurrentGuess())
    }

    override fun onSecretPegAdded(addedPeg: CodePeg, position: Int) {
        if (gameData.areDuplicatesAllowed) {
            handleSecretPegAdded(addedPeg, position)
        } else {
            if (!currentSecret.contains(addedPeg)) {
                handleSecretPegAdded(addedPeg, position)
            } else {
                removeDuplicatePegFromSecret.onNext(position)
            }
        }
    }

    private fun handleSecretPegAdded(addedPeg: CodePeg, position: Int) {
        currentSecret[position] = addedPeg
        updateActionButtonStatus(currentSecret)
    }

    override fun onPegRemoved(removedPeg: CodePeg, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onActionButtonClicked() {
        when (gameData.currentPlayer) {
            Player.CODE_BREAKER -> handleActionButtonClickForCodeBreaker()
            Player.CODE_MAKER -> {
                handleActionButtonClickForCodeMaker()
                showCodeBreakerTurnDialog.onNext(Unit)
            }
        }
    }

    private fun handleActionButtonClickForCodeMaker() {
        updateSecretView.onNext(get4EmptyPegs())
        gameData.secret = currentSecret.toList()
    }

    private fun handleActionButtonClickForCodeBreaker() {
        val guessHint = evaluateGuessUseCase.execute(
            gameData.guesses[gameData.currentLevel - 1],
            gameData.secret
        )

        if (guessHint.isGuessCorrect) {
            showWinDialog.onNext(Unit)
            updateSecretView.onNext(gameData.secret)
            currentSecret = Array(4) { CodePeg(CodePegColor.EMPTY) }
        } else {
            if (gameData.isLastLevel()) {
                showLostDialog.onNext(Unit)
                updateSecretView.onNext(gameData.secret)
                currentSecret = Array(4) { CodePeg(CodePegColor.EMPTY) }
            } else {
                gameData.guesses[gameData.currentLevel - 1] = guessHint
                gameData.guesses[gameData.currentLevel].isCurrentLevel = true
                updateCurrentLevel.onNext(
                    gameData to gameData.guesses.size - gameData.currentLevel
                )
                gameData.currentLevel++
                updateNewLevel.onNext(gameData to gameData.guesses.size - gameData.currentLevel)
                updateActionButtonStatus(gameData.getCurrentGuess())
            }
        }
    }

    private fun updateActionButtonStatus(codePegs: Array<CodePeg>) {
        if (!gameData.areBlanksAllowed) {
            enableActionButton.onNext(
                codePegs.none { it == CodePeg(CodePegColor.EMPTY) }
            )
        }
    }

    override fun playAgain() {
        setupNewGame(gameData.gameMode)
    }

    override fun onTurnDialogClosed(player: Player) {
        gameData.currentPlayer = player
        when (player) {
            Player.CODE_MAKER -> setupUiForCodeMaker.onNext(
                get4EmptyPegs()
            )
            Player.CODE_BREAKER -> {
                setupUiForCodeBreaker.onNext(gameData)
                updateCurrentLevel.onNext(gameData to gameData.guesses.size - gameData.currentLevel)
                updateActionButtonStatus(gameData.getCurrentGuess())
            }
        }
    }

    override fun onCreate() {
        isNewGame = true
    }

    private fun setupNewGame(gameMode: GameMode) {
        gameData = setupGame(gameMode, gameSettings)
        setupUi.onNext(gameData)
    }

    private fun setupGame(gameMode: GameMode, settings: GameSettings): GameData {
        val numberOfGuesses = when (settings.level) {
            GameLevel.EASY -> 15
            GameLevel.MEDIUM -> 10
            GameLevel.HARD -> 8
        }

        val guesses = MutableList(numberOfGuesses) {
            getEmptyGuessHintModel(it)
        }

        guesses.first().isCurrentLevel = true
        val secret = getSecret(gameMode, settings)
        updateSecretView.onNext(get4EmptyPegs())

        return getNewGameData(
            secret,
            numberOfGuesses,
            gameMode,
            settings,
            guesses
        )
    }

    private fun getEmptyGuessHintModel(position: Int) = GuessHintModel(
        number = (position + 1).toString(),
        guess = Array(4) { CodePeg() },
        hint = List(4) { KeyPeg() },
        isCurrentLevel = false,
        isGuessCorrect = false
    )

    private fun getSecret(
        gameMode: GameMode,
        settings: GameSettings
    ) = when (gameMode) {
        GameMode.SINGLE_PLAYER -> {
            generateSecretUseCase
                .execute(settings.areDuplicatesAllowed)
                .blockingGet()
        }
        GameMode.MULTIPLAYER -> {
            showCodeMakerTurnDialog.onNext(Unit)
            get4EmptyPegs()
        }
    }

    private fun get4EmptyPegs() = List(4) {
        CodePeg(CodePegColor.EMPTY)
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
        guesses = guesses,
        currentPlayer = if (gameMode == GameMode.SINGLE_PLAYER) {
            Player.CODE_BREAKER
        } else Player.CODE_MAKER
    )

    override fun onPause() {
        saveGameDataUseCase.execute(gameData)
        isNewGame = false
    }

    override fun setupUi(): Observable<GameData> = setupUi.observeOnUiAndHide()
    override fun updateCurrentLevel(): Observable<Pair<GameData, Int>> =
        updateCurrentLevel.observeOnUiAndHide()

    override fun updateNewLevel(): Observable<Pair<GameData, Int>> =
        updateNewLevel.observeOnUiAndHide()

    override fun enableCheckButton(): Observable<Boolean> =
        enableActionButton.observeOnUiAndHide()

    override fun showWinDialog(): Observable<Unit> = showWinDialog.observeOnUiAndHide()
    override fun showLostDialog(): Observable<Unit> = showLostDialog.observeOnUiAndHide()
    override fun updateSecretView(): Observable<List<CodePeg>> =
        updateSecretView.observeOnUiAndHide()

    override fun showCodeMakerTurnDialog(): Observable<Unit> =
        showCodeMakerTurnDialog.observeOnUiAndHide()

    override fun showCodeBreakerTurnDialog(): Observable<Unit> =
        showCodeBreakerTurnDialog.observeOnUiAndHide()

    override fun setupUiForCodeMaker(): Observable<List<CodePeg>> =
        setupUiForCodeMaker.observeOnUiAndHide()

    override fun setupUiForCodeBreaker(): Observable<GameData> =
        setupUiForCodeBreaker.observeOnUiAndHide()

    override fun removeDuplicatePegFromSecret(): Observable<Int> =
        removeDuplicatePegFromSecret.observeOnUiAndHide()

    private fun GameData.getCurrentGuess() = this.guesses[this.currentLevel - 1].guess
    private fun GameData.isLastLevel() = this.currentLevel == this.guesses.size
}