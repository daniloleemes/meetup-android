package br.com.firstsoft.opentheater.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import br.com.firstsoft.opentheater.R
import br.com.firstsoft.opentheater.adapter.ThumbAdapter
import br.com.firstsoft.opentheater.application.TransitionNames
import br.com.firstsoft.opentheater.model.enums.ListType
import br.com.firstsoft.opentheater.viewholder.ThumbViewHolder
import br.com.firstsoft.opentheater.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    var searchQuery = ""
    private val movieVM by lazy { ViewModelProviders.of(this).get(MovieViewModel::class.java) }
    private val searchAdapter by lazy { ThumbAdapter(mutableListOf(), this, R.layout.holder_movie_playing) }
    private val searchLayoutManager by lazy { LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        ViewCompat.setTransitionName(searchField, TransitionNames.SEARCH_FIELD)

        intent?.extras?.getString("query")?.let {
            searchField.text.insert(0, it)
            searchQuery = it
            movieVM.search(searchQuery, progressSearch).observe(this, Observer { searchResult ->
                searchResult?.let {
                    searchAdapter.clear()
                    searchAdapter.addAll(it)
                }
            })
        }

        setupView()
    }

    fun search() {
        movieVM.search(searchQuery, progressSearch)
    }

    fun setupView() {
        recyclerResults.adapter = searchAdapter
        recyclerResults.layoutManager = searchLayoutManager
        searchField.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && searchField.text.toString().length > 1) {
                searchQuery = searchField.text.toString()
                search()
                true
            }
            false
        }

        cancelSearchBtn.setOnClickListener { supportFinishAfterTransition() }
        seeMoreBtn.setOnClickListener {
            movieVM.searchResult.value?.let {
                if (it.isNotEmpty()) {
                    searchLayoutManager.let {
                        val firstVisibleItemPosition = it.findFirstVisibleItemPosition()
                        val lastVisibleItemPosition = it.findLastVisibleItemPosition()

                        val pairs: MutableList<Pair<View, String>> = mutableListOf()
                        for (i in firstVisibleItemPosition until lastVisibleItemPosition) {
                            val holderForAdapterPosition = recyclerResults.findViewHolderForAdapterPosition(i) as ThumbViewHolder
                            val imageView = holderForAdapterPosition.poster
                            pairs.add(Pair(imageView, "${TransitionNames.POSTER}_$i"))
                        }

                        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs.toTypedArray()).toBundle()

                        val intent = Intent(this, SeeMoreActivity::class.java)
                        intent.putExtra("searchResults", movieVM.searchResult.value as ArrayList)
                        intent.putExtra("searchQuery", searchQuery)
                        intent.putExtra("type", ListType.SEARCH)
                        intent.putExtra("firstVisible", firstVisibleItemPosition)
                        ActivityCompat.startActivity(this, intent, bundle)
                    }
                }
            }
        }
    }
}
