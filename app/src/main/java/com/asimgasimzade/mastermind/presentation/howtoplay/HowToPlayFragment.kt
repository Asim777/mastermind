package com.asimgasimzade.mastermind.presentation.howtoplay

import androidx.lifecycle.ViewModelProviders
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.databinding.FragmentHowToPlayBinding
import com.asimgasimzade.mastermind.presentation.base.BaseFragment

class HowToPlayFragment : BaseFragment<HowToPlayViewModel, FragmentHowToPlayBinding>() {

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

    override val bindingLayout: Int = R.layout.fragment_how_to_play
    override val viewModel: HowToPlayViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(HowToPlayViewModel::class.java)
    }
}