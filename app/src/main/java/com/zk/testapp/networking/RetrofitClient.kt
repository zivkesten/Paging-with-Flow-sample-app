package com.zk.testapp.networking

import com.zk.testapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    factory { provideOkHttpClient() }
    single { provideRetrofit(get()) }
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideOkHttpClient(): OkHttpClient {
    val connectTimeout = 15 // 15s
    val readTimeout = 15 // 15s
    val key = BuildConfig.PHOTOS_API_KEY
    val builder = OkHttpClient().newBuilder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
            val originalHttpUrl = chain.request().url
            val url = originalHttpUrl.newBuilder().addQueryParameter("key", key).build()
            request.url(url)
            return@addInterceptor chain.proceed(request.build())
        }
        .connectTimeout(connectTimeout.toLong(), TimeUnit.SECONDS)

        .readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
		builder.addInterceptor(httpLoggingInterceptor)
    }
    return builder.build()
}
