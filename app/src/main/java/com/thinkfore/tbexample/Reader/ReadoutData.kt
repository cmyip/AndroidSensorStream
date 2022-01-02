package com.thinkfore.tbexample.Reader

data class ReadoutData (val name: String,
                        val readDate: Long = System.currentTimeMillis(),
                        val fromCell: ReaderCell,
                        val floatReadout: Float? = null,
                        val stringReadout: String? = null) {

}