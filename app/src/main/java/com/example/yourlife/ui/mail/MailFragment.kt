package com.example.yourlife.ui.mail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yourlife.databinding.FragmentMailBinding
import com.example.yourlife.util.Resource

class MailFragment : Fragment() {

    private var _binding: FragmentMailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MailViewModel
    private lateinit var conversationAdapter: ConversationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MailViewModel::class.java)

        setupRecyclerView()
        setupObservers()

        viewModel.getConversations()
    }

    private fun setupRecyclerView() {
        conversationAdapter = ConversationAdapter()
        binding.rvConversations.adapter = conversationAdapter
    }

    private fun setupObservers() {
        viewModel.conversations.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                conversationAdapter.submitList(resource.data)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
