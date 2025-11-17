package com.example.yourlife.ui.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.yourlife.R
import com.example.yourlife.data.model.User
import com.example.yourlife.databinding.ItemFriendBinding

class FriendAdapter(
    private val listener: OnFriendInteractionListener
) : ListAdapter<User, FriendAdapter.ViewHolder>(DiffCallback()) {

    private val currentFriendIds = mutableSetOf<Int>()

    interface OnFriendInteractionListener {
        fun onSendMessageClick(user: User)
        fun onViewProfileClick(user: User)
        fun onRemoveFriendClick(user: User)
        fun onAddFriendClick(user: User)
    }

    fun setFriendList(friends: List<User>) {
        currentFriendIds.clear()
        currentFriendIds.addAll(friends.map { it.id })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.tvName.text = user.name
            binding.ivAvatar.load(user.avatar)

            val isFriend = currentFriendIds.contains(user.id)

            if (isFriend) {
                binding.btnAddFriend.visibility = View.GONE
                binding.root.setOnClickListener { showPopupMenu(it, user) }
            } else {
                binding.btnAddFriend.visibility = View.VISIBLE
                binding.btnAddFriend.setOnClickListener { listener.onAddFriendClick(user) }
                binding.root.setOnClickListener(null) // Remove click listener for non-friends
            }
        }

        private fun showPopupMenu(view: View, user: User) {
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.menu_friend_options)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_send_message -> {
                        listener.onSendMessageClick(user)
                        true
                    }
                    R.id.menu_view_profile -> {
                        listener.onViewProfileClick(user)
                        true
                    }
                    R.id.menu_remove_friend -> {
                        listener.onRemoveFriendClick(user)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }
}
