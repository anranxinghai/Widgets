package com.pinguo.camera360.fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.*

class MainViewModel(application: Application): AndroidViewModel(application),LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        Log.i("MainViewModel","MainFragment--->onCreate")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        Log.i("MainViewModel","MainFragment--->onStart")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        Log.i("MainViewModel","MainFragment--->onResume")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){
        Log.i("MainViewModel","MainFragment--->onPause")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        Log.i("MainViewModel","MainFragment--->onStop")
    }
}