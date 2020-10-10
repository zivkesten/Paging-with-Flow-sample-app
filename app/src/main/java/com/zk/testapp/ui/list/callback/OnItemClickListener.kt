package com.zk.testapp.ui.list.callback

import com.zk.testapp.model.Photo

interface OnItemClickListener {
    fun onItemClick(item: Photo)
}