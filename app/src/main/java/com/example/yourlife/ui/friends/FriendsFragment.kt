package com.example.yourlife.ui.friends

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yourlife.data.model.User
import com.example.yourlife.databinding.FragmentFriendsBinding
import com.example.yourlife.ui.feed.FeedActivity
import com.example.yourlife.ui.mail.MessageActivity
import com.example.yourlife.util.Resource

class FriendsFragment : Fragment(), FriendAdapter.OnFriendInteractionListener {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FriendsViewModel
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var friendRequestAdapter: FriendRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FriendsViewModel::class.java]

        setupRecyclerViews()
        setupObservers()
        setupListeners()

        viewModel.getFriends()
        viewModel.getFriendRequests()
    }

    private fun setupRecyclerViews() {
        friendAdapter = FriendAdapter(this)
        friendRequestAdapter = FriendRequestAdapter(
            onAccept = { viewModel.acceptFriendRequest(it.id) },
            onReject = { viewModel.rejectFriendRequest(it.id) }
        )

        binding.rvFriends.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = friendAdapter
        }

        binding.rvFriendRequests.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = friendRequestAdapter
        }
    }

    private fun setupObservers() {
        viewModel.friends.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                resource.data?.let {
                    friendAdapter.setFriendList(it)
                    // If search is not active, show friend list
                    if (binding.etSearch.text.isNullOrEmpty()) {
                        friendAdapter.submitList(it)
                    }
                }
            }
        }

        viewModel.friendRequests.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                friendRequestAdapter.submitList(resource.data)
            }
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                // Only update if search is active
                if (binding.etSearch.text.isNotEmpty()) {
                    friendAdapter.submitList(resource.data)
                }
            }
        }
    }

    private fun setupListeners() {
        binding.etSearch.addTextChangedListener {
            val query = it.toString()
            if (query.length > 2) {
                viewModel.searchUsers(query)
            } else if (query.isEmpty()) {
                viewModel.getFriends() // Reload original friends list
            }
        }
    }

    override fun onSendMessageClick(user: User) {
        val intent = Intent(requireContext(), MessageActivity::class.java)
        intent.putExtra("USER_ID", user.id)
        intent.putExtra("USER_NAME", user.name)
        startActivity(intent)
    }

    override fun onViewProfileClick(user: User) {
        (activity as? FeedActivity)?.navigateToUserProfile(user.id)
    }

    override fun onRemoveFriendClick(user: User) {
        viewModel.removeFriend(user.id)
        Toast.makeText(requireContext(), "Amizade com ${user.name} desfeita", Toast.LENGTH_SHORT).show()
    }

    override fun onAddFriendClick(user: User) {
        viewModel.sendFriendRequest(user.id)
        Toast.makeText(requireContext(), "Pedido de amizade enviado para ${user.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
