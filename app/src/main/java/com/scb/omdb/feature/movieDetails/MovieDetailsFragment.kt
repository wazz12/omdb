package com.scb.omdb.feature.movieDetails

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.scb.omdb.R
import com.scb.omdb.feature.BaseFragment
import com.scb.omdb.feature.MainActivity.Companion.ID
import com.scb.omdb.feature.appToolbarTitle
import com.scb.omdb.model.MovieDetails
import com.scb.omdb.utils.isNetworkAvailable
import com.scb.omdb.utils.setPosterImage
import com.scb.omdb.utils.showAlertDialog
import com.scb.omdb.utils.visibleIfTrue
import com.scb.omdb.viewModel.MovieViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_list.toolbar

class MovieDetailsFragment : BaseFragment() {

    private lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        setToolBar(getString(R.string.movie_details))
        setObservers()
        getData()
    }

    override fun onStop() {
        super.onStop()
        hideProgressSpinnerDialog()
    }

    override fun onDestroy() {
        super.onDestroy()

        movieViewModel.cancelAllRequests()
        hideProgressSpinnerDialog()
    }

    private fun setToolBar(title: String) {
        toolbar.appToolbarTitle(title)
        toolbarNavIcon.visibleIfTrue(true)
        toolbar.onNavBackPressed(View.OnClickListener {
            activity?.onBackPressed()
        })
    }

    private fun setObservers() {
        movieViewModel.movieDetailsLiveData.observe(viewLifecycleOwner, Observer {
            hideProgressSpinnerDialog()
            it?.let { movieDetails ->
                setData(movieDetails)
            } ?: showAlertDialog(requireContext(), R.string.network_error, R.string.error_message)
        })
    }

    private fun getData() {
        arguments?.let {
            if (it.containsKey(ID)) {
                val id = it.getString(ID)
                id?.let { imdbID ->
                    if (isNetworkAvailable(requireActivity())) {
                        showProgressSpinnerDialog()
                        movieViewModel.getMovieDetails(imdbID)
                    } else {
                        showAlertDialog(
                            requireContext(),
                            R.string.network_error,
                            R.string.no_network_error_message
                        )
                    }
                }
            }
        }
    }

    private fun setData(movieDetails: MovieDetails) {
        setPosterImage(requireContext(), movieDetails.Poster, movie_poster_image_view)
        title_text_view.text = movieDetails.Title
        year_text_view.text = movieDetails.Year.toString()
        rated_text_view.text = movieDetails.Rated
        runtime_text_view.text = movieDetails.Runtime
        released_date_text_view.text = movieDetails.Released
        plot_text_view.text = movieDetails.Plot
        when {
            movieDetails.Metascore.equals("N/A", ignoreCase = true) -> {
                meta_score_text_view.text = movieDetails.Metascore
            }
            movieDetails.Metascore.toDouble() > 60 -> {
                val greenString =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(
                            movieDetails.Metascore + "<font color=green>" + getString(R.string.up_meta_score_text),
                            Html.FROM_HTML_MODE_LEGACY
                        )
                    } else {
                        Html.fromHtml(movieDetails.Metascore + "<font color=green>" + getString(R.string.up_meta_score_text))
                    }
                meta_score_text_view.text = greenString
            }
            movieDetails.Metascore.toDouble() > 30 -> {
                val yellowString =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(
                            movieDetails.Metascore + "<font color=yellow>" + getString(R.string.down_meta_score_text),
                            Html.FROM_HTML_MODE_LEGACY
                        )
                    } else {
                        Html.fromHtml(movieDetails.Metascore + "<font color=yellow>" + getString(R.string.down_meta_score_text))
                    }
                meta_score_text_view.text = yellowString
            }
            else -> {
                val redString =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(
                            movieDetails.Metascore + "<font color=red>" + getString(R.string.down_meta_score_text),
                            Html.FROM_HTML_MODE_LEGACY
                        )
                    } else {
                        Html.fromHtml(movieDetails.Metascore + "<font color=red>" + getString(R.string.down_meta_score_text))
                    }
                meta_score_text_view.text = redString
            }
        }
        imdb_rating_text_view.text = getString(R.string.rating_text, movieDetails.imdbRating)
        imdb_votes_text_view.text = movieDetails.imdbVotes
        director_text_view.text = movieDetails.Director
        writer_text_view.text = movieDetails.Writer
        actors_text_view.text = movieDetails.Actors
        language_text_view.text = movieDetails.Language
        country_text_view.text = movieDetails.Country
        genre_text_view.text = movieDetails.Genre
        awards_text_view.text = movieDetails.Awards
    }
}