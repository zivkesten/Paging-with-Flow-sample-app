package com.zk.testapp.presentation.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zk.testapp.model.Lce
import kotlinx.coroutines.Job

abstract class BaseViewModel<ViewState : BaseViewState,
        ViewAction : BaseViewEffect,
        Event: BaseEvent,
        Result: BaseResult>(initialState: ViewState) :
    ViewModel() {

    internal val viewStateLD = MutableLiveData<ViewState>()
    internal val viewEffectLD = MutableLiveData<ViewAction>()
    val viewState: LiveData<ViewState> get() = viewStateLD
    val viewEffects: LiveData<ViewAction> get() = viewEffectLD

    var loadFromBEJob: Job? = null

    fun onEvent(event: Event) {
        Log.d("Zivi","----- event ${event.javaClass.simpleName}")
        eventToResult(event)
    }

    abstract fun eventToResult(event: Event)

    abstract fun resultToViewState(result: Lce<Result>)

    abstract fun resultToViewEffect(result: Lce<Result>)
}

interface BaseViewState

interface BaseViewEffect

interface BaseEvent

interface BaseResult