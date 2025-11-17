package com.example.yourlife.ui.friends

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourlife.data.model.FriendRequest
import com.example.yourlife.data.model.User
import com.example.yourlife.data.repository.YourLifeRepository
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager
import kotlinx.coroutines.launch

class FriendsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = YourLifeRepository()

    private val _friends = MutableLiveData<Resource<List<User>>>()
    val friends: LiveData<Resource<List<User>>> = _friends

    private val _friendRequests = MutableLiveData<Resource<List<FriendRequest>>>()
    val friendRequests: LiveData<Resource<List<FriendRequest>>> = _friendRequests

    private val _searchResults = MutableLiveData<Resource<List<User>>>()
    val searchResults: LiveData<Resource<List<User>>> = _searchResults

    fun getFriends() {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _friends.value = repository.getFriends(token)
        }
    }

    fun getFriendRequests() {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _friendRequests.value = repository.getFriendRequests(token)
        }
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _searchResults.value = repository.searchUsers(token, query)
        }
    }

    fun sendFriendRequest(friendId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            repository.sendFriendRequest(token, friendId)
        }
    }

    fun removeFriend(friendId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            repository.removeFriend(token, friendId)
            getFriends()
        }
    }

    fun acceptFriendRequest(requesterId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            repository.acceptFriendRequest(token, requesterId)
            // Refresh lists
            getFriends()
            getFriendRequests()
        }
    }

    fun rejectFriendRequest(requesterId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            repository.rejectFriendRequest(token, requesterId)
            // Refresh list
            getFriendRequests()
        }
    }
}
