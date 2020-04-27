package com.asimgasimzade.mastermind.presentation.game

import androidx.lifecycle.ViewModelProviders
import com.asimgasimzade.mastermind.BR
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.data.model.CodePeg
import com.asimgasimzade.mastermind.data.model.CodePegColor
import com.asimgasimzade.mastermind.data.model.GameMode
import com.asimgasimzade.mastermind.data.model.GameData
import com.asimgasimzade.mastermind.databinding.FragmentGameBinding
import com.asimgasimzade.mastermind.presentation.base.BaseFragment
import com.asimgasimzade.mastermind.presentation.menu.GAME_MODE_KEY
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.peg_palette.peg_palette_black as blackPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_white as whitePeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_blue as bluePeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_green as greenPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_pink as pinkPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_yellow as yellowPeg
import kotlinx.android.synthetic.main.peg_palette.peg_palette_red as redPeg
import kotlinx.android.synthetic.main.peg_palette.palette_check_button as checkButton

class GameFragment : BaseFragment<GameViewModel, FragmentGameBinding>() {

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
    }

    private fun setupInputListeners() {
        RxView.clicks(checkButton).subscribe {
            viewModel.onCheckButtonClicked()
        }.addTo(subscriptions)

        RxView.clicks(bluePeg).subscribe {
            viewModel.onPegSelected(CodePeg(CodePegColor.BLUE))
        }.addTo(subscriptions)

        RxView.clicks(greenPeg).subscribe {
            viewModel.onPegSelected(CodePeg(CodePegColor.GREEN))
        }.addTo(subscriptions)

        RxView.clicks(yellowPeg).subscribe {
            viewModel.onPegSelected(CodePeg(CodePegColor.YELLOW))
        }.addTo(subscriptions)

        RxView.clicks(redPeg).subscribe {
            viewModel.onPegSelected(CodePeg(CodePegColor.RED))
        }.addTo(subscriptions)

        RxView.clicks(whitePeg).subscribe {
            viewModel.onPegSelected(CodePeg(CodePegColor.WHITE))
        }.addTo(subscriptions)

        RxView.clicks(blackPeg).subscribe {
            viewModel.onPegSelected(CodePeg(CodePegColor.BLACK))
        }.addTo(subscriptions)

        RxView.clicks(pinkPeg).subscribe {
            viewModel.onPegSelected(CodePeg(CodePegColor.PINK))
        }.addTo(subscriptions)


    }

    private fun setupUi(game: GameData) {
        binding.guessHintRecyclerView.adapter = GuessHintAdapter()
        binding.setVariable(BR.gameModel, game)
        binding.guessHintRecyclerView.scrollToPosition(game.guesses.size - 1)
    }

    override val bindingLayout: Int = R.layout.fragment_game
    override val viewModel: GameViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(GameViewModel::class.java)
    }
}