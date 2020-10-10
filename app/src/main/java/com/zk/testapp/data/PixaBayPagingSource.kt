package com.zk.testapp.data

import android.util.Log
import androidx.paging.PagingSource
import com.zk.testapp.api.PixaBayService
import com.zk.testapp.model.Photo
import retrofit2.HttpException
import java.io.IOException

private const val PIXABAY_STARTING_PAGE_INDEX = 1


class PixaBayPagingSource(
    private val service: PixaBayService
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: PIXABAY_STARTING_PAGE_INDEX
        return try {
            val response = service.getPics(position)
            val photos = response.photos
            Log.d("Zivi", "Service -> getPhotos: ${photos.size}")
            LoadResult.Page(
                data = photos,
                prevKey = if (position == PIXABAY_STARTING_PAGE_INDEX) null else position,
                nextKey = if (photos.isEmpty()) null else position + 1

            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}