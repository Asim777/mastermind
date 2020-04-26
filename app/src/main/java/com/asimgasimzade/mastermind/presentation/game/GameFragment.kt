package com.asimgasimzade.mastermind.presentation.game

import androidx.lifecycle.ViewModelProviders
import com.asimgasimzade.mastermind.BR
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.data.model.GameMode
import com.asimgasimzade.mastermind.data.model.GameModel
import com.asimgasimzade.mastermind.databinding.FragmentGameBinding
import com.asimgasimzade.mastermind.presentation.base.BaseFragment
import com.asimgasimzade.mastermind.presentation.menu.GAME_MODE_KEY
import io.reactivex.rxkotlin.addTo

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
        //TODO: Implement input listeners
    }

    private fun setupUi(game: GameModel) {
        binding.guessHintRecyclerView.adapter = GuessHintAdapter()
        binding.setVariable(BR.gameModel, game)
    }

    override val bindingLayout: Int = R.layout.fragment_game
    override val viewModel: GameViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(GameViewModel::class.java)
    }
}