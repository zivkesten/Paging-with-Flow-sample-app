package com.zk.testapp.model

import android.view.View
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.zk.testapp.presentation.base.BaseEvent
import com.zk.testapp.presentation.base.BaseResult
import com.zk.testapp.presentation.base.BaseViewEffect
import com.zk.testapp.presentation.base.BaseViewState

data class ListViewState(
    val page: PagingData<Photo>? = null,
    val adapterList: List<Photo> = emptyList(),
    val errorMessageResource: Int? = null,
    val errorMessage: String? = null,
    val loadingStateVisibility: Int? = View.GONE,
    val errorVisibility: Int? = View.GONE
): BaseViewState

sealed class ViewEffect: BaseViewEffect {
    data class TransitionToScreen(val photo: Photo) : ViewEffect()
}

sealed class Event: BaseEvent {
    object SwipeToRefreshEvent: Event()
    data class LoadState(val state: CombinedLoadStates): Event()
    data class ListItemClicked(val item: Photo): Event()
    // Suspended
    object ScreenLoad: Event()
}

sealed class Result: BaseResult {
    data class Error(val errorMessage: String?): Result()
    data class Content(val content: PagingData<Photo>): Result()
    //data class ItemClickedResult(val item: Photo, val sharedElement: View) : Result()
}