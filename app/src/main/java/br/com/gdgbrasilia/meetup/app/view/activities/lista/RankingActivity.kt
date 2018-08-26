package br.com.gdgbrasilia.meetup.app.view.activities.lista

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.view.adapters.ViewPagerAdapter
import br.com.gdgbrasilia.meetup.app.view.fragments.ranking.RankingAvaliadosFragment
import br.com.gdgbrasilia.meetup.app.view.fragments.ranking.RankingFavoritosFragment
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.toolbar_main.*

class RankingActivity : AppCompatActivity() {

    private val viewPagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        setupToolbar()
        setupViewPager()
    }

    private fun setupViewPager() {
        viewPagerAdapter.addFragment(RankingAvaliadosFragment(), "Melhor avaliados")
        viewPagerAdapter.addFragment(RankingFavoritosFragment(), "Mais favoritados")
        rankingViewPager.adapter = viewPagerAdapter
        rankingTabLayout.setupWithViewPager(rankingViewPager)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Ranking"
    }
}
