package com.asimgasimzade.mastermind.presentation.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

interface BaseViewModelInputs {
    fun onResume()
    fun onCreate()
    fun onStart()
    fun onStop()
    fun onPause()
}

interface BaseViewModelOutputs {
    fun error(): Observable<Int>
    fun finish(): Observable<Unit>
    fun refreshing(): Observable<Boolean>
}

open class BaseViewModel(
    application: Application,
    protected val schedulerProvider: SchedulerProvider
) : AndroidViewModel(application), BaseViewModelInputs, BaseViewModelOutputs {

    protected val subscriptions = CompositeDisposable()

    open val inputs: BaseViewModelInputs
        get() = this

    open val outputs: BaseViewModelOutputs
        get() = this

    protected val finish: Subject<Unit> = PublishSubject.create()
    protected val error: Subject<Int> = PublishSubject.create()
    protected val refreshing: Subject<Boolean> = BehaviorSubject.createDefault(false)


    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }

    override fun onResume() {}

    override fun onCreate() {}

    override fun onStart() {}

    override fun onStop() {}

    override fun onPause() {}

    override fun error(): Observable<Int> = error.observeOnUiAndHide()

    override fun finish(): Observable<Unit> = finish.observeOnUiAndHide()

    override fun refreshing(): Observable<Boolean> = refreshing.observeOnUiAndHide()

    internal fun <T> Observable<T>.observeOnUiAndHide() = observeOn(schedulerProvider.ui()).hide()
}
