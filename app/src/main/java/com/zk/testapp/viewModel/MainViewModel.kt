package com.zk.testapp.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.zk.testapp.data.PixaBayRepository
import com.zk.testapp.model.*
import com.zk.testapp.presentation.base.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.lang.Exception


@FlowPreview
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

	private fun onDataFlowEventContent(photos: List<Photo>) {
		Log.d("Zivi", "onDataFlowEventContent: ${photos.size}")
		resultToViewState(Lce.Content(Result.Content(photos)))
	}

	private fun onDataFlowEventError(error: Exception) {
		Log.d("Zivi", "onDataFlowEventError: ${error.localizedMessage}")
		resultToViewState(Lce.Error(Result.Error(errorMessage = error.localizedMessage)))
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	private fun onScreenLoad() {
		viewModelScope.launch(Dispatchers.IO) {
			pixaBayRepository.getPhotos().collect { results ->
				when(results) {
					is PhotosResults.Success -> {
						Log.d("Zivi", "PhotosResults.Success result: ${results.data.size}")
						onEvent(Event.DataFlowEventContent(results.data))
					}
					is PhotosResults.Error -> {
						Log.d("Zivi", "PhotosResults.Error error: ${results.error.localizedMessage}")
						onEvent(Event.DataFlowEventError(results.error))
					}
				}
			}
		}
	}

	override fun eventToResult(event: Event) {
		when(event) {
			is Event.ScreenLoad -> onScreenLoad()
			is Event.DataFlowEventContent -> onDataFlowEventContent(event.photos)
			is Event.DataFlowEventError -> onDataFlowEventError(event.error)
			is Event.ListItemClicked -> viewAction.postValue(ViewEffect.TransitionToScreen(event.item))
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
							adapterList = result.packet.content,
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