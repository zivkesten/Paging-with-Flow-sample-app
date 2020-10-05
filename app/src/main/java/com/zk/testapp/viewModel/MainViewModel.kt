package com.zk.testapp.viewModel

import androidx.lifecycle.*
import com.zk.testapp.model.Event
import com.zk.testapp.model.ListViewState
import com.zk.testapp.model.ViewEffect
import com.zk.testapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: Repository) : ViewModel() {

	private var pageNum: Int = 1

	private val viewState = MutableLiveData<ListViewState>()

	private val viewAction = MutableLiveData<ViewEffect>()

	val obtainState: LiveData<ListViewState> = viewState

	val obtainViewEffects: LiveData<ViewEffect> = viewAction

	fun event(event: Event) {
		when(event) {
			is Event.ScreenLoad, Event.SwipeToRefreshEvent -> getPhotosFromApi(1)
			is Event.LoadNextPageEvent -> getPhotosFromApi(pageNum)
			is Event.ListItemClicked -> viewAction.postValue(ViewEffect.TransitionToScreen(event.item))
		}
	}

	private fun getPhotosFromApi(pageNum: Int) {
		if(pageNum > 1) {
			this.pageNum = pageNum + 1
		}
		viewModelScope.launch {
			val photos = loadPhotosFromApi(pageNum)
			photos?.let {
				val state = ListViewState(photos.photos)
				viewState.postValue(state)
			}
		}
	}

	private suspend fun loadPhotosFromApi(pageNum: Int) =
		withContext(Dispatchers.IO) {
			repository.getPhotos(pageNum)
		}
}