package com.thinkfore.tbexample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.thinkfore.tbexample.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainReadout : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val sensorController: SensorReadoutVm by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sensorController.isRunning.observe(viewLifecycleOwner, {
            isRunning ->
            binding.buttonStart.isEnabled = !isRunning
            binding.buttonStop.isEnabled = isRunning
        })

        sensorController.currentReadout.observe(viewLifecycleOwner, {
            readout ->
            if (readout?.count()!! > 0) {
                binding.textviewFirst.text = "Current Value:" + readout[0].floatReadout.toString()
            }

        })

        binding.buttonStart.setOnClickListener {
            sensorController.start()
        }

        binding.buttonStop.setOnClickListener {
            sensorController.stop()
            binding.textviewFirst.text = "Press start to stream accelerometer data"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}