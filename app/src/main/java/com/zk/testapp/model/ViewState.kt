package com.zk.testapp.model

import android.view.View
import com.zk.testapp.presentation.base.BaseEvent
import com.zk.testapp.presentation.base.BaseResult
import com.zk.testapp.presentation.base.BaseViewEffect
import com.zk.testapp.presentation.base.BaseViewState

data class ListViewState(
    val adapterList: List<Photo> = emptyList(),
    val errorMessageResource: Int? = null,
    val errorMessage: String? = null,
    val loadingStateVisibility: Int? = View.GONE
): BaseViewState

sealed class ViewEffect: BaseViewEffect {
    data class TransitionToScreen(val photo: Photo) : ViewEffect()
}

sealed class Event: BaseEvent {
    object SwipeToRefreshEvent: Event()
    object LoadNextPageEvent: Event()
    object ScreenLoad: Event()
    data class DataFlowEventContent(val photos: List<Photo>): Event()
    data class DataFlowEventError(val error: Exception): Event()
    data class ListItemClicked(val item: Photo): Event()
}

sealed class Result: BaseResult {
    data class Error(val errorMessage: String?): Result()
    data class Content(val content: List<Photo>): Result()
    data class ItemClickedResult(val item: Photo, val sharedElement: View) : Result()
}