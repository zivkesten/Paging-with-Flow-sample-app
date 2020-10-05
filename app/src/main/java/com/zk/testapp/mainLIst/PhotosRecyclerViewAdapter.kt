package com.zk.testapp.mainLIst

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zk.testapp.databinding.PhotoListItemBinding
import com.zk.testapp.model.Photo

class PhotosRecyclerViewAdapter(private var values: List<Photo> = ArrayList(), private val listener: OnItemClickListener) : RecyclerView.Adapter<PhotosRecyclerViewAdapter.ViewHolder>() {

    fun update(photos: List<Photo>) {
        if (values.isEmpty()) {
            values = photos
            notifyDataSetChanged()
            return
        }
        val diffResult = DiffUtil.calculateDiff(PhotosListDiffUtil(values, photos))
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PhotoListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(values[position])

    override fun getItemCount(): Int {
        return values.size
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
