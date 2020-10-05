package com.zk.testapp.mainLIst

import androidx.recyclerview.widget.DiffUtil
import com.zk.testapp.model.Photo

class PhotosListDiffUtil(private var newPhotos: List<Photo>, private var oldPhotos: List<Photo>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldPhotos.size
    }

    override fun getNewListSize(): Int {
        return newPhotos.size
    }

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldPhotos[oldItemPosition] === newPhotos[newItemPosition]
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldPhotos[oldItemPosition] == newPhotos[newItemPosition]
    }
}