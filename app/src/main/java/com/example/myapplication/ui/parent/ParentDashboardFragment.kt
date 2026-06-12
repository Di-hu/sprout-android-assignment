package com.example.myapplication.ui.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentParentDashboardBinding
import com.example.myapplication.util.TTSHelper

class ParentDashboardFragment : Fragment() {

    private var _binding: FragmentParentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ParentViewModel by viewModels()
    private val rewardAdapter = RewardAdapter()
    private lateinit var ttsHelper: TTSHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        // Announcement
        binding.root.postDelayed({
            if (isAdded) {
                ttsHelper.speak("Parent Dashboard")
            }
        }, 500)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.rvRewards.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rewardAdapter
        }

        viewModel.userProgress.observe(viewLifecycleOwner) { progress ->
            progress?.let {
                binding.tvActivitiesCount.text = it.activitiesCompleted.toString()
                binding.tvStarsCount.text = it.stars.toString()
                binding.tvTimeCount.text = "${it.screenTimeMinutes}m"
            }
        }

        viewModel.allRewards.observe(viewLifecycleOwner) { rewards ->
            rewardAdapter.submitList(rewards)
        }

        binding.btnReset.setOnClickListener {
            ttsHelper.speak("Reset progress feature coming soon")
            // In a real app, this would show a confirmation dialog
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsHelper.shutdown()
        _binding = null
    }
}