package br.com.gdgbrasilia.meetup.app.util.extensions

import android.content.Context
import android.content.Intent
import android.os.Bundle

fun <T> Context.startActivity(type: Class<T>, bundle: Bundle = Bundle()) {
    val intent = Intent(this, type)
    intent.putExtras(bundle)
    startActivity(intent)
}