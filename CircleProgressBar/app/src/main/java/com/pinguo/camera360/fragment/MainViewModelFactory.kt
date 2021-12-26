package com.pinguo.camera360.fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(val application: Application) : ViewModelProvider.Factory {

    override fun <MainViewModel : ViewModel> create(modelClass: Class<MainViewModel>): MainViewModel {
        val constructor = modelClass.getDeclaredConstructor(Application::class.java)
        return constructor.newInstance(application)
    }

}