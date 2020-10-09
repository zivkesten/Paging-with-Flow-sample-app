package com.zk.testapp.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.zk.testapp.data.PixaBayRepository
import com.zk.testapp.model.*
import com.zk.testapp.presentation.base.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class MainViewModel @ExperimentalCoroutinesApi constructor(
	private val pixaBayRepository: PixaBayRepository)
	: BaseViewModel<ListViewState, ViewEffect, Event, Result>(ListViewState()) {

	private var currentViewState = ListViewState()
		set(value) {
			field = value
			viewStateLD.postValue(value)
		}

	private val viewAction = MutableLiveData<ViewEffect>()

	val obtainViewEffects: LiveData<ViewEffect> = viewAction

	val obtainState: LiveData<ListViewState> = viewState

	private suspend fun onScreenLoad(){
		resultToViewState(Lce.Loading())
		Log.d("Zivi", "onScreenLoad before return")
		loadJob?.cancel()
		loadJob = viewModelScope.launch(Dispatchers.IO) {
			pixaBayRepository.getPhotos()
				.cachedIn(viewModelScope)
				.onEach { ddf -> Log.d("Zivi", "onScreenLoad returned FLOW: $ddf")}
				.collect { results ->
					Log.d("Zivi", "collect: $results")
					resultToViewState(Lce.Content(Result.Content(results)))
				}
		}
	}

	override fun eventToResult(event: Event) {
		when(event) {
			is Event.ListItemClicked -> viewAction.postValue(ViewEffect.TransitionToScreen(event.item))
		}
	}

	override suspend fun suspendEventToResult(event: Event) {
		when(event) {
			is Event.ScreenLoad -> onScreenLoad()
		}
	}

	override fun resultToViewState(result: Lce<Result>) {
		Log.d("Zivi", "----- result $result")
		currentViewState = when (result) {
			//Loading state
			is Lce.Loading -> {
				currentViewState.copy(loadingStateVisibility = View.VISIBLE)
			}
			//Content state
			is Lce.Content -> {
				when (result.packet) {
					is Result.Content ->
						currentViewState.copy(
							page = result.packet.content,
							loadingStateVisibility = View.GONE)
					else -> currentViewState.copy()
				}
			}
			//Error state
			is Lce.Error -> {
				when (result.packet) {
					is Result.Error ->
						currentViewState.copy(
							errorMessage = result.packet.errorMessage,
							loadingStateVisibility = View.GONE)
					else -> currentViewState.copy()
				}
			}
		}
	}

	override fun resultToViewEffect(result: Lce<Result>) {
		TODO("Not yet implemented")
	}
}