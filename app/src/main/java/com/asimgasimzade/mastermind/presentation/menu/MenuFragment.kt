package com.asimgasimzade.mastermind.presentation.menu

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.data.model.GameMode
import com.asimgasimzade.mastermind.databinding.FragmentMenuBinding
import com.asimgasimzade.mastermind.presentation.base.BaseFragment
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.rxkotlin.addTo

const val GAME_MODE_KEY = "gameMode"

class MenuFragment : BaseFragment<MenuViewModel, FragmentMenuBinding>() {

    private val activityNavController by lazy { requireActivity().findNavController(R.id.homeNavigationFragment) }

    override fun onResume() {
        super.onResume()
        setupInputListeners()
        setupOutputListeners()
        viewModel.onResume()
    }

    private fun setupOutputListeners() {
        viewModel.outputs.navigate().subscribe { destination ->
            when (destination) {
                is MenuViewModel.Companion.Destination.NewGame -> {
                    val args = getGameSettingsBundle(GameMode.SINGLE_PLAYER)
                    navigateTo(R.id.goToGameFragmentFromMenu, args)
                }
                is MenuViewModel.Companion.Destination.MultiPlayer -> {
                    val args = getGameSettingsBundle(GameMode.MULTIPLAYER)
                    navigateTo(R.id.goToGameFragmentFromMenu, args)
                }
                is MenuViewModel.Companion.Destination.Settings ->
                    navigateTo(R.id.goToSettingsFragmentFromMenu)
                is MenuViewModel.Companion.Destination.HowToPlay ->
                    navigateTo(R.id.goToHowToPlayFragmentFromMenu)
                is MenuViewModel.Companion.Destination.Exit ->
                    requireActivity().finish()
            }
        }.addTo(subscriptions)
    }

    private fun setupInputListeners() {
        RxView.clicks(binding.menuItemNewGame).subscribe {
            viewModel.inputs.onNewGameClicked()
        }.addTo(subscriptions)

        RxView.clicks(binding.menuItemMultiplayer).subscribe {
            viewModel.inputs.onMultiPlayerClicked()
        }.addTo(subscriptions)

        RxView.clicks(binding.menuItemSettings).subscribe {
            viewModel.inputs.onSettingsClicked()
        }.addTo(subscriptions)

        RxView.clicks(binding.menuItemHowToPlay).subscribe {
            viewModel.inputs.onHowToPlayClicked()
        }.addTo(subscriptions)

        RxView.clicks(binding.menuItemExit).subscribe {
            viewModel.inputs.onExitClicked()
        }.addTo(subscriptions)
    }

    private fun getGameSettingsBundle(gameMode: GameMode): Bundle =
        Bundle().apply {
            putParcelable(
                GAME_MODE_KEY,
                gameMode
            )
        }

    private fun navigateTo(destination: Int, args: Bundle? = null) {
        activityNavController.navigate(destination, args)
    }

    override val bindingLayout: Int = R.layout.fragment_menu
    override val viewModel: MenuViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(MenuViewModel::class.java)
    }
}