package com.thinkfore.tbexample.Reader

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.json.JSONObject

class ReaderManager : MqttCallback {
    data class ConnectionParameter(
        public val url: String,
        public val deviceToken: String,
        public val username: String,
        public val password: String
    )
    val readList = mutableListOf<ReaderCell>();
    val readoutBuffer = mutableListOf<ReadoutData>()
    var connected = false
    var lastConnectError: Throwable? = null
    private lateinit var mqttClient: MqttAndroidClient
    private var topic = "v1/devices/me/telemetry"

    fun addReader(readerCell: ReaderCell) {
        readList.add(readerCell)
    }

    fun collectSensor() {
        for (readItem in readList) {
            readoutBuffer.add(readItem.collectReadout())
        }
    }

    fun configure(context: Context, connectionConfig: ConnectionParameter) {
        mqttClient = MqttAndroidClient(context, connectionConfig.url, connectionConfig.deviceToken)
        mqttClient.setCallback(this)

        val options = MqttConnectOptions()
        try {
            options.userName = connectionConfig.username
            options.password = connectionConfig.password.toCharArray()
            mqttClient.connect(options, null, object: IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    connected = true
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    lastConnectError = exception
                    Log.e(TAG, exception.toString())
                    exception?.printStackTrace()
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
    }

    override fun connectionLost(cause: Throwable?) {
        Log.d(TAG, "Connection lost ${cause.toString()}")
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {

    }

    fun uploadData() {
        if (!connected) {
            return
        }
        val jsonOut = JSONObject()
        for (readout in readoutBuffer) {
            if (readout.floatReadout != null) {
                jsonOut.put(readout.name, readout.floatReadout.toDouble())
            }
            if (readout.stringReadout != null) {
                jsonOut.put(readout.name, readout.stringReadout.toString())
            }
        }

        val message = MqttMessage()
        message.payload = jsonOut.toString().toByteArray()
        message.qos = 1
        message.isRetained = false
        mqttClient.publish(topic, message, null, object: IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {

            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {

            }
        })
    }

    fun runCycle() {
        collectSensor()
        uploadData()
    }
}