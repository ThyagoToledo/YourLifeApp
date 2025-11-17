package com.example.yourlife.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yourlife.R
import com.example.yourlife.data.model.Notification
import com.example.yourlife.databinding.ItemNotificationBinding

class NotificationsAdapter(
    private val onNotificationClick: (Notification) -> Unit
) : ListAdapter<Notification, NotificationsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            binding.apply {
                tvTitle.text = notification.type
                tvMessage.text = notification.content
                tvTimestamp.text = ""

                val iconRes = when (notification.type) {
                    "like" -> R.drawable.ic_heart_filled
                    "comment" -> R.drawable.ic_comment
                    "friend_request" -> R.drawable.ic_person_add
                    "friend_accepted" -> R.drawable.ic_person_check
                    "message" -> R.drawable.ic_mail
                    else -> R.drawable.ic_notifications
                }
                ivIcon.setImageResource(iconRes)

                root.alpha = if (!notification.isRead) 1.0f else 0.6f
                root.setBackgroundResource(
                    if (!notification.isRead) R.drawable.bg_notification_unread
                    else R.drawable.bg_notification_read
                )

                root.setOnClickListener {
                    onNotificationClick(notification)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification) =
            oldItem == newItem
    }
}
