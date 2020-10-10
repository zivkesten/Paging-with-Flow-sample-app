package com.zk.testapp.api

import com.zk.testapp.model.PhotoList
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

val apiModule = module {
    single {
        val retrofit: Retrofit = get()
        retrofit.create(PixaBayService::class.java)
    }
}

interface PixaBayService {

    @GET("api/")
    suspend fun getPics(
        @Query("page") page: Int): PhotoList
}
