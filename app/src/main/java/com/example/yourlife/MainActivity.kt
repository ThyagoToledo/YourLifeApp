package com.example.yourlife

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.yourlife.databinding.ActivityMainBinding
import com.example.yourlife.ui.auth.AuthViewModel
import com.example.yourlife.ui.feed.FeedActivity
import com.example.yourlife.util.Resource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        // Verificar se já está autenticado
        if (authViewModel.isAuthenticated()) {
            // Usuário já está logado, pode carregar a tela principal
            showMainContent()
        } else {
            // Mostrar tela de login
            showLoginScreen()
        }
    }

    private fun showLoginScreen() {
        binding.apply {
            // Configurar listeners dos botões
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (validateLoginInputs(email, password)) {
                    authViewModel.login(email, password)
                }
            }

            btnRegister.setOnClickListener {
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (validateRegisterInputs(name, email, password)) {
                    authViewModel.register(name, email, password)
                }
            }
        }

        // Observar estado do login
        authViewModel.loginState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    if (resource.data?.success == true) {
                        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        showMainContent()
                    } else {
                        Toast.makeText(this, resource.data?.error ?: "Erro no login", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(this, resource.message ?: "Erro desconhecido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observar estado do registro
        authViewModel.registerState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    if (resource.data?.success == true) {
                        Toast.makeText(this, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        showMainContent()
                    } else {
                        Toast.makeText(this, resource.data?.error ?: "Erro no registro", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(this, resource.message ?: "Erro desconhecido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMainContent() {
        val intent = Intent(this, FeedActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateLoginInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "Digite o email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Digite a senha", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateRegisterInputs(name: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(this, "Digite o nome", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Digite o email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Digite a senha", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "Senha deve ter no mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnLogin.isEnabled = !show
        binding.btnRegister.isEnabled = !show
    }
}

