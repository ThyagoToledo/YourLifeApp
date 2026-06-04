package com.example.yourlife.ui.advice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourlife.data.model.Advice
import com.example.yourlife.data.repository.YourLifeRepository
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager
import kotlinx.coroutines.launch

class AdviceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = YourLifeRepository()

    private val _advices = MutableLiveData<Resource<List<Advice>>>()
    val advices: LiveData<Resource<List<Advice>>> = _advices

    fun getAdvices() {
        viewModelScope.launch {
            _advices.value = Resource.Loading()
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _advices.value = repository.getAdvices(token)
        }
    }

    fun voteAdvice(adviceId: Int, voteType: String) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            // repository.voteAdvice(token, adviceId, voteType) // Método a ser implementado no repositório
        }
    }
}
