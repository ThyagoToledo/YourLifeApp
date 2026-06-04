package com.example.yourlife.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourlife.data.model.Notification
import com.example.yourlife.data.repository.YourLifeRepository
import com.example.yourlife.util.Resource
import com.example.yourlife.util.TokenManager
import kotlinx.coroutines.launch

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = YourLifeRepository()

    private val _notifications = MutableLiveData<Resource<List<Notification>>>()
    val notifications: LiveData<Resource<List<Notification>>> = _notifications

    fun getNotifications() {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            _notifications.value = repository.getNotifications(token)
        }
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            // repository.markNotificationAsRead(token, notificationId)
            getNotifications()
        }
    }
}
