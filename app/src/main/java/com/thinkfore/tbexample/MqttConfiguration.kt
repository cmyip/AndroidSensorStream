package com.thinkfore.tbexample

import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.thinkfore.tbexample.Reader.ReaderManager
import com.thinkfore.tbexample.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MqttConfiguration : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val fabController: FabController by activityViewModels()
    private val sensorConfigController: SensorConfigVm by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabController.setState(false)
        val currentConfig = sensorConfigController.currentConfig.value
        binding.editBrokerUri.setText(currentConfig?.url)
        binding.editClientId.setText(currentConfig?.deviceToken)
        binding.editPassword.setText(currentConfig?.password)
        binding.editUsername.setText(currentConfig?.username)

        binding.buttonSecond.setOnClickListener {
            val url = binding.editBrokerUri.text.toString()
            val accessToken = binding.editClientId.text.toString()
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            sensorConfigController.setState(ReaderManager.ConnectionParameter(url = url, deviceToken = accessToken, username, password))
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fabController.setState(true)
        _binding = null
    }
}