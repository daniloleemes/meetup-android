package br.com.gdgbrasilia.meetup.app.data.di.components

import android.content.SharedPreferences
import br.com.gdgbrasilia.meetup.app.data.di.modules.AppModule
import br.com.gdgbrasilia.meetup.app.data.di.modules.NetModule
import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class])
interface NetComponent {

    fun gson(): Gson
    fun retrofit(): Retrofit
    fun okHttpClient(): OkHttpClient
    fun sharedPreferences(): SharedPreferences

}