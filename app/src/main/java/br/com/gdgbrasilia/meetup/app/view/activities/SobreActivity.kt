package br.com.gdgbrasilia.meetup.app.view.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.util.extensions.getViewModel
import br.com.gdgbrasilia.meetup.app.view.viewmodel.PoliticaViewModel
import kotlinx.android.synthetic.main.toolbar_main.*

class SobreActivity : AppCompatActivity() {

    private val politicaViewModel by lazy { getViewModel(PoliticaViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sobre)
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Sobre"
    }
}
