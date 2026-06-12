package com.example.myapplication.ui.learning

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentColorsBinding
import com.example.myapplication.ui.common.RewardDialogFragment
import com.example.myapplication.util.TTSHelper

class ColorsFragment : Fragment() {

    private var _binding: FragmentColorsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LearningViewModel by viewModels()
    private lateinit var ttsHelper: TTSHelper

    private val colors = listOf(
        Triple("Red", "#FF0000", "Apple"),
        Triple("Blue", "#0000FF", "Sky"),
        Triple("Green", "#00FF00", "Grass"),
        Triple("Yellow", "#FFFF00", "Sun"),
        Triple("Orange", "#FF8C00", "Orange"),
        Triple("Purple", "#800080", "Grape"),
        Triple("Pink", "#FFC0CB", "Rose"),
        Triple("Brown", "#A52A2A", "Tree"),
    )
    private var currentIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        updateContent()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnNext.setOnClickListener {
            if (currentIndex < colors.size - 1) {
                currentIndex++
                updateContent()
                viewModel.onLetterLearned("Color ${colors[currentIndex].first}")
            } else {
                viewModel.onActivityCompleted("Colors")
            }
        }

        binding.btnPrev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                updateContent()
            }
        }
        
        binding.viewColor.setOnClickListener {
            val (name, _, example) = colors[currentIndex]
            ttsHelper.speak("$name as in $example")
        }

        // Observe rewards
        viewModel.rewardEarned.observe(viewLifecycleOwner) { coins ->
            if (coins != null) {
                RewardDialogFragment.newInstance(coins).show(parentFragmentManager, "reward")
                viewModel.onRewardShown()
            }
        }
    }

    private fun updateContent() {
        val (name, hex, example) = colors[currentIndex]
        binding.tvColorName.text = name
        binding.viewColor.setBackgroundColor(Color.parseColor(hex))
        
        binding.btnPrev.isEnabled = currentIndex > 0
        
        if (currentIndex == colors.size - 1) {
            binding.btnNext.text = "Finish"
        } else {
            binding.btnNext.text = "Next"
        }
        
        ttsHelper.speak(name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsHelper.shutdown()
        _binding = null
    }
}