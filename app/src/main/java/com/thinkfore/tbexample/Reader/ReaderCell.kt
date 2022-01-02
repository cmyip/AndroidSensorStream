package com.thinkfore.tbexample.Reader

import android.content.Context

interface ReaderCell {
    fun collectReadout(): ReadoutData
    fun configure(context: Context)
}