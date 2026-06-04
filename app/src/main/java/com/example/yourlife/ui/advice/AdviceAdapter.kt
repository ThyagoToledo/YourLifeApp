package com.example.yourlife.ui.advice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yourlife.data.model.Advice
import com.example.yourlife.databinding.ItemAdviceBinding

class AdviceAdapter(
    private val onUpvote: (Advice) -> Unit,
    private val onDownvote: (Advice) -> Unit
) : ListAdapter<Advice, AdviceAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAdviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(advice: Advice) {
            binding.tvTitle.text = advice.title
            binding.tvContent.text = advice.content
            binding.btnUpvote.setOnClickListener { onUpvote(advice) }
            binding.btnDownvote.setOnClickListener { onDownvote(advice) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Advice>() {
        override fun areItemsTheSame(oldItem: Advice, newItem: Advice) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Advice, newItem: Advice) = oldItem == newItem
    }
}
