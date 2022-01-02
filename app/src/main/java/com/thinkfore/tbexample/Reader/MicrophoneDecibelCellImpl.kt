package com.thinkfore.tbexample.Reader

class MicrophoneDecibelCellImpl : ReaderCellImpl() {
    companion object {

    }

    override fun collectReadout(): ReadoutData {
        return ReadoutData(
            name = "Accelerometer",
            floatReadout = 0f,
            fromCell = this
        )
    }
}