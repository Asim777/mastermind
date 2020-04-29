package com.asimgasimzade.mastermind.presentation.game

import android.content.ClipData
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.peg_palette.palette_check_button as checkButton
import kotlinx.android.synthetic.main.peg_palette.peg_palette_black as blackPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_blue as bluePeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_green as greenPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_pink as pinkPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_red as redPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_white as whitePeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_yellow as yellowPeg

class GameFragment : BaseFragment<GameViewModel, FragmentGameBinding>() {

    private val guessHintAdapter = GuessHintAdapter()

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
    }

    private fun setupOutputListeners() {
        viewModel.outputs.setupUi()
            .subscribe(::setupUi)
            .addTo(subscriptions)

        viewModel.outputs.updateGuessHint()
            .subscribe(::updateGuessHint)
            .addTo(subscriptions)

        viewModel.enableCheckButton()
            .subscribe(::enableCheckButton)
            .addTo(subscriptions)

        viewModel.showSecret()
            .subscribe(::updateSecretView)
            .addTo(subscriptions)

        viewModel.showWinDialog()
            .subscribe { showWinDialog() }
            .addTo(subscriptions)

        viewModel.showLostDialog()
            .subscribe { showLostDialog() }
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

        RxView.clicks(checkButton).subscribe {
            viewModel.inputs.onCheckButtonClicked()
        }.addTo(subscriptions)

        guessHintAdapter.onCodePegAdded.subscribe { codePegAndPosition ->
            val codePeg = codePegAndPosition.first
            val codePegPosition = codePegAndPosition.second
            viewModel.inputs.onPegAdded(codePeg, codePegPosition)
        }.addTo(subscriptions)
    }

    private fun setupUi(gameData: GameData) {
        binding.guessHintRecyclerView.adapter = guessHintAdapter
        binding.setVariable(BR.gameModel, gameData)
        binding.guessHintRecyclerView.scrollToPosition(gameData.guesses.size - 1)
    }

    private fun updateGuessHint(gameData: GameData) {
        binding.setVariable(BR.gameModel, gameData)
    }

    private fun enableCheckButton(isEnabled: Boolean) {
        checkButton.isEnabled = isEnabled
        checkButton.setBackgroundResource(
            if (isEnabled) R.color.brown else R.color.grey
        )
    }

    private fun updateSecretView(secret: List<CodePeg>) {
        val secretViews = listOf<ImageView>(
            binding.secretView.findViewById(R.id.secret_view_peg_1),
            binding.secretView.findViewById(R.id.secret_view_peg_2),
            binding.secretView.findViewById(R.id.secret_view_peg_3),
            binding.secretView.findViewById(R.id.secret_view_peg_4)
        )
        secret.forEachIndexed { position, codePeg ->
            secretViews[position].setImageResource(
                when (codePeg) {
                    CodePeg(CodePegColor.BLUE) -> R.drawable.ic_blue_peg
                    CodePeg(CodePegColor.GREEN) -> R.drawable.ic_green_peg
                    CodePeg(CodePegColor.YELLOW) -> R.drawable.ic_yellow_peg
                    CodePeg(CodePegColor.RED) -> R.drawable.ic_red_peg
                    CodePeg(CodePegColor.WHITE) -> R.drawable.ic_white_peg
                    CodePeg(CodePegColor.BLACK) -> R.drawable.ic_black_peg
                    CodePeg(CodePegColor.PINK) -> R.drawable.ic_pink_peg
                    else -> R.drawable.ic_secret_placeholder
                }
            )
        }
    }

    private fun showWinDialog() {
        dialogCreator.create(
            context = requireActivity(),
            dialogModel = DialogModel(
                drawableResource = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_dialog_win
                ),
                title = getString(R.string.win_dialog_title),
                titleColor = ContextCompat.getColor(requireContext(), R.color.green),
                text = getString(R.string.win_dialog_text)
            ),
            onPlayAgain = {
                viewModel.inputs.playAgain()
            },
            onLeave = {
                findNavController().popBackStack()
            }
        )
    }

    override fun onPause() {
        super.onPause()

        viewModel.onPause()
    }

    private fun showLostDialog() {
        dialogCreator.create(
            context = requireActivity(),
            dialogModel = DialogModel(
                drawableResource = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_dialog_lost
                ),
                title = getString(R.string.lost_dialog_title),
                titleColor = ContextCompat.getColor(requireContext(), R.color.red),
                text = getString(R.string.lost_dialog_text)
            ),
            onPlayAgain = {
                viewModel.inputs.playAgain()
            },
            onLeave = {
                findNavController().popBackStack()
            }
        )
    }

    override val bindingLayout: Int = R.layout.fragment_game
    override val viewModel: GameViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(GameViewModel::class.java)
    }
}