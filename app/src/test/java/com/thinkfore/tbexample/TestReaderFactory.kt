package com.thinkfore.tbexample

import com.thinkfore.tbexample.Reader.AccelerometerReaderCellImpl
import com.thinkfore.tbexample.Reader.ReaderFactory
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestReaderFactory {
    @Test
    fun can_getclass() {
        val sensorInstance = ReaderFactory.GetCell(sensorType = ReaderFactory.SensorType.ACCELEROMETER)
        assertEquals(sensorInstance.javaClass.name, AccelerometerReaderCellImpl::class.java.name)
    }
}