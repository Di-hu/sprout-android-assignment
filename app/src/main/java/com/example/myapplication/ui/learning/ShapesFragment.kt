package com.example.myapplication.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentShapesBinding
import com.example.myapplication.ui.common.RewardDialogFragment
import com.example.myapplication.util.TTSHelper

class ShapesFragment : Fragment() {

    private var _binding: FragmentShapesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LearningViewModel by viewModels()
    private lateinit var ttsHelper: TTSHelper

    private val shapes = listOf(
        "Square" to R.drawable.img_6,
        "Circle" to R.drawable.img_7,
        "Triangle" to R.drawable.img_8,
        "Star" to R.drawable.img_9,
        "Rectangle" to R.drawable.img_10,
        "Heart" to R.drawable.img_12
    )
    private var currentIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShapesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        // Intro sound
        binding.root.postDelayed({
            if (isAdded) {
                ttsHelper.speak("Let's learn about shapes!")
            }
        }, 500)

        updateContent()

        binding.btnBack.setOnClickListener {
            ttsHelper.speak("Going back")
            findNavController().navigateUp()
        }

        binding.btnNext.setOnClickListener {
            if (currentIndex < shapes.size - 1) {
                currentIndex++
                updateContent()
                viewModel.onLetterLearned("Shape ${shapes[currentIndex].first}")
            } else {
                viewModel.onActivityCompleted("Shapes")
            }
        }

        binding.btnPrev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                updateContent()
            }
        }
        
        binding.ivShape.setOnClickListener {
            ttsHelper.speak(shapes[currentIndex].first)
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
        val (name, resId) = shapes[currentIndex]
        binding.tvShapeName.text = name
        binding.ivShape.setImageResource(resId)
        
        binding.btnPrev.isEnabled = currentIndex > 0
        
        if (currentIndex == shapes.size - 1) {
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
