package com.zk.testapp.ui.list

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.zk.testapp.ui.list.types.LoadingFooterViewHolder

class PhotosLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingFooterViewHolder>() {
    override fun onBindViewHolder(holder: LoadingFooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingFooterViewHolder {
        return LoadingFooterViewHolder.create(parent, retry)
    }
}