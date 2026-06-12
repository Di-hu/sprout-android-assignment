package com.example.myapplication.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNumbersBinding
import com.example.myapplication.ui.common.RewardDialogFragment
import com.example.myapplication.util.TTSHelper

class NumbersFragment : Fragment() {

    private var _binding: FragmentNumbersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LearningViewModel by viewModels()
    private lateinit var ttsHelper: TTSHelper
    private var currentNumber = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        // Intro
        binding.root.postDelayed({
            if (isAdded) {
                ttsHelper.speak("Let's count together!")
            }
        }, 500)

        updateContent()

        binding.btnBack.setOnClickListener {
            ttsHelper.speak("Going back")
            findNavController().navigateUp()
        }

        binding.btnNext.setOnClickListener {
            if (currentNumber < 20) {
                currentNumber++
                updateContent()
                viewModel.onLetterLearned("Number $currentNumber")
            } else {
                viewModel.onActivityCompleted("Numbers")
            }
        }

        binding.btnPrev.setOnClickListener {
            if (currentNumber > 1) {
                currentNumber--
                updateContent()
            }
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
        binding.tvNumber.text = currentNumber.toString()
        binding.btnPrev.isEnabled = currentNumber > 1
        
        if (currentNumber == 20) {
            binding.btnNext.text = "Finish"
        } else {
            binding.btnNext.text = "Next"
        }

        binding.containerObjects.removeAllViews()
        for (i in 1..currentNumber) {
            val imageView = ImageView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(100, 100)
                setImageResource(android.R.drawable.btn_star_big_on)
                setPadding(8, 8, 8, 8)
            }
            binding.containerObjects.addView(imageView)
        }
        
        ttsHelper.speak(currentNumber.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsHelper.shutdown()
        _binding = null
    }
}