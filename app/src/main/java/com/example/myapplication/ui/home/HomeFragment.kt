package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.parent.ParentViewModel
import com.example.myapplication.ui.parent.ParentalGateDialog
import com.example.myapplication.util.TTSHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var ttsHelper: TTSHelper
    
    // Using ParentViewModel as it already has access to userProgress
    private val viewModel: ParentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        // Observe progress to show coins
        viewModel.userProgress.observe(viewLifecycleOwner) { progress ->
            binding.tvTotalCoins.text = (progress?.stars ?: 0).toString()
        }

        // Welcome message
        binding.root.postDelayed({
            if (isAdded) {
                ttsHelper.speak("Welcome to Sprout! What shall we learn today?")
            }
        }, 500)

        binding.cardAlphabet.setOnClickListener {
            ttsHelper.speak("Alphabet")
            findNavController().navigate(R.id.action_homeFragment_to_alphabetFragment)
        }

        binding.cardNumbers.setOnClickListener {
            ttsHelper.speak("Numbers")
            findNavController().navigate(R.id.action_homeFragment_to_numbersFragment)
        }

        binding.cardColors.setOnClickListener {
            ttsHelper.speak("Colors")
            findNavController().navigate(R.id.action_homeFragment_to_colorsFragment)
        }

        binding.cardShapes.setOnClickListener {
            ttsHelper.speak("Shapes")
            findNavController().navigate(R.id.action_homeFragment_to_shapesFragment)
        }

        binding.cardAnimals.setOnClickListener {
            ttsHelper.speak("Animals")
            findNavController().navigate(R.id.action_homeFragment_to_animalsFragment)
        }

        binding.cardStories.setOnClickListener {
            ttsHelper.speak("Stories")
            findNavController().navigate(R.id.action_homeFragment_to_storyFragment)
        }

        binding.cardDrawing.setOnClickListener {
            ttsHelper.speak("Art Studio")
            findNavController().navigate(R.id.action_homeFragment_to_drawingFragment)
        }

        binding.cardCamera.setOnClickListener {
            ttsHelper.speak("Explorer Camera")
            findNavController().navigate(R.id.action_homeFragment_to_cameraFragment)
        }

        binding.cardPuzzle.setOnClickListener {
            ttsHelper.speak("Puzzles")
            findNavController().navigate(R.id.action_homeFragment_to_puzzleFragment)
        }

        binding.btnParentDashboard.setOnClickListener {
            ttsHelper.speak("Parents only")
            ParentalGateDialog {
                findNavController().navigate(R.id.action_homeFragment_to_parentDashboardFragment)
            }.show(parentFragmentManager, "ParentalGate")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsHelper.shutdown()
        _binding = null
    }
}