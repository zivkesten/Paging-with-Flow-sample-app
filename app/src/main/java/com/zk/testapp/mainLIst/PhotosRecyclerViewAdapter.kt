package com.zk.testapp.mainLIst

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zk.testapp.databinding.PhotoListItemBinding
import com.zk.testapp.model.Photo

class PhotosRecyclerViewAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<Photo, PhotosRecyclerViewAdapter.ViewHolder>(PHOTO_COMPARATOR) {

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PhotoListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        getItem(position)?.let { photo ->
            holder.bind(photo)
        }
    }

    inner class ViewHolder(private val binding: PhotoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Photo) {
            with(binding) {
                primaryText.text = item.userName
                subText.text = item.likes.toString()
                Picasso.get()
                    .load(item.previewURL)

                    .into(mediaImage)
                binding.root.setOnClickListener {
                    listener.onItemClick(item)
                }
            }
        }
    }
}
