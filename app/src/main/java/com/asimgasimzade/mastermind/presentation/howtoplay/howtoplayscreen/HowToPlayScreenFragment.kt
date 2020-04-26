package com.asimgasimzade.mastermind.presentation.howtoplay.howtoplayscreen

import androidx.lifecycle.ViewModelProviders
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.databinding.FragmentHowToPlayScreenBinding
import com.asimgasimzade.mastermind.presentation.base.BaseFragment

class HowToPlayScreenFragment : BaseFragment<HowToPlayScreenViewModel, FragmentHowToPlayScreenBinding>() {

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

    override val bindingLayout: Int = R.layout.fragment_how_to_play_screen
    override val viewModel: HowToPlayScreenViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(HowToPlayScreenViewModel::class.java)
    }
}