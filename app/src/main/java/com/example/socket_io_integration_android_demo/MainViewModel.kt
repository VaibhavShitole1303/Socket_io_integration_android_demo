package com.example.socket_io_integration_android_demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _socketStatus= MutableLiveData(false)// define the live data and default it will false

    val socketStatus:LiveData<Boolean> = _socketStatus // here we assign that th our live data which we want to observe

    private val _message= MutableLiveData<Pair<Boolean,String>>()
    val message:LiveData<Pair<Boolean,String>> = _message

    @OptIn(DelicateCoroutinesApi::class)
    fun setStatus(status:Boolean) = GlobalScope.launch(Dispatchers.Main){
        _socketStatus.value= status
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun setMessage(message:Pair<Boolean,String>) = GlobalScope.launch(Dispatchers.Main){
        if (_socketStatus.value==true){
            _message.value= message
        }
    }
}