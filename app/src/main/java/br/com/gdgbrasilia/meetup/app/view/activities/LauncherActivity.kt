package br.com.gdgbrasilia.meetup.app.view.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.gdgbrasilia.meetup.R
import br.com.gdgbrasilia.meetup.app.data.AppStatics
import br.com.gdgbrasilia.meetup.app.util.extensions.getViewModel
import br.com.gdgbrasilia.meetup.app.view.viewmodel.LauncherViewModel

class LauncherActivity : AppCompatActivity() {

    private val launcherVM by lazy { getViewModel(LauncherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        launcherVM.fetchGenres { genres ->
            genres?.let {
                AppStatics.genres.addAll(it)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
