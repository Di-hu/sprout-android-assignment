package com.example.myapplication.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAnimalsBinding
import com.example.myapplication.ui.common.RewardDialogFragment
import com.example.myapplication.util.TTSHelper

class AnimalsFragment : Fragment() {

    private var _binding: FragmentAnimalsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LearningViewModel by viewModels()
    private lateinit var ttsHelper: TTSHelper

    private val animals = listOf(
        Triple("Lion", "Roar!",R.drawable.img),
        Triple("Elephant", "Trumpet!",R.drawable.img_1),
        Triple("Cow", "Moo!",R.drawable.img_2),
        Triple("Cat", "Meow!",R.drawable.img_3),
        Triple("Dog", "Woof!",R.drawable.img_4),
        Triple("Bird", "Chirp!",R.drawable.img_5),
    )
    private var currentIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        // Intro
        binding.root.postDelayed({
            if (isAdded) {
                ttsHelper.speak("Welcome to the Animal Kingdom! Let's meet some friends.")
            }
        }, 500)

        updateContent()

        binding.btnBack.setOnClickListener {
            ttsHelper.speak("Going back")
            findNavController().navigateUp()
        }

        binding.btnNext.setOnClickListener {
            if (currentIndex < animals.size - 1) {
                currentIndex++
                updateContent()
                viewModel.onLetterLearned("Animal: ${animals[currentIndex].first}")
            } else {
                viewModel.onActivityCompleted("Animals")
            }
        }

        binding.btnPrev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                updateContent()
            }
        }
        
        binding.cardAnimal.setOnClickListener {
            val (name, sound, _) = animals[currentIndex]
            ttsHelper.speak("$name says $sound")
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
        val (name, sound, resId) = animals[currentIndex]
        binding.tvAnimalName.text = name
        binding.tvAnimalSound.text = sound
        binding.ivAnimal.setImageResource(resId)
        
        binding.btnPrev.isEnabled = currentIndex > 0
        
        if (currentIndex == animals.size - 1) {
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