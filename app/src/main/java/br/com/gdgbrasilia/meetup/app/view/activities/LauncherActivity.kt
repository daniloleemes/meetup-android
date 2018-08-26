package br.com.firstsoft.opentheater.activities

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.firstsoft.opentheater.R
import br.com.firstsoft.opentheater.viewmodel.LauncherViewModel

class LauncherActivity : AppCompatActivity() {

    private val launcherVM by lazy { ViewModelProviders.of(this).get(LauncherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        launcherVM.fetchGenres(this)
    }
}
