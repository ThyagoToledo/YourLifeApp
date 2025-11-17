package com.example.yourlife.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourlife.data.model.ApiResponse
import com.example.yourlife.data.model.CreatePostResponse
import com.example.yourlife.data.model.Post
import com.example.yourlife.data.repository.YourLifeRepository
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = YourLifeRepository()

    private val _feed = MutableLiveData<Resource<List<Post>>>()
    val feed: LiveData<Resource<List<Post>>> = _feed

    private val _createPostState = MutableLiveData<Resource<CreatePostResponse>>()
    val createPostState: LiveData<Resource<CreatePostResponse>> = _createPostState

    private val _likeState = MutableLiveData<Resource<ApiResponse>>()
    val likeState: LiveData<Resource<ApiResponse>> = _likeState

    fun getFeed() {
        viewModelScope.launch {
            _feed.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _feed.value = repository.getFeed(token)
        }
    }

    fun createPost(content: String) {
        viewModelScope.launch {
            _createPostState.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            val result = repository.createPost(token, content)
            if (result is Resource.Success) {
                getFeed()
            }
            _createPostState.value = result
        }
    }

    fun likePost(postId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            val result = repository.likePost(token, postId)
            if (result is Resource.Success) {
                updatePostInFeed(postId, true)
            }
            _likeState.value = result
        }
    }

    fun unlikePost(postId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            val result = repository.unlikePost(token, postId)
            if (result is Resource.Success) {
                updatePostInFeed(postId, false)
            }
            _likeState.value = result
        }
    }

    private fun updatePostInFeed(postId: Int, isLiked: Boolean) {
        val currentFeed = (_feed.value as? Resource.Success)?.data ?: return
        val updatedFeed = currentFeed.map {
            if (it.id == postId) {
                it.copy(isLiked = isLiked, likesCount = if (isLiked) it.likesCount + 1 else it.likesCount - 1)
            } else {
                it
            }
        }
        _feed.value = Resource.Success(updatedFeed)
    }
}
