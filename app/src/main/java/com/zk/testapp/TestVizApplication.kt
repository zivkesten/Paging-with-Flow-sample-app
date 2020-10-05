package com.zk.testapp

import android.app.Application
import com.zk.testapp.di.ViewModeslModule
import com.zk.testapp.networking.networkModule
import com.zk.testapp.repository.apiModule
import com.zk.testapp.repository.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TestVizApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TestVizApplication)
            modules(listOf(
                ViewModeslModule.modules,
                repositoryModule,
                apiModule,
                networkModule
            ))
        }
    }
}

