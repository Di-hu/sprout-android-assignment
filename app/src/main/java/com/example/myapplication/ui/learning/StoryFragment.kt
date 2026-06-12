package com.example.myapplication.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentStoryBinding
import com.example.myapplication.ui.common.RewardDialogFragment
import com.example.myapplication.util.TTSHelper

class StoryFragment : Fragment() {

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LearningViewModel by viewModels()
    private lateinit var ttsHelper: TTSHelper

    private val story = """
        Once upon a time, there was a tiny seed named Sprout. 
        Sprout lived in the dark, warm earth. 
        One day, the sun came out and warmed the ground. 
        Sprout felt a tickle and started to grow! 
        He grew a little root, then a tiny green leaf. 
        Soon, Sprout became a beautiful flower, dancing in the wind.
    """.trimIndent()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        // Intro sound
        binding.root.postDelayed({
            if (isAdded) {
                ttsHelper.speak("Story time! Let's listen to the story of Sprout.")
            }
        }, 500)

        binding.tvStoryContent.text = story

        binding.btnBack.setOnClickListener {
            ttsHelper.speak("Going back")
            findNavController().navigateUp()
        }

        binding.btnListen.setOnClickListener {
            ttsHelper.speak(story)
            viewModel.onActivityCompleted("Sprout's Story")
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