package com.example.myapplication.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSplashBinding
import com.example.myapplication.util.TTSHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var ttsHelper: TTSHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        // Welcome sound
        viewLifecycleOwner.lifecycleScope.launch {
            delay(500)
            if (isAdded) {
                ttsHelper.speak("Sprout. Little Minds, Big Adventures.")
            }
            
            delay(2500) // Total delay
            if (isAdded) {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsHelper.shutdown()
        _binding = null
    }
}