package br.com.gdgbrasilia.meetup.app.providers

import android.net.Uri

/**
 * Created by danilolemes on 30/12/2017.
 */
class ImageFileProvider : android.support.v4.content.FileProvider() {
    override fun getType(uri: Uri): String = "image/jpeg"
}