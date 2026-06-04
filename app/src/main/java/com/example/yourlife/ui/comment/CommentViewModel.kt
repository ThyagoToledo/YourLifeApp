package com.example.yourlife.ui.comment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourlife.data.model.Comment
import com.example.yourlife.data.model.CreateCommentResponse
import com.example.yourlife.data.repository.YourLifeRepository
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager
import kotlinx.coroutines.launch

class CommentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = YourLifeRepository()

    private val _comments = MutableLiveData<Resource<List<Comment>>>()
    val comments: LiveData<Resource<List<Comment>>> = _comments

    private val _createCommentState = MutableLiveData<Resource<CreateCommentResponse>>()
    val createCommentState: LiveData<Resource<CreateCommentResponse>> = _createCommentState

    fun getComments(postId: Int) {
        viewModelScope.launch {
            _comments.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _comments.value = repository.getComments(token, postId)
        }
    }

    fun createComment(postId: Int, content: String) {
        viewModelScope.launch {
            _createCommentState.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            val result = repository.createComment(token, postId, content)
            _createCommentState.value = result
            if (result is Resource.Success) {
                getComments(postId)
            }
        }
    }
}
