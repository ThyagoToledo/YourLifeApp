package com.example.yourlife.ui.feed

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.yourlife.MainActivity
import com.example.yourlife.R
import com.example.yourlife.databinding.ActivityFeedBinding
import com.example.yourlife.ui.advice.AdviceFragment
import com.example.yourlife.ui.auth.AuthViewModel
import com.example.yourlife.ui.friends.FriendsFragment
import com.example.yourlife.ui.mail.MailFragment
import com.example.yourlife.ui.notifications.NotificationsActivity
import com.example.yourlife.ui.profile.ProfileFragment

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = AuthViewModel(application)

        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, FeedFragment())
                .commit()
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_feed -> selectedFragment = FeedFragment()
                R.id.navigation_profile -> selectedFragment = ProfileFragment()
                R.id.navigation_friends -> selectedFragment = FriendsFragment()
                R.id.navigation_advice -> selectedFragment = AdviceFragment()
                R.id.navigation_mail -> selectedFragment = MailFragment()
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, selectedFragment)
                    .commit()
            }

            true
        }
    }

    fun navigateToUserProfile(userId: Int) {
        val profileFragment = ProfileFragment().apply {
            arguments = Bundle().apply {
                putInt("USER_ID", userId)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, profileFragment)
            .addToBackStack(null) // Permite voltar para a tela anterior
            .commit()
        
        // Atualiza a aparência da barra de navegação sem acionar o listener
        binding.bottomNav.menu.findItem(R.id.navigation_profile).isChecked = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_notifications -> {
                val intent = Intent(this, NotificationsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_settings -> {
                showSettingsMenu()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSettingsMenu() {
        val anchorView = findViewById<View>(R.id.action_settings) ?: binding.toolbar
        val popup = PopupMenu(this, anchorView)
        popup.menuInflater.inflate(R.menu.settings_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    authViewModel.logout()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}
