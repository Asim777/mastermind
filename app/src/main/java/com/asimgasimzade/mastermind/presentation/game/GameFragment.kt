package com.asimgasimzade.mastermind.presentation.game

import android.content.ClipData
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import com.asimgasimzade.mastermind.BR
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.data.model.CodePeg
import com.asimgasimzade.mastermind.data.model.CodePegColor
import com.asimgasimzade.mastermind.data.model.GameData
import com.asimgasimzade.mastermind.data.model.GameMode
import com.asimgasimzade.mastermind.databinding.FragmentGameBinding
import com.asimgasimzade.mastermind.presentation.base.BaseFragment
import com.asimgasimzade.mastermind.presentation.menu.GAME_MODE_KEY
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.rxkotlin.addTo
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

    override fun onResume() {
        super.onResume()
        setupInputListeners()
        setupOutputListeners()

        arguments?.getParcelable<GameMode>(GAME_MODE_KEY)?.let { gameMode ->
            viewModel.onLoad(gameMode)
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
            .subscribe(::showSecret)
            .addTo(subscriptions)

        viewModel.showWinDialog()
            .subscribe(::showWinDialog)
            .addTo(subscriptions)

        viewModel.showWinDialog()
            .subscribe(::showLostDialog)
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
            viewModel.onCheckButtonClicked()
        }.addTo(subscriptions)

        guessHintAdapter.onCodePegAdded.subscribe { codePegAndPosition ->
            val codePeg = codePegAndPosition.first
            val codePegPosition = codePegAndPosition.second
            viewModel.onPegAdded(codePeg, codePegPosition)
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

    private fun showSecret(secret: List<CodePeg>) {
        //TODO: Implement showing secret in secret view
    }

    private fun showWinDialog() {
        //TODO: Implement showing win dialog
    }

    private fun showLostDialog() {
        //TODO: Implement showing lost dialog
    }

    override val bindingLayout: Int = R.layout.fragment_game
    override val viewModel: GameViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(GameViewModel::class.java)
    }
}