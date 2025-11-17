package com.example.yourlife.ui.profile

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.yourlife.R
import com.example.yourlife.data.model.User
import com.example.yourlife.databinding.ActivityEditProfileBinding
import com.example.yourlife.util.Resource

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra("USER_DATA")

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupForm()
        setupObservers()
    }

    private fun setupForm() {
        user?.let {
            binding.etName.setText(it.name)
            binding.etBio.setText(it.bio)
            binding.etAvatarUrl.setText(it.avatar)
            binding.etCoverUrl.setText(it.coverImage)
            binding.etInterests.setText(it.interests?.joinToString(", "))
        }
    }

    private fun setupObservers() {
        viewModel.updateState.observe(this) { resource ->
            // Invalida o menu para que onPrepareOptionsMenu seja chamado
            invalidateOptionsMenu()
            binding.progressBar.visibility = if (resource is Resource.Loading) View.VISIBLE else View.GONE

            when (resource) {
                is Resource.Success -> {
                    Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message ?: "Ocorreu um erro", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    // Apenas exibe o progresso
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_profile_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val isLoading = viewModel.updateState.value is Resource.Loading
        menu?.findItem(R.id.action_save)?.isEnabled = !isLoading
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val name = binding.etName.text.toString()
                val bio = binding.etBio.text.toString()
                val avatarUrl = binding.etAvatarUrl.text.toString()
                val coverUrl = binding.etCoverUrl.text.toString()
                val interests = binding.etInterests.text.toString()
                viewModel.updateProfile(name, bio, avatarUrl, coverUrl, interests)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
