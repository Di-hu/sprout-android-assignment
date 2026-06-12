package com.example.myapplication.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.DialogRewardBinding
import com.example.myapplication.util.TTSHelper

class RewardDialogFragment : DialogFragment() {

    private var _binding: DialogRewardBinding? = null
    private val binding get() = _binding!!
    private lateinit var ttsHelper: TTSHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRewardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        val stars = arguments?.getInt(ARG_STARS) ?: 0
        val rewardText = getString(R.string.earned_stars, stars)
        binding.tvRewardDetail.text = rewardText

        // Play celebration sound/text
        binding.root.postDelayed({
            if (isAdded) {
                ttsHelper.speak("Great job! $rewardText")
            }
        }, 500)

        binding.btnContinue.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsHelper.shutdown()
        _binding = null
    }

    companion object {
        private const val ARG_STARS = "stars"

        fun newInstance(stars: Int): RewardDialogFragment {
            val fragment = RewardDialogFragment()
            val args = Bundle()
            args.putInt(ARG_STARS, stars)
            fragment.arguments = args
            return fragment
        }
    }
}