package com.example.myapplication.ui.learning

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentPuzzleBinding
import com.example.myapplication.ui.common.RewardDialogFragment
import com.example.myapplication.util.TTSHelper
import kotlin.random.Random

class PuzzleFragment : Fragment() {

    private var _binding: FragmentPuzzleBinding? = null
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
    
    private var currentShapeName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPuzzleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ttsHelper = TTSHelper(requireContext())

        binding.btnBack.setOnClickListener {
            ttsHelper.speak("Going back")
            findNavController().navigateUp()
        }

        setupPuzzle()
        setupDragAndDrop()

        // Observe rewards
        viewModel.rewardEarned.observe(viewLifecycleOwner) { coins ->
            if (coins != null) {
                RewardDialogFragment.newInstance(coins).show(parentFragmentManager, "reward")
                viewModel.onRewardShown()
            }
        }
    }

    private fun setupPuzzle() {
        val randomShape = shapes[Random.nextInt(shapes.size)]
        currentShapeName = randomShape.first
        val resId = randomShape.second

        // Target Zone
        binding.ivTargetPlaceholder.setImageResource(resId)
        binding.ivTargetPlaceholder.alpha = 0.2f
        
        // Draggable Item
        binding.ivDraggable.setImageResource(resId)
        binding.ivDraggable.tag = currentShapeName

        val instruction = getString(R.string.drag_instruction, currentShapeName)
        binding.tvInstruction.text = instruction
        
        binding.root.postDelayed({
            if (isAdded) {
                ttsHelper.speak(instruction)
            }
        }, 1000)
    }

    private fun setupDragAndDrop() {
        binding.ivDraggable.setOnLongClickListener { v ->
            val item = ClipData.Item(v.tag as? CharSequence ?: "")
            val dragData = ClipData(
                v.tag as? CharSequence ?: "",
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )
            val shadow = View.DragShadowBuilder(v)
            v.startDragAndDrop(dragData, shadow, null, 0)
            v.visibility = View.INVISIBLE
            ttsHelper.speak("Dragging the $currentShapeName")
            true
        }

        binding.targetZone.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start()
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val item = event.clipData.getItemAt(0).text
                    if (item == currentShapeName) {
                        onPuzzleSolved()
                        true
                    } else {
                        ttsHelper.speak("Oops, try again!")
                        false
                    }
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                    if (!event.result) {
                        binding.ivDraggable.visibility = View.VISIBLE
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun onPuzzleSolved() {
        binding.ivDraggable.visibility = View.GONE
        binding.animationSuccess.visibility = View.VISIBLE
        binding.animationSuccess.playAnimation()
        
        val successMsg = getString(R.string.amazing_match)
        Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
        ttsHelper.speak("$successMsg! Great job!")
        viewModel.onActivityCompleted("Puzzle: $currentShapeName")
        
        binding.root.postDelayed({
            if (isAdded) {
                setupPuzzle() // New puzzle round
                binding.ivDraggable.visibility = View.VISIBLE
                binding.animationSuccess.visibility = View.GONE
            }
        }, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsHelper.shutdown()
        _binding = null
    }
}