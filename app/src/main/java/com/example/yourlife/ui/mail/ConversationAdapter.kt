package com.example.yourlife.ui.mail

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.yourlife.data.model.Conversation
import com.example.yourlife.databinding.ItemConversationBinding

class ConversationAdapter : ListAdapter<Conversation, ConversationAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(conversation: Conversation) {
            binding.tvUserName.text = conversation.friendName
            binding.tvLastMessage.text = conversation.lastMessage
            binding.ivAvatar.load(conversation.friendAvatar)

            binding.root.setOnClickListener { 
                val context = it.context
                val intent = Intent(context, MessageActivity::class.java).apply {
                    putExtra("USER_ID", conversation.friendId)
                    putExtra("USER_NAME", conversation.friendName)
                }
                context.startActivity(intent)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Conversation>() {
        override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation) =
            oldItem.friendId == newItem.friendId

        override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation) = oldItem == newItem
    }
}
