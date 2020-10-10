/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zk.testapp.ui.list.types

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.zk.testapp.R
import com.zk.testapp.model.Photo
import com.zk.testapp.ui.list.callback.OnItemClickListener

/**
 * View Holder for a [Photo] RecyclerView list item.
 */
class PhotoViewHolder(view: View, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.primary_text)
    private val description: TextView = view.findViewById(R.id.sub_text)
    private val media: ImageView = view.findViewById(R.id.media_image)
    private var photo: Photo? = null

    init {
        view.setOnClickListener {
            photo?.previewURL?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(photo: Photo?) {
        if (photo == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            description.text = resources.getString(R.string.loading)
        } else {
            showData(photo)
        }
    }

    private fun showData(photo: Photo) {
        this.photo= photo
        name.text = photo.userName
        description.text = photo.likes.toString()

        Picasso.get()
            .load(photo.previewURL)
            .into(media)
    }

    companion object {
        fun create(parent: ViewGroup, listener: OnItemClickListener): PhotoViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.photo_list_item, parent, false)
            return PhotoViewHolder(view, listener)
        }
    }
}
