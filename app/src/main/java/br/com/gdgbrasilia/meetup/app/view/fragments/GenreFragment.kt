package br.com.gdgbrasilia.meetup.app.view.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.R
import br.com.gdgbrasilia.meetup.app.business.vo.Genre
import br.com.gdgbrasilia.meetup.app.util.extensions.getViewModel
import br.com.gdgbrasilia.meetup.app.view.adapters.ThumbAdapter
import br.com.gdgbrasilia.meetup.app.view.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_genre.*

/**
 * Created by danilolemes on 01/03/2018.
 */
class GenreFragment : Fragment() {

    private val movieVM by lazy { getViewModel(MovieViewModel::class.java) }
    private val movieAdapter by lazy { ThumbAdapter(mutableListOf(), activity!!, R.layout.holder_movie_complete) }
    private val movieLayoutManager by lazy { LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater!!.inflate(R.layout.fragment_genre, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getSerializable("genre")?.let {
            val genre = it as Genre

            genreHeader.text = genre.name
            recyclerGenre.adapter = movieAdapter
            recyclerGenre.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            movieVM.movies.observe(this, Observer { movies ->
                movies?.let {
                    movieAdapter.clear()
                    movieAdapter.addAll(it)
                }
            })
            movieVM.fetchByGenre(genre.id)
        }
    }
}