package com.zk.testapp.ui.list.types

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.zk.testapp.R
import com.zk.testapp.databinding.LoadingListItemBinding

class LoadingFooterViewHolder(
    private val binding: LoadingListItemBinding,
    retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.loadingItemProgressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState !is LoadState.Loading
            binding.errorMsg.isVisible = loadState !is LoadState.Loading
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): LoadingFooterViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_list_item, parent, false)
                val binding = LoadingListItemBinding.bind(view)
                return LoadingFooterViewHolder(binding, retry)
            }
        }
    }