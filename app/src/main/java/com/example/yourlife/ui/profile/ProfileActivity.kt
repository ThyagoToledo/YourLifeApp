package com.example.yourlife.ui.profile

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.yourlife.R
import com.example.yourlife.data.model.User
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var ivProfileImage: ImageView
    private lateinit var tvUsername: TextView

    private val viewModel: ProfileViewModel by viewModels()

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        ivProfileImage = findViewById(R.id.iv_profile_image)
        tvUsername = findViewById(R.id.tv_username)
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

        val receivedUserId = intent.getIntExtra(USER_ID, -1)

        val currentUserId = TokenManager.getUserId(this)

        userId = if (receivedUserId != -1) receivedUserId else currentUserId

        if (userId != -1) {
            setupViews()
            setupObservers()
            viewModel.fetchUserProfile(userId)
        } else {
            Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupViews() {
        val adapter = ProfilePagerAdapter(this, userId)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Posts"
                1 -> "Amigos"
                else -> null
            }
        }.attach()
    }

    private fun setupObservers() {
        viewModel.user.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { setupProfileHeader(it) }
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    // Pode exibir um indicador de progresso no cabeçalho
                }
            }
        })
    }

    private fun setupProfileHeader(user: User) {
        tvUsername.text = user.name
        ivProfileImage.load(user.avatar) {
            placeholder(R.drawable.ic_person)
            error(R.drawable.ic_person)
            transformations(coil.transform.CircleCropTransformation())
        }
    }

    companion object {
        const val USER_ID = "USER_ID"
    }
}
