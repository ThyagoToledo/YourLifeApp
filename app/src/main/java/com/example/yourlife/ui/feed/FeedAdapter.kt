package com.example.yourlife.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.yourlife.R
import com.example.yourlife.data.model.Post
import com.example.yourlife.databinding.ItemPostBinding

class FeedAdapter(
    private val onLikeClick: (Post) -> Unit,
    private val onCommentClick: (Post) -> Unit
) : ListAdapter<Post, FeedAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(
        private val binding: ItemPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {
                ivAvatar.load(post.userAvatar ?: "") {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
                    transformations(CircleCropTransformation())
                }

                tvUserName.text = post.userName ?: "Usuário"
                tvTimestamp.text = ""

                badgeAdvice.visibility = if (post.isAdvice) View.VISIBLE else View.GONE

                tvContent.text = post.content

                tvLikesCount.text = "${post.likesCount} curtidas"
                tvCommentsCount.text = "${post.commentsCount} comentários"

                // Atualizar o estado visual do botão de like
                btnLike.isSelected = post.isLiked

                // Usar setImageResource para garantir que a imagem mude
                if (post.isLiked) {
                    btnLike.setImageResource(R.drawable.ic_heart_filled)
                } else {
                    btnLike.setImageResource(R.drawable.ic_heart_outline)
                }

                btnLike.setOnClickListener { onLikeClick(post) }
                btnComment.setOnClickListener { onCommentClick(post) }
            }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}
