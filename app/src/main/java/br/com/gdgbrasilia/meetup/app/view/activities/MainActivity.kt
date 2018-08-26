package br.com.gdgbrasilia.meetup.app.view.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import br.com.gdgbrasilia.meetup.R
import br.com.gdgbrasilia.meetup.app.data.AppApplication
import br.com.gdgbrasilia.meetup.app.model.enums.ListType
import br.com.gdgbrasilia.meetup.app.view.adapters.ThumbAdapter
import br.com.gdgbrasilia.meetup.app.view.common.TransitionNames
import br.com.gdgbrasilia.meetup.app.view.viewholder.ThumbViewHolder
import br.com.gdgbrasilia.meetup.app.view.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val movieVM by lazy { ViewModelProviders.of(this).get(MovieViewModel::class.java) }
    private val upComingAdapter by lazy { ThumbAdapter(mutableListOf(), this, R.layout.holder_movie_playing) }
    private val upComingLayoutManager by lazy { LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSearchField()
        setupUpcoming()
    }

    private fun setupSearchField() {
        searchField.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && searchField.text.toString().length > 1) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair(searchField, TransitionNames.SEARCH_FIELD))
                val intent = Intent(AppApplication.instance.applicationContext, SearchActivity::class.java)
                intent.putExtra("query", searchField.text.toString())
                ActivityCompat.startActivity(this, intent, options.toBundle())
                searchField.text.clear()
                true
            }
            false
        }
    }

    private fun setupUpcoming() {
        recyclerUpcoming.adapter = upComingAdapter
        recyclerUpcoming.layoutManager = upComingLayoutManager
        recyclerUpcoming.scheduleLayoutAnimation()
        movieVM.fetchUpcoming().observe(this, Observer { movies ->
            movies?.let {
                upComingAdapter.addAll(it)
            }
        })
        seeMoreBtn.setOnClickListener {
            movieVM.upcomingMovies.value?.let {
                if (it.isNotEmpty()) {
                    upComingLayoutManager.let {
                        val firstVisibleItemPosition = it.findFirstVisibleItemPosition()
                        val lastVisibleItemPosition = it.findLastVisibleItemPosition()

                        val pairs: MutableList<Pair<View, String>> = mutableListOf()
                        for (i in firstVisibleItemPosition until lastVisibleItemPosition) {
                            val holderForAdapterPosition = recyclerUpcoming.findViewHolderForAdapterPosition(i) as ThumbViewHolder
                            val imageView = holderForAdapterPosition.poster
                            pairs.add(Pair(imageView, "${TransitionNames.POSTER}_$i"))
                        }

                        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs.toTypedArray()).toBundle()

                        val intent = Intent(this, SeeMoreActivity::class.java)
                        intent.putExtra("upcoming", movieVM.upcomingMovies.value as ArrayList)
                        intent.putExtra("type", ListType.UPCOMING)
                        intent.putExtra("firstVisible", firstVisibleItemPosition)
                        ActivityCompat.startActivity(this, intent, bundle)
                    }
                }
            }
        }
    }
}
