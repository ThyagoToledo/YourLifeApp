package com.example.yourlife.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourlife.data.model.ApiResponse
import com.example.yourlife.data.model.FriendshipStatus
import com.example.yourlife.data.model.UpdateProfileRequest
import com.example.yourlife.data.model.User
import com.example.yourlife.data.repository.YourLifeRepository
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = YourLifeRepository()

    private val _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>> = _user

    private val _updateState = MutableLiveData<Resource<ApiResponse>>()
    val updateState: LiveData<Resource<ApiResponse>> = _updateState

    private val _friendshipStatus = MutableLiveData<Resource<FriendshipStatus>>()
    val friendshipStatus: LiveData<Resource<FriendshipStatus>> = _friendshipStatus

    private val _friendActionState = MutableLiveData<Resource<ApiResponse>>()
    val friendActionState: LiveData<Resource<ApiResponse>> = _friendActionState

    fun getUser() {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            val userId = TokenManager.getUserId(getApplication())

            // Busca o usuário
            val userResult = repository.getUserById(token, userId)

            if (userResult is Resource.Success && userResult.data != null) {
                // Busca estatísticas em paralelo
                val feedDeferred = async { repository.getFeed(token) }
                val friendsDeferred = async { repository.getFriends(token) }

                val feedResult = feedDeferred.await()
                val friendsResult = friendsDeferred.await()

                // Conta posts do usuário
                val postsCount = if (feedResult is Resource.Success) {
                    feedResult.data?.count { it.userId == userId } ?: 0
                } else {
                    0
                }

                // Conta amigos
                val friendsCount = if (friendsResult is Resource.Success) {
                    friendsResult.data?.size ?: 0
                } else {
                    0
                }

                // Cria usuário com estatísticas atualizadas
                val updatedUser = userResult.data.copy(
                    postsCount = postsCount,
                    friendsCount = friendsCount
                )

                _user.value = Resource.Success(updatedUser)
            } else {
                _user.value = userResult
            }
        }
    }

    fun getUserById(userId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch

            // Busca o usuário. A resposta do servidor já deve conter a contagem de amigos (friendsCount).
            val userResult = repository.getUserById(token, userId)

            if (userResult is Resource.Success && userResult.data != null) {
                // Busca o feed para contar os posts do usuário (lógica existente).
                val feedResult = repository.getFeed(token)

                // Conta posts do usuário
                val postsCount = if (feedResult is Resource.Success) {
                    feedResult.data?.count { it.userId == userId } ?: 0
                } else {
                    0
                }

                // O friendsCount já vem do userResult.data.friendsCount e não precisa ser recalculado.
                // Apenas atualizamos o postsCount, mantendo o friendsCount do servidor.
                val updatedUser = userResult.data.copy(
                    postsCount = postsCount
                )

                _user.value = Resource.Success(updatedUser)
            } else {
                _user.value = userResult
            }
        }
    }

    fun updateProfile(name: String, bio: String, avatarUrl: String, coverUrl: String, interests: String) {
        viewModelScope.launch {
            _updateState.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            val interestsList = interests.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            val request = UpdateProfileRequest(
                name = name.ifEmpty { null },
                bio = bio.ifEmpty { null },
                avatar = avatarUrl.ifEmpty { null },
                coverImage = coverUrl.ifEmpty { null },
                interests = interestsList.ifEmpty { null }
            )
            _updateState.value = repository.updateProfile(token, request)
        }
    }

    fun getFriendshipStatus(userId: Int) {
        viewModelScope.launch {
            _friendshipStatus.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _friendshipStatus.value = repository.getFriendshipStatus(token, userId)
        }
    }

    fun sendFriendRequest(userId: Int) {
        viewModelScope.launch {
            _friendActionState.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _friendActionState.value = repository.sendFriendRequest(token, userId)
        }
    }

    fun removeFriend(userId: Int) {
        viewModelScope.launch {
            _friendActionState.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _friendActionState.value = repository.removeFriend(token, userId)
        }
    }

    fun logout() {
        TokenManager.clearAll(getApplication())
    }
}
