package com.zk.testapp

import android.app.Application
import com.zk.testapp.di.ViewModelsModule
import com.zk.testapp.networking.networkModule
import com.zk.testapp.api.apiModule
import com.zk.testapp.data.repositoryModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@FlowPreview
@ExperimentalCoroutinesApi
class TestVizApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TestVizApplication)
            modules(listOf(
                ViewModelsModule.modules,
                repositoryModule,
                apiModule,
                networkModule
            ))
        }
    }
}

