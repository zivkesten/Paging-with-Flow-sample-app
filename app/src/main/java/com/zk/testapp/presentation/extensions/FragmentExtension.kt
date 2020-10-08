package com.zk.testapp.presentation.extensions

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

fun Fragment.loadImageForMaterialTransition(imageUrl: String, destination: ImageView) {
    Picasso.get()
        .load(imageUrl)
        .into(destination, object : Callback {
            override fun onSuccess() {
                startPostponedEnterTransition()
            }

            override fun onError(e: Exception?) {
                startPostponedEnterTransition()
            }
        })
}

fun Fragment.loadImageAsync(imageUrl: String, destination: ImageView) {
    Picasso.get()
        .load(imageUrl)
        .into(destination)
}