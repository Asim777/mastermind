package com.asimgasimzade.mastermind.presentation

import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.databinding.ActivityHomeBinding
import com.asimgasimzade.mastermind.presentation.base.BaseActivity

class HomeActivity : BaseActivity<HomeViewModel, ActivityHomeBinding>() {
    private val navController by lazy { findNavController(R.id.homeNavigationFragment) }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override val bindingLayout = R.layout.activity_home
    override val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(HomeViewModel::class.java)
    }
}
