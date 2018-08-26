@file:JvmName("ExtensionsUtils")

package br.com.gdgbrasilia.meetup.app.util.extensions

import br.com.gdgbrasilia.meetup.R
import br.com.gdgbrasilia.meetup.app.data.AppApplication
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import java.util.*


fun getGlideUrl(url: String): GlideUrl {
    val sharedPreferences = AppApplication.instance.applicationContext.getSharedPreferences(AppApplication.instance.applicationContext.getString(R.string.app_name), 0)
    val cookie = sharedPreferences.getString(AppApplication.instance.applicationContext.getString(R.string.cookie), "")

    return GlideUrl(url, LazyHeaders.Builder()
            .addHeader("Cookie", cookie)
            .build())
}

fun getDefaultParams(): HashMap<String, String> {
    val map = HashMap<String, String>()
    map["pagina"] = "0"
    map["paginacao"] = "-1"
    return map
}
