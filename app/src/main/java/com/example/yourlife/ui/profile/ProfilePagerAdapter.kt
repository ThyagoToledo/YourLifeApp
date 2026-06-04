package com.example.yourlife.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfilePagerAdapter(fragmentActivity: FragmentActivity, private val userId: Int) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> PostsFragment()
            1 -> FriendsFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
        fragment.arguments = Bundle().apply {
            putInt(ProfileActivity.USER_ID, userId)
        }
        return fragment
    }
}
