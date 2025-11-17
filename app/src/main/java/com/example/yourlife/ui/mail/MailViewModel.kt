package com.example.yourlife.ui.mail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourlife.data.model.Conversation
import com.example.yourlife.data.repository.YourLifeRepository
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager
import kotlinx.coroutines.launch

class MailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = YourLifeRepository()

    private val _conversations = MutableLiveData<Resource<List<Conversation>>>()
    val conversations: LiveData<Resource<List<Conversation>>> = _conversations

    fun getConversations() {
        viewModelScope.launch {
            _conversations.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _conversations.value = repository.getConversations(token)
        }
    }
}
