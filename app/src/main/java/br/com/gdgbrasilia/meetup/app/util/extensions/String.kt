package br.com.gdgbrasilia.meetup.app.util.extensions

import android.annotation.SuppressLint
import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

fun String.formatSite(): String {
    return if (!this.startsWith("http")) "http://$this" else this
}

fun String.isValidEmail() =
        this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

@SuppressLint("SimpleDateFormat")
fun String.displayAsDate(showHours: Boolean = false): String {
    return this.let {
        try {
            val dateFormatter = SimpleDateFormat()
            val date = Date(it.replace("/Date(", "").replace(")/", "").toLong())
            if (showHours) {
                dateFormatter.applyPattern("dd/MM/yyyy HH:mm")
            } else {
                dateFormatter.applyPattern("dd/MM/yyyy")
            }
            dateFormatter.format(date)
        } catch (e: Exception) {
            it
        }
    }
}

fun String.getDateMillis(pattern: String = "dd/MM/yyyy"): Long {
    val dateFormatter = SimpleDateFormat()
    dateFormatter.applyPattern(pattern)
    return dateFormatter.parse(this).time
}