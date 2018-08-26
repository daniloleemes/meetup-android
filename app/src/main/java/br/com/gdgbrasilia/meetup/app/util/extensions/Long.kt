package br.com.gdgbrasilia.meetup.app.util.extensions

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Long.displayAsHour(): String {
    return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(this),
            TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this)))
}

fun Long.displayAsDate(): String {
    return "".let {
        val dateFormatter = SimpleDateFormat()
        val date = Date(this)
        dateFormatter.applyPattern("dd/MM/yyyy")
        dateFormatter.format(date)
    }
}