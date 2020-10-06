package com.zk.testapp.data

import android.util.Log
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

private const val PIXABAY_STARTING_PAGE_INDEX = 1

@FlowPreview
@ExperimentalCoroutinesApi
class PixaBayRepository(private val service: PixaBayService)  {

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    // keep the list of all results received
    private val inMemoryCache = mutableListOf<Photo>()

    // keep channel of results. The channel allows us to broadcast updates so
    // the subscriber will have the latest data

    private val fetchResults = ConflatedBroadcastChannel<PhotosResults>()

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = PIXABAY_STARTING_PAGE_INDEX

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Fetch photos, expose them as a stream of data that will emit
     * every time we get more data from the network.
     */
    suspend fun getPhotos(): Flow<PhotosResults> {
        Log.d("PixaBayRepository", "New page")
        lastRequestedPage = 1
        requestAndSaveData()
        return fetchResults.asFlow()
   }

    private suspend fun requestAndSaveData() {
        isRequestInProgress = true
        try {
            val response = service.getPics(lastRequestedPage)
            Log.d("PixaBayRepository", "response $response")
            val photos = response.photos
            //inMemoryCache.addAll(repos)
            fetchResults.offer(PhotosResults.Success(photos))
        } catch (exception: IOException) {
            fetchResults.offer(PhotosResults.Error(exception))
        } catch (exception: HttpException) {
            fetchResults.offer(PhotosResults.Error(exception))
        }
        isRequestInProgress = false
    }
}
