package com.zk.testapp.viewModel

import androidx.lifecycle.*
import com.zk.testapp.model.Event
import com.zk.testapp.model.ListViewState
import com.zk.testapp.model.ViewEffect
import com.zk.testapp.data.PixaBayRepository
import com.zk.testapp.model.PhotosResults
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel(private val pixaBayRepository: PixaBayRepository) : ViewModel() {

	private var pageNum: Int = 1

	//private val viewState = MutableLiveData<ListViewState>()

	private val viewAction = MutableLiveData<ViewEffect>()



	val obtainViewEffects: LiveData<ViewEffect> = viewAction

	private val fetchLiveData = MutableLiveData<String>()

	private val viewState: LiveData<ListViewState> = fetchLiveData.switchMap {
		liveData {
			val photos = pixaBayRepository.getPhotos().asLiveData(Dispatchers.Main)
			emitSource(photos.map { results ->
				when (results) {
					is PhotosResults.Success -> ListViewState(results.data)
					is PhotosResults.Error -> ListViewState(error = results.error.toString())
				}
			})
		}
	}

	val obtainState: LiveData<ListViewState> = viewState

	fun event(event: Event) {
		when(event) {
			is Event.ScreenLoad, Event.SwipeToRefreshEvent -> getPhotosFromApi()
			is Event.LoadNextPageEvent -> getPhotosFromApi()
			is Event.ListItemClicked -> viewAction.postValue(ViewEffect.TransitionToScreen(event.item))
		}
	}

	private fun getPhotosFromApi() {
		fetchLiveData.postValue("j")
	}
}