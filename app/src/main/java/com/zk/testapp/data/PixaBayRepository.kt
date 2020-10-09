package com.zk.testapp.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zk.testapp.api.PixaBayService
import com.zk.testapp.model.Photo
import com.zk.testapp.model.PhotoList
import com.zk.testapp.model.PhotosResults
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.koin.dsl.module
import retrofit2.HttpException
import java.io.IOException

@ExperimentalCoroutinesApi
@FlowPreview
val repositoryModule = module {
    single { PixaBayRepository(get()) }
}

class PixaBayRepository(private val service: PixaBayService)  {

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    /**
     * Fetch photos, expose them as a stream of data that will emit
     * every time we get more data from the network.
     */
    fun getPhotos(): Flow<PagingData<Photo>> {
        Log.d("PixaBayRepository", "New page")
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { PixaBayPagingSource(service) }
        ).flow
   }
}
