package com.example.yourlife.ui.mail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourlife.data.model.Message
import com.example.yourlife.data.repository.YourLifeRepository
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager
import kotlinx.coroutines.launch

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = YourLifeRepository()

    private val _messages = MutableLiveData<Resource<List<Message>>>()
    val messages: LiveData<Resource<List<Message>>> = _messages

    fun getMessages(friendId: Int) {
        viewModelScope.launch {
            _messages.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            // Corrigindo: Atribuindo o resultado ao LiveData
            _messages.value = repository.getMessages(token, friendId)
        }
    }

    fun sendMessage(friendId: Int, content: String) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            repository.sendMessage(token, friendId, content)
            // Recarrega as mensagens após enviar uma nova
            getMessages(friendId)
        }
    }
}
