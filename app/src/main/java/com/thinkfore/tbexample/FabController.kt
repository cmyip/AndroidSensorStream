package com.thinkfore.tbexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FabController : ViewModel() {
    private val isVisible = MutableLiveData<Boolean>()
    val currentState: LiveData<Boolean> get() = isVisible

    fun setState(visible: Boolean) {
        isVisible.value = visible
    }
}