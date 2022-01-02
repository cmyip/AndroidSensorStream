package com.thinkfore.tbexample.Reader

import android.app.Activity
import android.content.Context
import java.lang.Exception
import kotlin.reflect.KClass

class ReaderFactory {
    enum class SensorType {
        ACCELEROMETER,
        MICROPHONE_DECIBEL
    }


    companion object {
        var sensorMap: MutableMap<String, KClass<out ReaderCell>> = mutableMapOf(
            SensorType.ACCELEROMETER.name to AccelerometerReaderCellImpl::class,
            SensorType.MICROPHONE_DECIBEL.name to MicrophoneDecibelCellImpl::class)

        var _context: Activity? = null

        fun UseContext(context: Activity) {
            _context = context
        }

        fun GetCell(sensorType: SensorType): ReaderCell {
            val hasClass = sensorMap.containsKey(sensorType.name)
            if (hasClass) {
                var klass = sensorMap.get(sensorType.name)
                val newInstance = klass?.java?.newInstance()
                if (newInstance == null) {
                    throw Exception("Unable to construct class " + klass.toString())
                }
                if (_context != null) {
                    newInstance.configure(_context as Activity)
                }

                return newInstance;
            }
            throw Exception("Class instance not found")
        }

        fun AddCell(sensorTypeStr: String, clazz: KClass<ReaderCellImpl>) {
            sensorMap[sensorTypeStr] = clazz
        }
    }
}