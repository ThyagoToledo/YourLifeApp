package com.example.yourlife.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Utilitários para formatação de datas
 */
object DateUtils {

    /**
     * Formata timestamp do backend (ISO 8601) para formato relativo
     * Exemplo: "2h atrás", "5min atrás", "1d atrás"
     */
    fun formatRelativeTime(timestamp: String?): String {
        if (timestamp.isNullOrEmpty()) return ""

        try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            format.timeZone = TimeZone.getTimeZone("UTC")
            val date = format.parse(timestamp) ?: return ""

            val now = System.currentTimeMillis()
            val diff = now - date.time

            return when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "Agora"
                diff < TimeUnit.HOURS.toMillis(1) -> {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
                    "${minutes}min atrás"
                }
                diff < TimeUnit.DAYS.toMillis(1) -> {
                    val hours = TimeUnit.MILLISECONDS.toHours(diff)
                    "${hours}h atrás"
                }
                diff < TimeUnit.DAYS.toMillis(7) -> {
                    val days = TimeUnit.MILLISECONDS.toDays(diff)
                    "${days}d atrás"
                }
                else -> {
                    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    outputFormat.format(date)
                }
            }
        } catch (e: Exception) {
            return ""
        }
    }

    /**
     * Formata para hora simples (HH:mm)
     */
    fun formatTime(timestamp: String?): String {
        if (timestamp.isNullOrEmpty()) return ""

        try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            format.timeZone = TimeZone.getTimeZone("UTC")
            val date = format.parse(timestamp) ?: return ""

            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            return outputFormat.format(date)
        } catch (e: Exception) {
            return ""
        }
    }
}

