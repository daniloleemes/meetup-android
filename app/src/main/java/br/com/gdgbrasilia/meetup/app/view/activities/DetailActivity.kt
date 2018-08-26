package br.com.firstsoft.opentheater.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import br.com.firstsoft.opentheater.R
import br.com.firstsoft.opentheater.adapter.GalleryAdapter
import br.com.firstsoft.opentheater.adapter.RecommendationAdapter
import br.com.firstsoft.opentheater.application.AppApplication
import br.com.firstsoft.opentheater.application.TransitionNames
import br.com.firstsoft.opentheater.model.Movie
import br.com.firstsoft.opentheater.viewmodel.MovieViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_status_bar_detail.*


class DetailActivity : AppCompatActivity() {

    private var movie: Movie? = null
    private val movieVM by lazy { ViewModelProviders.of(this).get(MovieViewModel::class.java) }
    private val galleryAdapter by lazy { GalleryAdapter(mutableListOf(), galleryClickListener()) }
    private val galleryLayoutManager by lazy { LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) }
    private val recommendationsAdapter by lazy {
        RecommendationAdapter(mutableListOf(), this, recommendationCallback())
    }

    private val recommendationsLayoutManager by lazy { LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        ViewCompat.setTransitionName(movieCover, TransitionNames.POSTER)

        movie = intent.extras.getSerializable("movie") as Movie
        movie?.let {
            fabVideo.isEnabled = false

            movieVM.fetchDetails(it.id).observe(this, Observer { movie ->
                movie?.let {
                    this.movie = it
                    setupDetailViews(it)
                }
            })

            movieVM.fetchVideos(it.id).observe(this, Observer { video ->
                video?.let {
                    fabVideo.isEnabled = true
                    fabVideo.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://www.youtube.com/watch?v=${video.key}")
                        startActivity(intent)
                    }
                }
            })

            setupCoordinatorLayout(it.title)
            setupViews(it)
            setupGallery(it.id)
            setupRecommendations(it.id)
            setupFABBehavior()
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

    fun setupViews(movie: Movie) {
        Glide.with(movieCover).load(AppApplication.IMAGE_PATH + movie.poster_path).into(movieCover)
        movieRating.rating = movie.vote_average / 2

        movieLang.text = movie.original_language.capitalize()
        movieLang.visibility = View.VISIBLE
        progressMovieLang.visibility = View.GONE

        movieRatingAverage.text = movie.vote_average.toString()
        movieRatingAverage.visibility = View.VISIBLE
        progressRatingAverage.visibility = View.GONE

        movieOverview.text = movie.overview
        movieReleaseDate.text = movie.release_date.replace('-', '.')
        movieGenres.text = AppApplication.genres.filter { movie.genre_ids.contains(it.id) }.joinToString(", ") { it.name }


        movieCover.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtra("gallery", arrayListOf(movie.poster_path))
            startActivity(intent)
        }
    }

    fun setupDetailViews(movie: Movie) {

        movie.runtime?.let {
            val duration = "${(it / 60)}h ${(it % 60)}min"

            movieLength.visibility = View.VISIBLE
            movieLength.text = duration
            progressMovieLength.visibility = View.GONE
        }
    }

    fun setupGallery(movieID: Int) {
        galleryRecycler.adapter = galleryAdapter
        galleryRecycler.layoutManager = galleryLayoutManager
        movieVM.fetchGallery(movieID).observe(this, Observer { gallery ->
            gallery?.let {
                galleryAdapter.clear()
                galleryAdapter.addAll(it)
            }
        })
    }

    fun setupRecommendations(movieID: Int) {
        recommentationsRecycler.adapter = recommendationsAdapter
        recommentationsRecycler.layoutManager = recommendationsLayoutManager
        progressRecommendations.visibility = View.VISIBLE
        recommentationsRecycler.visibility = View.GONE
        movieVM.fetchRecommendations(movieID).observe(this, Observer { recommendations ->
            recommendations?.let {
                progressRecommendations.visibility = View.GONE
                recommentationsRecycler.visibility = View.VISIBLE
                recommendationsAdapter.clear()
                recommendationsAdapter.addAll(it)
            }
        })
    }

    private fun setupFABBehavior() {

        val upArrow = resources.getDrawable(R.drawable.ic_back);

        appBarLayout.addOnOffsetChangedListener({ _, verticalOffset ->
            if (verticalOffset == 0) {
                fabVideo.show()
            } else {
                fabVideo.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                    override fun onHidden(fab: FloatingActionButton?) {
                        super.onShown(fab)
                        fab!!.visibility = View.INVISIBLE
                    }
                })
            }

            if (verticalOffset < -590) {
                upArrow.setColorFilter(resources.getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP)
                supportActionBar?.setHomeAsUpIndicator(upArrow)
            } else {
                upArrow.setColorFilter(resources.getColor(R.color.colorBackground), PorterDuff.Mode.SRC_ATOP)
                supportActionBar?.setHomeAsUpIndicator(upArrow)
            }
        })
    }

    private fun setupCoordinatorLayout(titulo: String) {
        collapsingToolbar.title = titulo
        val typeface = Typeface.createFromAsset(assets, "fonts/poppins_medium.ttf")
        collapsingToolbar.setExpandedTitleTypeface(typeface)
        collapsingToolbar.setCollapsedTitleTypeface(typeface)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun galleryClickListener(): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(this@DetailActivity, GalleryActivity::class.java)
            intent.putExtra("gallery", movieVM.gallery.value?.map { it.file_path } as ArrayList<String>)
            startActivity(intent)
        }
    }

    private fun recommendationCallback(): (Movie) -> Unit {
        return { movie ->
            finish()
            val intent = Intent(AppApplication.instance.applicationContext, DetailActivity::class.java)
            intent.putExtra("movie", movie)
            startActivity(intent)
        }
    }
}
