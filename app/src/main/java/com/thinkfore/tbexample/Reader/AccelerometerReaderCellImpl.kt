package com.thinkfore.tbexample.Reader

import android.app.Activity
import android.content.Context
import android.hardware.*
import androidx.core.content.ContextCompat.getSystemService

class AccelerometerReaderCellImpl : ReaderCellImpl() {
    companion object {

    }
    var readout = ReadoutData(name = "", fromCell = this)

    override fun configure(context: Context) {
        super.configure(context)
        val sensorManager = getSystemService(context, SensorManager::class.java)
        val sensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        val self = this
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val x = event?.values?.get(0)
                if (x == null) {
                    return
                }
                readout = ReadoutData(name="x", floatReadout = x, fromCell = self)
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }

        }

        sensor?.also { _sensor ->
            val __sensor: Sensor = _sensor
            val _sensorEventListener : SensorEventListener = sensorEventListener
            val listenSuccess: Boolean = sensorManager.registerListener(_sensorEventListener, __sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun collectReadout(): ReadoutData {
        return readout
    }
}