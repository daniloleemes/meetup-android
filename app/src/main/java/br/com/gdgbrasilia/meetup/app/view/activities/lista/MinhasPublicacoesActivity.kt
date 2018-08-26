package br.com.gdgbrasilia.meetup.app.view.activities.lista

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.view.adapters.ViewPagerAdapter
import br.com.gdgbrasilia.meetup.app.view.fragments.minhas.MeusEstabelecimentosFragment
import br.com.gdgbrasilia.meetup.app.view.fragments.minhas.MeusEventosFragment
import kotlinx.android.synthetic.main.activity_minhas_publicacoes.*
import kotlinx.android.synthetic.main.toolbar_main.*

class MinhasPublicacoesActivity : AppCompatActivity() {

    private val viewPagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minhas_publicacoes)
        setupToolbar()
        setupViewPager()
    }

    private fun setupViewPager() {
        viewPagerAdapter.addFragment(MeusEventosFragment(), "Eventos")
        viewPagerAdapter.addFragment(MeusEstabelecimentosFragment(), "Estabelecimentos")
        minhasPublicacoesViewPager.adapter = viewPagerAdapter
        minhasPublicacoesTabLayout.setupWithViewPager(minhasPublicacoesViewPager)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Minhas Publicações"
    }
}
