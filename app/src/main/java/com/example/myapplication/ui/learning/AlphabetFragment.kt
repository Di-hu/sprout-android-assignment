package com.example.myapplication.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentAlphabetBinding
import com.example.myapplication.ui.common.RewardDialogFragment
import com.example.myapplication.util.TTSHelper

class AlphabetFragment : Fragment() {

    private var _binding: FragmentAlphabetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LearningViewModel by viewModels()
    private lateinit var ttsHelper: TTSHelper

    private val alphabet = listOf(
        "A" to "Apple", "B" to "Ball", "C" to "Cat", "D" to "Dog",
        "E" to "Elephant", "F" to "Fish", "G" to "Giraffe", "H" to "Horse",
        "I" to "Iguana", "J" to "Jellyfish", "K" to "Kangaroo", "L" to "Lion",
        "M" to "Monkey", "N" to "Nest", "O" to "Octopus", "P" to "Penguin",
        "Q" to "Queen", "R" to "Rabbit", "S" to "Snake", "T" to "Tiger",
        "U" to "Umbrella", "V" to "Van", "W" to "Whale", "X" to "X-ray",
        "Y" to "Yak", "Z" to "Zebra"
    )
    private var currentIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlphabetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        // Intro sound
        binding.root.postDelayed({
            if (isAdded) {
                ttsHelper.speak("Let's learn our A B C's!")
            }
        }, 500)

        updateContent()

        binding.btnBack.setOnClickListener {
            ttsHelper.speak("Going back")
            findNavController().navigateUp()
        }

        binding.btnNext.setOnClickListener {
            if (currentIndex < alphabet.size - 1) {
                currentIndex++
                updateContent()
                viewModel.onLetterLearned(alphabet[currentIndex].first)
            } else {
                viewModel.onActivityCompleted("Alphabet")
            }
        }

        binding.btnPrev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                updateContent()
            }
        }
        
        binding.cardLetter.setOnClickListener {
            val (letter, word) = alphabet[currentIndex]
            ttsHelper.speak("$letter is for $word")
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
        val (letter, word) = alphabet[currentIndex]
        binding.tvLetter.text = letter
        binding.tvWord.text = word
        
        binding.btnPrev.isEnabled = currentIndex > 0
        
        if (currentIndex == alphabet.size - 1) {
            binding.btnNext.text = "Finish"
        } else {
            binding.btnNext.text = "Next"
        }
        
        ttsHelper.speak(letter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsHelper.shutdown()
        _binding = null
    }
}