package com.zk.testapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo (
	@SerializedName("id")
	val id: Int,
	@SerializedName("user")
	val userName: String?,
	@SerializedName("likes")
	val likes: Int,
	@SerializedName("previewURL")
	val previewURL: String?,


) : Parcelable
