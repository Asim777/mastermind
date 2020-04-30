package com.asimgasimzade.mastermind.presentation.game

import android.content.ClipData
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.asimgasimzade.mastermind.BR
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.data.model.*
import com.asimgasimzade.mastermind.databinding.FragmentGameBinding
import com.asimgasimzade.mastermind.presentation.base.BaseFragment
import com.asimgasimzade.mastermind.presentation.menu.GAME_MODE_KEY
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import kotlinx.android.synthetic.main.peg_palette.palette_action_button as actionButton
import kotlinx.android.synthetic.main.peg_palette.peg_palette_black as blackPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_blue as bluePeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_green as greenPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_pink as pinkPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_red as redPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_white as whitePeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_yellow as yellowPeg

class GameFragment : BaseFragment<GameViewModel, FragmentGameBinding>() {

    private val guessHintAdapter = GuessHintAdapter()
    private lateinit var secretViews: List<ImageView>

    @Inject
    lateinit var dialogCreator: DialogCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.inputs.onCreate()
    }

    override fun onResume() {
        super.onResume()
        setupInputListeners()
        setupOutputListeners()

        arguments?.getParcelable<GameMode>(GAME_MODE_KEY)?.let { gameMode ->
            viewModel.inputs.onLoad(gameMode)
        }

        secretViews = listOf(
            binding.secretView.findViewById(R.id.secret_view_peg_1),
            binding.secretView.findViewById(R.id.secret_view_peg_2),
            binding.secretView.findViewById(R.id.secret_view_peg_3),
            binding.secretView.findViewById(R.id.secret_view_peg_4)
        )
    }

    private fun setupOutputListeners() {
        viewModel.outputs.setupUi()
            .subscribe(::setupUi)
            .addTo(subscriptions)

        viewModel.outputs.updateCurrentLevel()
            .subscribe(::updateLevel)
            .addTo(subscriptions)

        viewModel.outputs.updateNewLevel()
            .subscribe(::updateLevel)
            .addTo(subscriptions)

        viewModel.enableCheckButton()
            .subscribe(::enableActionButton)
            .addTo(subscriptions)

        viewModel.updateSecretView()
            .subscribe { updateSecretView(it, false) }
            .addTo(subscriptions)

        viewModel.showWinDialog()
            .subscribe { showWinDialog() }
            .addTo(subscriptions)

        viewModel.showLostDialog()
            .subscribe { showLostDialog() }
            .addTo(subscriptions)

        viewModel.showCodeMakerTurnDialog()
            .subscribe { showCodeMakerTurnDialog() }
            .addTo(subscriptions)

        viewModel.showCodeBreakerTurnDialog()
            .subscribe { showCodeBreakerTurnDialog() }
            .addTo(subscriptions)

        viewModel.setupUiForCodeBreaker()
            .subscribe(::setupUiForCodeBreaker)
            .addTo(subscriptions)

        viewModel.setupUiForCodeMaker()
            .subscribe(::setupUiForCodeMaker)
            .addTo(subscriptions)

        viewModel.removeDuplicatePegFromSecret()
            .subscribe(::removeDuplicatePegFromSecret)
            .addTo(subscriptions)
    }

    private fun setupInputListeners() {
        val codePegViews = hashMapOf<ImageView, CodePegColor>(
            bluePeg to CodePegColor.BLUE,
            greenPeg to CodePegColor.GREEN,
            yellowPeg to CodePegColor.YELLOW,
            redPeg to CodePegColor.RED,
            whitePeg to CodePegColor.WHITE,
            blackPeg to CodePegColor.BLACK,
            pinkPeg to CodePegColor.PINK
        )

        codePegViews.forEach { codePeg ->
            val codePegView = codePeg.key
            val codePegColor = codePeg.value

            codePegView.setOnTouchListener { view, _ ->
                val data = ClipData.newPlainText("color", codePegColor.name)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view?.startDragAndDrop(data, View.DragShadowBuilder(view), null, 0)
                } else {
                    view?.startDrag(data, View.DragShadowBuilder(view), null, 0)
                }
                false
            }
        }

        RxView.clicks(actionButton).subscribe {
            viewModel.inputs.onActionButtonClicked()
        }.addTo(subscriptions)

        guessHintAdapter.onCodePegAdded.subscribe { codePegAndPosition ->
            val codePeg = codePegAndPosition.first
            val codePegPosition = codePegAndPosition.second
            viewModel.inputs.onGuessPegAdded(codePeg, codePegPosition)
        }.addTo(subscriptions)
    }

    private fun setupUi(gameData: GameData) {
        binding.guessHintRecyclerView.adapter = guessHintAdapter
        binding.setVariable(BR.gameModel, gameData)
        guessHintAdapter.notifyDataSetChanged()
        binding.guessHintRecyclerView.scrollToPosition(gameData.guesses.size - 1)
    }

    private fun setupUiForCodeBreaker(gameData: GameData) {
        setSecretViewOnDragListeners(false)
        updateActionButton(R.string.action_button_text_check)
        guessHintAdapter.disableCodePlaces = false
    }

    private fun setupUiForCodeMaker(emptySecret: List<CodePeg>) {
        setSecretViewOnDragListeners(true)
        updateSecretView(emptySecret, true)
        updateActionButton(R.string.action_button_text_hide_secret)
        guessHintAdapter.disableCodePlaces = true
    }

    private fun updateActionButton(textResource: Int) {
        actionButton.text = getString(textResource)
    }

    private fun enableActionButton(isEnabled: Boolean) {
        actionButton.isEnabled = isEnabled
        actionButton.setBackgroundResource(
            if (isEnabled) R.color.brown else R.color.grey
        )
    }

    private fun updateLevel(gameDataAndPosition: Pair<GameData, Int>) {
        val gameData = gameDataAndPosition.first
        val position = gameDataAndPosition.second
        binding.setVariable(BR.gameModel, gameData)
        guessHintAdapter.notifyItemChanged(position)
    }

    private fun setSecretViewOnDragListeners(status: Boolean) {
        if (status) {
            val dragListener = getSecretsOnDragListener()

            secretViews.forEachIndexed { secretViewPosition, secretView ->
                secretView.setOnDragListener(dragListener)
                secretView.tag = secretViewPosition
            }
        } else {
            secretViews.forEach { secretView ->
                secretView.setOnDragListener(null)
            }
        }
    }

    private fun removeDuplicatePegFromSecret(position: Int) {
        secretViews[position].setImageResource(CodePegColor.EMPTY.image)
    }

    private fun getSecretsOnDragListener() = View.OnDragListener { view, dragEvent ->
        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_ENDED -> true
            DragEvent.ACTION_DRAG_ENTERED -> true
            DragEvent.ACTION_DRAG_EXITED -> true
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DROP -> {
                if (view is ImageView) {
                    when (dragEvent.clipData.getItemAt(0).text.toString()) {
                        CodePegColor.BLUE.name -> addSecretPeg(view, CodePeg(CodePegColor.BLUE))
                        CodePegColor.GREEN.name -> addSecretPeg(view, CodePeg(CodePegColor.GREEN))
                        CodePegColor.YELLOW.name -> addSecretPeg(view, CodePeg(CodePegColor.YELLOW))
                        CodePegColor.RED.name -> addSecretPeg(view, CodePeg(CodePegColor.RED))
                        CodePegColor.WHITE.name -> addSecretPeg(view, CodePeg(CodePegColor.WHITE))
                        CodePegColor.BLACK.name -> addSecretPeg(view, CodePeg(CodePegColor.BLACK))
                        CodePegColor.PINK.name -> addSecretPeg(view, CodePeg(CodePegColor.PINK))
                    }
                    true
                } else false
            }
            else -> false
        }
    }

    private fun addSecretPeg(view: ImageView, addedSecretCodePeg: CodePeg) {
        val viewPosition = view.tag.toString().toInt()
        secretViews[viewPosition].setImageResource(addedSecretCodePeg.color.image)
        viewModel.onSecretPegAdded(addedSecretCodePeg, viewPosition)
    }

    private fun updateSecretView(secret: List<CodePeg>, isCodeMaker: Boolean) {
        secret.forEachIndexed { position, codePeg ->
            secretViews[position].setImageResource(
                if (codePeg == CodePeg(CodePegColor.EMPTY)) {
                    if (isCodeMaker) R.drawable.ic_empty_peg else R.drawable.ic_secret_placeholder
                } else {
                    codePeg.color.image
                }
            )
        }
    }

    private fun showWinDialog() {
        dialogCreator.create(
            context = requireActivity(),
            dialogModel = getWinDialogModel(),
            onPlayAgain = {
                viewModel.inputs.playAgain()
            },
            onLeave = {
                findNavController().popBackStack()
            }
        )
    }

    private fun showLostDialog() {
        dialogCreator.create(
            context = requireActivity(),
            dialogModel = getLostDialogModel(),
            onPlayAgain = {
                viewModel.inputs.playAgain()
            },
            onLeave = {
                findNavController().popBackStack()
            }
        )
    }

    private fun showCodeMakerTurnDialog() {
        dialogCreator.create(
            context = requireActivity(),
            dialogModel = getCodeMakerDialogModel(),
            onClose = {
                viewModel.onTurnDialogClosed(Player.CODE_MAKER)
            }
        )
    }

    private fun showCodeBreakerTurnDialog() {
        dialogCreator.create(
            context = requireActivity(),
            dialogModel = getCodeBreakerDialogModel(),
            onClose = {
                viewModel.onTurnDialogClosed(Player.CODE_BREAKER)
            }
        )
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }


    override val bindingLayout: Int = R.layout.fragment_game
    override val viewModel: GameViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(GameViewModel::class.java)
    }
}