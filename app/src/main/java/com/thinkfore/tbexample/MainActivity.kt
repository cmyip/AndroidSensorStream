package com.thinkfore.tbexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.thinkfore.tbexample.Reader.ReaderFactory
import com.thinkfore.tbexample.Reader.ReaderManager
import com.thinkfore.tbexample.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.timerTask
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.thinkfore.tbexample.Reader.ReadoutData


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var readerManager = ReaderManager()
    val fabController: FabController by viewModels()
    val sensorController: SensorReadoutVm by viewModels()
    val readerConfigController: SensorConfigVm by viewModels()
    var runTimer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ReaderFactory.UseContext(this)
        var accelerometer = ReaderFactory.GetCell(ReaderFactory.SensorType.ACCELEROMETER)
        readerManager.addReader(accelerometer)
        val config = buildConfigFromPref()
        readerManager.configure(this, config)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        sensorController.isRunning.observe(this, {
            isRunning ->
            if (isRunning) {
                if (!readerManager.connected) {
                    navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
                    return@observe
                }
                if (runTimer == null) {
                    runTimer = Timer(SettingConstants.SCHEULE_TIMER)
                }
                runTimer?.scheduleAtFixedRate(timerTask {
                    readerManager.runCycle()
                    val newData = readerManager.readoutBuffer.toList()
                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        updateViewModel(newData)
                    })
                    readerManager.readoutBuffer.clear()
                },0,1000)
            } else {
                runTimer?.cancel()
                runTimer = null
            }
        })

        readerConfigController.currentConfig.observe(this, {
            newConfig ->
                sensorController.stop()
                readerManager.configure(this@MainActivity, newConfig)
                saveConfig()
        })

        fabController.currentState.observe(this, Observer {
            observedState ->
                if (observedState) {
                    binding.fab.show()
                } else {
                    binding.fab.hide()
                }
        })
    }

    fun buildConfigFromPref(): ReaderManager.ConnectionParameter {
        val settings = getSharedPreferences(this.packageName, MODE_PRIVATE)
        val brokerUri = settings.getString(SettingConstants.BROKER_URI, null)
        val deviceToken = settings.getString(SettingConstants.DEVICE_TOKEN, null)
        val username = settings.getString(SettingConstants.USERNAME, null)
        val password = settings.getString(SettingConstants.PASSWORD, null)
        return ReaderManager.ConnectionParameter(
            brokerUri.toString(),
            deviceToken.toString(),
            username.toString(),
            password.toString()
        )
    }

    fun saveConfig() {
        val settings = getSharedPreferences(this.packageName, MODE_PRIVATE)
        val currentConfig = readerConfigController.currentConfig.value
        val editor = settings.edit()
        editor.putString(SettingConstants.BROKER_URI, currentConfig?.url)
        editor.putString(SettingConstants.DEVICE_TOKEN, currentConfig?.deviceToken)
        editor.putString(SettingConstants.USERNAME, currentConfig?.username)
        editor.putString(SettingConstants.PASSWORD, currentConfig?.password)
        editor.commit()
    }

    fun updateViewModel(newData: List<ReadoutData>) {
        sensorController.setState(newData)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}