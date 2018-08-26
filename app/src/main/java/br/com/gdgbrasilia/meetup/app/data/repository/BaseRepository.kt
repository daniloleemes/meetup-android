package br.com.firstsoft.opentheater.repository

import android.content.Context
import br.com.firstsoft.opentheater.R
import br.com.firstsoft.opentheater.application.AppApplication
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by danilolemes on 28/02/2018.
 */
class BaseRepository {

    companion object {

        val gson by lazy { GsonBuilder().create() }

        private val okHttpClient by lazy {
            prepareOkHttpClient(AppApplication.instance.applicationContext)
                    .cache(Cache(AppApplication.instance.applicationContext.cacheDir, 10 * 1024 * 1024))
                    .build()
        }

        private val retrofit by lazy {
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(AppApplication.BASE_URL)
                    .client(okHttpClient)
                    .build()
        }

        private fun prepareOkHttpClient(context: Context): OkHttpClient.Builder {
            return OkHttpClient.Builder()
                    .readTimeout(2, TimeUnit.MINUTES)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val originalHttpUrl = original.url()

                        val url = originalHttpUrl.newBuilder()
                                .addQueryParameter("api_key", context.getString(R.string.token))
                                .build()

                        val requestBuilder = original.newBuilder()
                                .url(url)

                        val request = requestBuilder.build()
                        val response = chain.proceed(request)

                        response
                    }
        }


        fun createMovieRepository(): MovieRepository = retrofit.create(MovieRepository::class.java)
    }
}