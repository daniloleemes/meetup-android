package br.com.gdgbrasilia.meetup.app.view.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.gdgbrasilia.meetup.R
import br.com.gdgbrasilia.meetup.app.view.adapters.ViewPagerAdapter
import br.com.gdgbrasilia.meetup.app.view.fragments.GalleryFragment
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {

    private val viewPagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        (intent?.extras?.getSerializable("gallery") as? ArrayList<String>)?.let {
            for (picture in it) {
                val fragment = GalleryFragment()
                val arguments = Bundle()
                arguments.putSerializable("path", picture)
                fragment.arguments = arguments
                viewPagerAdapter.addFragment(fragment, "")
            }
            galleryViewPager.adapter = viewPagerAdapter
            galleryTabLayout.setupWithViewPager(galleryViewPager)
        }
    }
}
