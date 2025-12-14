package com.example.yourlife.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourlife.data.model.ApiResponse
import com.example.yourlife.data.model.Post
import com.example.yourlife.data.model.UpdateProfileRequest
import com.example.yourlife.data.model.User
import com.example.yourlife.data.repository.YourLifeRepository
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = YourLifeRepository()

    private val _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>> = _user

    private val _posts = MutableLiveData<Resource<List<Post>>>()
    val posts: LiveData<Resource<List<Post>>> = _posts

    private val _friends = MutableLiveData<Resource<List<User>>>()
    val friends: LiveData<Resource<List<User>>> = _friends

    private val _updateState = MutableLiveData<Resource<ApiResponse>>()
    val updateState: LiveData<Resource<ApiResponse>> = _updateState

    fun getUser() {
        viewModelScope.launch {
            _user.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _user.value = repository.getCurrentUser(token)
        }
    }

    fun getUserById(userId: Int) {
        viewModelScope.launch {
            _user.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _user.value = repository.getUserById(token, userId)
        }
    }

    fun fetchUserProfile(userId: Int) {
        viewModelScope.launch {
            _user.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _user.value = repository.getUserById(token, userId)
        }
    }

    fun fetchUserPosts(userId: Int) {
        viewModelScope.launch {
            _posts.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _posts.value = repository.getUserPosts(token, userId)
        }
    }

    fun fetchUserFriends(userId: Int) {
        viewModelScope.launch {
            _friends.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _friends.value = repository.getUserFriends(token, userId)
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

    fun likePost(postId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            val result = repository.likePost(token, postId)
            if (result is Resource.Success) {
                updatePostInList(postId, true)
            }
        }
    }

    fun unlikePost(postId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            val result = repository.unlikePost(token, postId)
            if (result is Resource.Success) {
                updatePostInList(postId, false)
            }
        }
    }

    private fun updatePostInList(postId: Int, isLiked: Boolean) {
        val currentPosts = (_posts.value as? Resource.Success)?.data ?: return
        val updatedPosts = currentPosts.map {
            if (it.id == postId) {
                it.copy(isLiked = isLiked, likesCount = if (isLiked) it.likesCount + 1 else it.likesCount - 1)
            } else {
                it
            }
        }
        _posts.value = Resource.Success(updatedPosts)
    }

    fun logout() {
        TokenManager.clearAll(getApplication())
    }
}
