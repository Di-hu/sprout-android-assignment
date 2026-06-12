package com.example.myapplication.ui.drawing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDrawingBinding
import com.example.myapplication.ui.common.RewardDialogFragment
import com.example.myapplication.ui.learning.LearningViewModel
import com.example.myapplication.util.TTSHelper

class DrawingFragment : Fragment() {

    private var _binding: FragmentDrawingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LearningViewModel by viewModels()
    private lateinit var ttsHelper: TTSHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        // Intro
        binding.root.postDelayed({
            if (isAdded) {
                ttsHelper.speak("Welcome to the Art Studio! Choose a color and start drawing.")
            }
        }, 500)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnFinish.setOnClickListener {
            ttsHelper.speak("Amazing masterpiece!")
            viewModel.onActivityCompleted("Drawing")
        }

        binding.colorPurple.setOnClickListener {
            ttsHelper.speak("Purple")
            binding.drawingView.setColor(ContextCompat.getColor(requireContext(), R.color.sprout_purple))
        }

        binding.colorTeal.setOnClickListener {
            ttsHelper.speak("Teal")
            binding.drawingView.setColor(ContextCompat.getColor(requireContext(), R.color.sprout_teal))
        }

        binding.colorPink.setOnClickListener {
            ttsHelper.speak("Pink")
            binding.drawingView.setColor(ContextCompat.getColor(requireContext(), R.color.sprout_pink))
        }

        binding.colorOrange.setOnClickListener {
            ttsHelper.speak("Orange")
            binding.drawingView.setColor(ContextCompat.getColor(requireContext(), R.color.sprout_orange))
        }
        binding.colorGreen.setOnClickListener {
            ttsHelper.speak("Green")
            binding.drawingView.setColor(ContextCompat.getColor(requireContext(), R.color.sprout_green))
        }
        binding.colorBrown.setOnClickListener {
            ttsHelper.speak("Brown")
            binding.drawingView.setColor(ContextCompat.getColor(requireContext(), R.color.sprout_brown))
        }

        binding.btnClear.setOnClickListener {
            ttsHelper.speak("Clear canvas")
            binding.drawingView.clearCanvas()
        }

        // Observe rewards
        viewModel.rewardEarned.observe(viewLifecycleOwner) { coins ->
            if (coins != null) {
                RewardDialogFragment.newInstance(coins).show(parentFragmentManager, "reward")
                viewModel.onRewardShown()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsHelper.shutdown()
        _binding = null
    }
}