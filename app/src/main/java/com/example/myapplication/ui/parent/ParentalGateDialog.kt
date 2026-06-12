package com.example.myapplication.ui.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.DialogParentalGateBinding
import com.example.myapplication.util.TTSHelper
import kotlin.random.Random

class ParentalGateDialog(private val onPassed: () -> Unit) : DialogFragment() {

    private var _binding: DialogParentalGateBinding? = null
    private val binding get() = _binding!!
    private lateinit var ttsHelper: TTSHelper

    private val num1 = Random.nextInt(1, 10)
    private val num2 = Random.nextInt(1, 10)
    private val correctAnswer = num1 + num2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogParentalGateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())
        
        val question = getString(R.string.parental_gate_question, num1, num2)
        binding.tvQuestion.text = question
        
        // Speak question for accessibility/consistency
        binding.root.postDelayed({
            ttsHelper.speak(question)
        }, 500)

        binding.btnSubmit.setOnClickListener {
            val userAnswer = binding.etAnswer.text.toString().toIntOrNull()
            if (userAnswer == correctAnswer) {
                onPassed()
                dismiss()
            } else {
                val errorMsg = getString(R.string.incorrect_try_again)
                ttsHelper.speak(errorMsg)
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsHelper.shutdown()
        _binding = null
    }
}