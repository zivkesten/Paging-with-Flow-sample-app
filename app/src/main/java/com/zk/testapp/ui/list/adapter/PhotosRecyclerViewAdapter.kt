package com.zk.testapp.ui.list.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.zk.testapp.model.Photo
import com.zk.testapp.ui.list.callback.OnItemClickListener
import com.zk.testapp.ui.list.types.PhotoViewHolder

class PhotosRecyclerViewAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<Photo, PhotoViewHolder>(PHOTO_COMPARATOR) {

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { photo ->
            holder.bind(photo)
        }
    }
}
