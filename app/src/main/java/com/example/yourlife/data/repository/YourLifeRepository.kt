package com.example.yourlife.data.repository

import com.example.yourlife.data.model.*
import com.example.yourlife.data.remote.RetrofitInstance
import com.example.yourlife.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class YourLifeRepository {

    private val api = RetrofitInstance.api

    private fun <T> handleResponse(response: Response<T>): Resource<T> {
        return if (response.isSuccessful) {
            response.body()?.let {
                Resource.Success(it)
            } ?: Resource.Error("Resposta vazia do servidor")
        } else {
            Resource.Error("Erro: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun register(name: String, email: String, password: String): Resource<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.register(RegisterRequest(name, email, password))
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun login(email: String, password: String): Resource<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.login(LoginRequest(email, password))
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getCurrentUser(token: String): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCurrentUser("Bearer $token")
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getUserById(token: String, userId: Int): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserById("Bearer $token", userId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getUserPosts(token: String, userId: Int): Resource<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserPosts("Bearer $token", userId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getUserFriends(token: String, userId: Int): Resource<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserFriends("Bearer $token", userId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun updateProfile(token: String, request: UpdateProfileRequest): Resource<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.updateProfile("Bearer $token", request)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun searchUsers(token: String, query: String): Resource<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchUsers("Bearer $token", query)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getFeed(token: String): Resource<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getFeed("Bearer $token")
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun createPost(token: String, content: String): Resource<CreatePostResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.createPost("Bearer $token", CreatePostRequest(content))
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }

    suspend fun likePost(token: String, postId: Int): Resource<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.likePost("Bearer $token", postId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun unlikePost(token: String, postId: Int): Resource<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.unlikePost("Bearer $token", postId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getComments(token: String, postId: Int): Resource<List<Comment>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getComments("Bearer $token", postId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun createComment(token: String, postId: Int, content: String): Resource<CreateCommentResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.createComment("Bearer $token", postId, CreateCommentRequest(content))
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getFriends(token: String): Resource<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getFriends("Bearer $token")
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getFriendRequests(token: String): Resource<List<FriendRequest>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getFriendRequests("Bearer $token")
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getFriendshipStatus(token: String, userId: Int): Resource<FriendshipStatus> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getFriendshipStatus("Bearer $token", userId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun sendFriendRequest(token: String, friendId: Int): Resource<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.sendFriendRequest("Bearer $token", FriendRequestData(friendId))
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun removeFriend(token: String, friendId: Int): Resource<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.removeFriend("Bearer $token", friendId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun acceptFriendRequest(token: String, requesterId: Int): Resource<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.acceptFriendRequest("Bearer $token", requesterId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun rejectFriendRequest(token: String, requesterId: Int): Resource<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.rejectFriendRequest("Bearer $token", requesterId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getConversations(token: String): Resource<List<Conversation>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getConversations("Bearer $token")
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getMessages(token: String, userId: Int): Resource<List<Message>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMessages("Bearer $token", userId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun sendMessage(token: String, toUserId: Int, content: String): Resource<SendMessageResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.sendMessage("Bearer $token", SendMessageRequest(toUserId, content))
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getNotifications(token: String): Resource<List<Notification>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getNotifications("Bearer $token")
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun getAdvices(token: String, category: String? = null): Resource<List<Advice>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getAdvices("Bearer $token", category)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }
}
