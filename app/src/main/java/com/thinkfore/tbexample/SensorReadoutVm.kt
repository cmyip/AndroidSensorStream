package com.thinkfore.tbexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thinkfore.tbexample.Reader.ReadoutData

class SensorReadoutVm: ViewModel() {
    private val _currentReadout = MutableLiveData<List<ReadoutData>>()
    private val _isRunning = MutableLiveData<Boolean>()
    val currentReadout: LiveData<List<ReadoutData>> get() = _currentReadout
    val isRunning: LiveData<Boolean> get() = _isRunning


    fun setState(outvalue: List<ReadoutData>) {
        _currentReadout.value = outvalue
    }

    fun start() {
        _isRunning.value = true
    }

    fun stop() {
        _isRunning.value = false
    }
}