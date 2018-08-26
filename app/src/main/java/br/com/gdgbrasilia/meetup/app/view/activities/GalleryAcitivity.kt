package br.com.gdgbrasilia.meetup.app.view.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.GridLayout
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.business.model.IntentFieldNames
import br.com.gdgbrasilia.meetup.app.business.vo.AppFoto
import br.com.gdgbrasilia.meetup.app.view.adapters.GalleryAdapter
import kotlinx.android.synthetic.main.activity_gallery_acitivity.*
import kotlinx.android.synthetic.main.toolbar_main.*

class GalleryAcitivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_acitivity)
        setupToolbar()

        val fotos = intent.getSerializableExtra(IntentFieldNames.FOTOS) as ArrayList<AppFoto>

        galleryRecycler.adapter = GalleryAdapter(fotos)
        galleryRecycler.layoutManager = StaggeredGridLayoutManager(2, GridLayout.VERTICAL)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Galeria"
    }
}
