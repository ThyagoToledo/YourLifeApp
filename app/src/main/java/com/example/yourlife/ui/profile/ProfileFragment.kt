package com.example.yourlife.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.yourlife.MainActivity
import com.example.yourlife.R
import com.example.yourlife.databinding.FragmentProfileBinding
import com.example.yourlife.ui.mail.MessageActivity
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel
    private var userId: Int = -1
    private var isOwnProfile: Boolean = true
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt("USER_ID", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        // Responsive banner
        val displayMetrics = requireContext().resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val height = screenWidth * 9 / 16
        binding.ivCoverImage.layoutParams.height = height

        currentUserId = TokenManager.getUserId(requireContext())
        isOwnProfile = userId == -1 || userId == currentUserId

        // Define o userId correto para buscar posts e amigos
        val profileUserId = if (userId == -1) currentUserId else userId

        setupObservers()
        setupListeners(profileUserId)

        if (isOwnProfile) {
            // Viewing own profile
            viewModel.getUser()
        } else {
            // Viewing another user's profile
            viewModel.getUserById(userId)
        }

        // Buscar contagens de posts e amigos
        if (profileUserId != -1) {
            viewModel.fetchUserPosts(profileUserId)
            viewModel.fetchUserFriends(profileUserId)
        }
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    resource.data?.let {
                        binding.ivCoverImage.load(it.coverImage) {
                            placeholder(R.drawable.bg_cover_placeholder)
                            error(R.drawable.bg_cover_placeholder)
                        }
                        binding.ivAvatar.load(it.avatar) {
                            placeholder(R.drawable.ic_avatar_placeholder)
                        }
                        binding.tvUserName.text = it.name
                        binding.tvBio.text = it.bio

                        // Usar valores da API se disponíveis
                        if (it.postsCount != null) {
                            binding.tvPostsCount.text = it.postsCount.toString()
                        }
                        if (it.friendsCount != null) {
                            binding.tvFriendsCount.text = it.friendsCount.toString()
                        }

                        if (it.interests.isNullOrEmpty()) {
                            binding.tvInterests.visibility = View.GONE
                        } else {
                            binding.tvInterests.visibility = View.VISIBLE
                            binding.tvInterests.text = "Interesses: ${it.interests.joinToString()}"
                        }
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        // Observer para atualizar contagem de posts
        viewModel.posts.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { posts ->
                        binding.tvPostsCount.text = posts.size.toString()
                    }
                }
                else -> {}
            }
        }

        // Observer para atualizar contagem de amigos
        viewModel.friends.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { friends ->
                        binding.tvFriendsCount.text = friends.size.toString()
                    }
                }
                else -> {}
            }
        }
    }

    private fun setupListeners(profileUserId: Int) {
        if (!isOwnProfile) {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                }
            })
        } else {
            binding.btnLogout.setOnClickListener {
                viewModel.logout()
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        binding.layoutPosts.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra(ProfileActivity.USER_ID, profileUserId)
            intent.putExtra("TAB_INDEX", 0) // Aba de Posts
            startActivity(intent)
        }

        binding.layoutFriends.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra(ProfileActivity.USER_ID, profileUserId)
            intent.putExtra("TAB_INDEX", 1) // Aba de Amigos
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
