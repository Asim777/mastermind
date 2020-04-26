package com.asimgasimzade.mastermind.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.asimgasimzade.mastermind.framework.di.ViewModelProviderFactory
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit
import javax.inject.Inject

abstract class BaseFragment<T : BaseViewModel, B : ViewDataBinding> : Fragment(), BaseView<T>,
    HasSupportFragmentInjector {
    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector

    @Inject
    protected lateinit var vmFactory: ViewModelProviderFactory<T>

    @Inject
    lateinit var schedulers: SchedulerProvider

    protected lateinit var binding: B
    protected val subscriptions = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, bindingLayout, container, false)
        return binding.root
    }

    open fun onBackPressed(): Boolean = false

    override fun onAttach(context: Context) {
        injectMembers()
        super.onAttach(context)
    }

    protected open fun injectMembers() =
        AndroidSupportInjection.inject(this)

    protected fun bindTextChanges(textView: TextView, textChangeListener: (String) -> Unit) {
        RxTextView.textChanges(textView)
            .subscribe { textChangeListener(it.toString()) }
            .addTo(subscriptions)
    }

    protected fun bindSwitchChanges(
        switchView: SwitchCompat,
        switchChangeListener: (Boolean) -> Unit
    ) {
        RxCompoundButton.checkedChanges(switchView)
            .skip(1)
            .subscribe { switchChangeListener(it) }
            .addTo(subscriptions)
    }

    protected fun bindClickAction(view: View, clickAction: () -> Unit) {
        RxView.clicks(view)
            .throttleFirst(BUTTON_DEBOUNCE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .subscribe { clickAction() }
            .addTo(subscriptions)
    }

    override fun onPause() {
        subscriptions.clear()
        super.onPause()
    }

    protected fun <T : ViewModel> getViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, vmFactory).get(viewModelClass)

    companion object {
        const val TRANSITION_TIME_MS: Long = 750L
        const val BUTTON_DEBOUNCE_TIMEOUT_MS = 500L
    }
}
