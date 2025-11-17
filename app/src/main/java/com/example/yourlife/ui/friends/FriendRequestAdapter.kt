package com.example.yourlife.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.yourlife.data.model.FriendRequest
import com.example.yourlife.databinding.ItemFriendRequestBinding

class FriendRequestAdapter(
    private val onAccept: (FriendRequest) -> Unit,
    private val onReject: (FriendRequest) -> Unit
) : ListAdapter<FriendRequest, FriendRequestAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFriendRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFriendRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(friendRequest: FriendRequest) {
            binding.tvName.text = friendRequest.name
            binding.ivAvatar.load(friendRequest.avatar)
            binding.btnAccept.setOnClickListener { onAccept(friendRequest) }
            binding.btnReject.setOnClickListener { onReject(friendRequest) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FriendRequest>() {
        override fun areItemsTheSame(oldItem: FriendRequest, newItem: FriendRequest) =
            oldItem.requestId == newItem.requestId

        override fun areContentsTheSame(oldItem: FriendRequest, newItem: FriendRequest) =
            oldItem == newItem
    }
}
