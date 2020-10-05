package com.zk.testapp.di

import com.zk.testapp.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModeslModule {
	companion object{
		val modules = module {
			viewModel { MainViewModel(get()) }
		}
	}
}
