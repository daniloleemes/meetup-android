package br.com.gdgbrasilia.meetup.app.view.viewholder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.gdgbrasilia.meetup.app.business.vo.Movie
import br.com.gdgbrasilia.meetup.app.data.AppConstants.THUMB_PATH
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import kotlinx.android.synthetic.main.holder_movie_playing.view.*

/**
 * Created by danilolemes on 28/02/2018.
 */
class RecommendationViewHolder(view: View, val context: Context, val listener: (Movie) -> Unit) : RecyclerView.ViewHolder(view), View.OnClickListener {

    val poster = view.movieCover
    val title = view.movieTitle
    val rating = view.movieRating

    var movie: Movie? = null

    init {
        view.setOnClickListener(this)
    }

    fun bind(movie: Movie) {
        this.movie = movie
        this.title.text = if (movie.title.length > 20) movie.title.substring(0, 17) + "..." else movie.title
        this.rating.rating = movie.vote_average / 2
        this.poster.loadImg(THUMB_PATH + movie.poster_path)
    }

    override fun onClick(p0: View?) {
        movie?.let {
            listener(it)
        }
    }
}