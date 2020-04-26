package com.asimgasimzade.mastermind.presentation.menu

import androidx.lifecycle.ViewModelProviders
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.databinding.FragmentMenuBinding
import com.asimgasimzade.mastermind.presentation.base.BaseFragment

class MenuFragment : BaseFragment<MenuViewModel, FragmentMenuBinding>() {

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

    override val bindingLayout: Int = R.layout.fragment_menu
    override val viewModel: MenuViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(MenuViewModel::class.java)
    }
}