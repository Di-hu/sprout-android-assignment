package com.example.myapplication.ui.parent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.Reward
import com.example.myapplication.databinding.ItemRewardBinding
import java.text.SimpleDateFormat
import java.util.*

class RewardAdapter : ListAdapter<Reward, RewardAdapter.ViewHolder>(RewardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRewardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemRewardBinding) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        fun bind(reward: Reward) {
            binding.tvRewardName.text = reward.name
            binding.tvRewardType.text = reward.type
            binding.tvEarnedDate.text = dateFormat.format(Date(reward.earnedDate))
        }
    }

    class RewardDiffCallback : DiffUtil.ItemCallback<Reward>() {
        override fun areItemsTheSame(oldItem: Reward, newItem: Reward): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Reward, newItem: Reward): Boolean = oldItem == newItem
    }
}