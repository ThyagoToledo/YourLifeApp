package com.example.yourlife.ui.mail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yourlife.databinding.ActivityMessageBinding
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private lateinit var viewModel: MessageViewModel
    private lateinit var messageAdapter: MessageAdapter
    private var userId: Int = -1
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("USER_ID", -1)
        userName = intent.getStringExtra("USER_NAME")

        if (userId == -1) {
            finish()
            return
        }

        viewModel = ViewModelProvider(this).get(MessageViewModel::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = userName ?: "Chat"

        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.getMessages(userId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupRecyclerView() {
        val currentUserId = TokenManager.getUserId(this)
        messageAdapter = MessageAdapter(currentUserId)
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@MessageActivity).apply {
                stackFromEnd = true
            }
            adapter = messageAdapter
        }
    }

    private fun setupObservers() {
        viewModel.messages.observe(this) { resource ->
            binding.progressBar.visibility = if (resource is Resource.Loading) View.VISIBLE else View.GONE

            when (resource) {
                is Resource.Success -> {
                    messageAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {}
            }
        }
    }

    private fun setupListeners() {
        binding.btnSend.setOnClickListener {
            val content = binding.etMessage.text.toString().trim()
            if (content.isNotEmpty()) {
                viewModel.sendMessage(userId, content)
                binding.etMessage.text.clear()
            }
        }
    }
}
