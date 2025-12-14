package com.example.yourlife.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourlife.R
import com.example.yourlife.ui.comment.CommentActivity
import com.example.yourlife.ui.feed.FeedAdapter
import com.example.yourlife.util.Resource

class PostsFragment : Fragment() {

    private lateinit var rvPosts: RecyclerView
    private lateinit var feedAdapter: FeedAdapter

    private val viewModel: ProfileViewModel by activityViewModels()
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)
        rvPosts = view.findViewById(R.id.rv_posts)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt(ProfileActivity.USER_ID) ?: -1

        if (userId != -1) {
            setupRecyclerView()
            setupObservers()
            viewModel.fetchUserPosts(userId)
        } else {
            Toast.makeText(requireContext(), "ID do usuário não encontrado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        feedAdapter = FeedAdapter(
            onLikeClick = { post ->
                if (post.isLiked) {
                    viewModel.unlikePost(post.id)
                } else {
                    viewModel.likePost(post.id)
                }
            },
            onCommentClick = { post ->
                val intent = Intent(requireContext(), CommentActivity::class.java)
                intent.putExtra("POST_ID", post.id)
                startActivity(intent)
            }
        )
        rvPosts.apply {
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {
        viewModel.posts.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    feedAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    // Pode exibir um indicador de progresso
                }
            }
        })
    }
}
