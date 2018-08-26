package br.com.firstsoft.opentheater.viewholder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.firstsoft.opentheater.activities.DetailActivity
import br.com.firstsoft.opentheater.application.AppApplication.Companion.THUMB_PATH
import br.com.firstsoft.opentheater.application.TransitionNames
import br.com.firstsoft.opentheater.model.Movie
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.holder_movie_playing.view.*

/**
 * Created by danilolemes on 28/02/2018.
 */
class ThumbViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view), View.OnClickListener {

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
        Glide.with(this.poster).load(THUMB_PATH + movie.poster_path).into(this.poster)
    }

    override fun onClick(p0: View?) {
        movie?.let {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, Pair(poster, TransitionNames.POSTER))
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("movie", it)
            ActivityCompat.startActivity(context, intent, options.toBundle())
        }
    }
}