package br.com.firstsoft.opentheater.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import br.com.firstsoft.opentheater.R
import br.com.firstsoft.opentheater.adapter.ThumbAdapter
import br.com.firstsoft.opentheater.model.Movie
import br.com.firstsoft.opentheater.model.enums.ListType
import br.com.firstsoft.opentheater.view.EndlessRecyclerViewScrollListener
import br.com.firstsoft.opentheater.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_see_more.*

class SeeMoreActivity : AppCompatActivity() {

    private val movieVM by lazy { ViewModelProviders.of(this).get(MovieViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_more)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val type = intent?.extras?.getSerializable("type") as ListType
        val firstItemVisible = intent?.extras?.getInt("firstVisible") as Int
        val searchQuery = intent?.extras?.getString("searchQuery")
        val books = when (type) {
            ListType.SEARCH -> intent?.extras?.get("searchResults") as ArrayList<Movie>
            ListType.UPCOMING -> intent?.extras?.get("upcoming") as ArrayList<Movie>
        }

        supportPostponeEnterTransition()
        val adapter = ThumbAdapter(books ?: mutableListOf(), this, R.layout.holder_movie_complete)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerSeeMore.adapter = adapter
        recyclerSeeMore.layoutManager = layoutManager
        recyclerSeeMore.scrollToPosition(firstItemVisible ?: 0)
        recyclerSeeMore.post { supportStartPostponedEnterTransition() }
        recyclerSeeMore.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                when (type) {
                    ListType.SEARCH -> movieVM.search(searchQuery
                            ?: "", null, page).observe(this@SeeMoreActivity, searchResultObserver(adapter))
                    ListType.UPCOMING -> movieVM.fetchUpcoming(page).observe(this@SeeMoreActivity, upcomingObserver(adapter))
                }
            }
        })

    }

    private fun searchResultObserver(adapter: ThumbAdapter): Observer<MutableList<Movie>> {
        return Observer { searchResult ->
            searchResult?.let {
                recyclerSeeMore.post {
                    adapter.addAll(it)
                }
            }
        }
    }

    private fun upcomingObserver(adapter: ThumbAdapter): Observer<List<Movie>> {
        return Observer { upcoming ->
            upcoming?.let {
                recyclerSeeMore.post {
                    adapter.addAll(it)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
