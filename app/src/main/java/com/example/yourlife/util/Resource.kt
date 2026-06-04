package com.example.yourlife.util

/**
 * Classe sealed para encapsular estados de resposta da API
 * Facilita o tratamento de sucesso, erro e loading
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}

