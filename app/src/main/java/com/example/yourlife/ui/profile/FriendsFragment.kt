package com.example.yourlife.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourlife.R
import com.example.yourlife.ui.friends.FriendsAdapter
import com.example.yourlife.util.Resource

class FriendsFragment : Fragment() {

    private lateinit var rvFriends: RecyclerView
    private lateinit var friendsAdapter: FriendsAdapter

    private val viewModel: ProfileViewModel by activityViewModels()
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friends, container, false)
        rvFriends = view.findViewById(R.id.rv_friends)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt(ProfileActivity.USER_ID) ?: -1

        if (userId != -1) {
            setupRecyclerView()
            setupObservers()
            viewModel.fetchUserFriends(userId)
        } else {
            Toast.makeText(requireContext(), "ID do usuário não encontrado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        friendsAdapter = FriendsAdapter(
            onFriendClick = { user ->
                // Navegar para o perfil do amigo
                val intent = android.content.Intent(requireContext(), ProfileActivity::class.java)
                intent.putExtra(ProfileActivity.USER_ID, user.id)
                startActivity(intent)
            }
        )
        rvFriends.apply {
            adapter = friendsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {
        viewModel.friends.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    friendsAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    // Pode exibir um indicador de progresso
                }
            }
        }
    }
}
