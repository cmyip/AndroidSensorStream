package com.thinkfore.tbexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thinkfore.tbexample.Reader.ReaderManager
import com.thinkfore.tbexample.Reader.ReadoutData

class SensorConfigVm: ViewModel() {
    private val _currentConfig = MutableLiveData<ReaderManager.ConnectionParameter>()
    val currentConfig: LiveData<ReaderManager.ConnectionParameter> get() = _currentConfig


    fun setState(config: ReaderManager.ConnectionParameter) {
        _currentConfig.value = config
    }
}