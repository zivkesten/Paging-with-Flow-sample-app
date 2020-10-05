package com.zk.testapp.repository

import com.zk.testapp.model.PhotoList
import org.koin.dsl.module

val repositoryModule = module {
    single { Repository(get()) }
}

class Repository(private val pixaBayApi: PixaBayApi)  {

   suspend fun getPhotos(pageNum: Int): PhotoList? {
       return pixaBayApi.getPics(pageNum)
   }
}
