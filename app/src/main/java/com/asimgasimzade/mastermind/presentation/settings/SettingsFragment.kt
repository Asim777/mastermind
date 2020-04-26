package com.asimgasimzade.mastermind.presentation.settings

import androidx.lifecycle.ViewModelProviders
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.databinding.FragmentSettingsBinding
import com.asimgasimzade.mastermind.presentation.base.BaseFragment

class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

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

    override val bindingLayout: Int = R.layout.fragment_settings
    override val viewModel: SettingsViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(SettingsViewModel::class.java)
    }
}