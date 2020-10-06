package com.zk.testapp.model

class ListViewState(
    val adapterList: List<Photo> = emptyList(),
    val error: String? = null
)

sealed class ViewEffect {
    data class TransitionToScreen(val photo: Photo) : ViewEffect()
}

sealed class Event {
    object SwipeToRefreshEvent: Event()
    object LoadNextPageEvent: Event()
    object ScreenLoad: Event()
    data class ListItemClicked(val item: Photo): Event()
}