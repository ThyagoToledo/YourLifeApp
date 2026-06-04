package com.example.yourlife.ui.comment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yourlife.databinding.ActivityCommentBinding
import com.example.yourlife.util.Resource

class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding
    private lateinit var viewModel: CommentViewModel
    private lateinit var commentAdapter: CommentAdapter
    private var postId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getIntExtra(POST_ID, -1)
        if (postId == -1) {
            finish()
            return
        }

        viewModel = ViewModelProvider(this).get(CommentViewModel::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.getComments(postId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupRecyclerView() {
        commentAdapter = CommentAdapter()
        binding.rvComments.apply {
            layoutManager = LinearLayoutManager(this@CommentActivity)
            adapter = commentAdapter
        }
    }

    private fun setupObservers() {
        viewModel.comments.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    commentAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    // You can show a progress bar here
                }
            }
        }

        viewModel.createCommentState.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.etComment.text.clear()
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    // You can disable the send button here
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnSend.setOnClickListener {
            val content = binding.etComment.text.toString().trim()
            if (content.isNotEmpty()) {
                viewModel.createComment(postId, content)
            }
        }
    }

    companion object {
        const val POST_ID = "POST_ID"
    }
}
