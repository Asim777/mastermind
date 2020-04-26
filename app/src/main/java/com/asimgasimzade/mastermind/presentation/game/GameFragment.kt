package com.asimgasimzade.mastermind.presentation.game

import androidx.lifecycle.ViewModelProviders
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.databinding.FragmentGameBinding
import com.asimgasimzade.mastermind.presentation.base.BaseFragment

class GameFragment : BaseFragment<GameViewModel, FragmentGameBinding>() {

    override fun onResume() {
        super.onResume()
        setupInputListeners()
        setupOutputListeners()
        viewModel.onResume()
    }

    private fun setupOutputListeners() {
        //TODO: Implement output listeners
    }

    private fun setupInputListeners() {
        //TODO: Implement input listeners
    }

    private fun setupUi() {
        //TODO: Implement setup ui
    }

    override val bindingLayout: Int = R.layout.fragment_game
    override val viewModel: GameViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(GameViewModel::class.java)
    }
}