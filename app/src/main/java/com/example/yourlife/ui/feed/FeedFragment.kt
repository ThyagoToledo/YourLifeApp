package com.example.yourlife.ui.feed

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yourlife.data.model.Post
import com.example.yourlife.databinding.FragmentFeedBinding
import com.example.yourlife.ui.comment.CommentActivity
import com.example.yourlife.util.Resource

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FeedViewModel
    private lateinit var feedAdapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.getFeed()
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
                val intent = Intent(requireContext(), CommentActivity::class.java).apply {
                    putExtra("POST_ID", post.id)
                }
                startActivity(intent)
            }
        )

        binding.rvFeed.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = feedAdapter
        }
    }

    private fun setupObservers() {
        // Observer para o feed de posts
        viewModel.feed.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is Resource.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    resource.data?.let { posts ->
                        feedAdapter.submitList(posts)
                    }
                }
                is Resource.Error -> {
                    binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(requireContext(), resource.message ?: "Erro ao carregar feed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observer para likes
        viewModel.likeState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    // A atualização agora é tratada no ViewModel
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message ?: "Erro ao curtir", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    // Pode adicionar indicador de loading se necessário
                }
            }
        }

        // Observer para criar post
        viewModel.createPostState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Post criado com sucesso!", Toast.LENGTH_SHORT).show()
                    binding.etNewPost.text?.clear()
                    binding.cbIsAdvice.isChecked = false
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message ?: "Erro ao criar post", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    // Pode adicionar indicador de loading
                }
            }
        }
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getFeed()
        }

        binding.btnPost.setOnClickListener {
            val content = binding.etNewPost.text.toString().trim()

            if (content.isNotEmpty()) {
                viewModel.createPost(content)
            } else {
                Toast.makeText(requireContext(), "Digite algo para postar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
