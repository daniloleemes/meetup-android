package br.com.firstsoft.opentheater.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import br.com.firstsoft.opentheater.activities.MainActivity
import br.com.firstsoft.opentheater.application.AppApplication
import br.com.firstsoft.opentheater.service.MovieService

/**
 * Created by danilolemes on 01/03/2018.
 */
class LauncherViewModel : ViewModel() {

    private val movieService by lazy { MovieService() }

    fun fetchGenres(activity: AppCompatActivity) {
        movieService.fetchGenres { appResponse, throwable ->
            throwable?.printStackTrace()
            appResponse?.let {
                it.genres?.let {
                    AppApplication.genres.addAll(it)
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                    activity.finish()
                }
            }
        }
    }

}