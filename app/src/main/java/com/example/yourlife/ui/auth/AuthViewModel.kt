package com.example.yourlife.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourlife.data.model.AuthResponse
import com.example.yourlife.data.repository.YourLifeRepository
import com.example.yourlife.util.NetworkUtils
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager
import kotlinx.coroutines.launch

/**
 * ViewModel para gerenciar autenticação (Login/Registro)
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = YourLifeRepository()

    private val _loginState = MutableLiveData<Resource<AuthResponse>>()
    val loginState: LiveData<Resource<AuthResponse>> = _loginState

    private val _registerState = MutableLiveData<Resource<AuthResponse>>()
    val registerState: LiveData<Resource<AuthResponse>> = _registerState

    /**
     * Realiza login
     */
    fun login(email: String, password: String) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            _loginState.value = Resource.Error("Sem conexão com a internet")
            return
        }

        viewModelScope.launch {
            _loginState.value = Resource.Loading()

            val result = repository.login(email, password)
            _loginState.value = result

            // Salvar token se sucesso
            if (result is Resource.Success && result.data?.success == true) {
                result.data.token?.let { token ->
                    TokenManager.saveToken(getApplication(), token)

                    result.data.user?.let { user ->
                        TokenManager.saveUserInfo(
                            getApplication(),
                            user.id,
                            user.name,
                            user.email
                        )
                    }
                }
            }
        }
    }

    /**
     * Realiza registro
     */
    fun register(name: String, email: String, password: String) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            _registerState.value = Resource.Error("Sem conexão com a internet")
            return
        }

        viewModelScope.launch {
            _registerState.value = Resource.Loading()

            val result = repository.register(name, email, password)
            _registerState.value = result

            // Salvar token se sucesso
            if (result is Resource.Success && result.data?.success == true) {
                result.data.token?.let { token ->
                    TokenManager.saveToken(getApplication(), token)

                    result.data.user?.let { user ->
                        TokenManager.saveUserInfo(
                            getApplication(),
                            user.id,
                            user.name,
                            user.email
                        )
                    }
                }
            }
        }
    }

    /**
     * Verifica se usuário está autenticado
     */
    fun isAuthenticated(): Boolean {
        return TokenManager.hasToken(getApplication())
    }

    /**
     * Faz logout
     */
    fun logout() {
        TokenManager.clearAll(getApplication())
    }
}

