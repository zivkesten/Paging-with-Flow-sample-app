package com.zk.testapp.repository

import com.zk.testapp.model.PhotoList
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


val apiModule = module {
    single {
        val retrofit: Retrofit = get()
        retrofit.create(PixaBayApi::class.java)
    }
}

interface PixaBayApi {

    // TODO: move key and url to gradle and fetch from BuildConfig, use query params
    @GET("https://pixabay.com/api/?key=18131625-84d062aaf05a023714a29127a")
    suspend fun getPics(@Query("page") num: Int): PhotoList?
}
