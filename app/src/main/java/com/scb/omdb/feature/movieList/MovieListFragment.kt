package com.scb.omdb.feature.movieList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.scb.omdb.R
import com.scb.omdb.feature.BaseFragment
import com.scb.omdb.feature.ItemClickListener
import com.scb.omdb.feature.MainActivity.Companion.ID
import com.scb.omdb.feature.appToolbarTitle
import com.scb.omdb.model.Movies
import com.scb.omdb.utils.isNetworkAvailable
import com.scb.omdb.utils.showAlertDialog
import com.scb.omdb.utils.visibleIfTrue
import com.scb.omdb.viewModel.MovieViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_movie_list.*

class MovieListFragment : BaseFragment(), ItemClickListener {

    private lateinit var movieViewModel: MovieViewModel
    private var movieMutableList: MutableList<Movies> = mutableListOf()
    private lateinit var movieListAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        setToolBar(getString(R.string.movie_list))
        setObservers()

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.e("onQueryTextSubmit", "query:$query")
                movieMutableList.clear()
                if (!query.isNullOrBlank())
                    getSearchedMoviesList(query)
                search_view.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })
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
        toolbarNavIcon.visibleIfTrue(false)
    }

    private fun setObservers() {
        movieViewModel.moviesListLiveData.observe(viewLifecycleOwner, Observer {
            hideProgressSpinnerDialog()
            no_data_text_view.visibleIfTrue(false)
            it?.let { movieList ->
                movieMutableList = movieList.toMutableList()
                setMovieListAdapter()
            } ?: showAlertDialog(requireContext(), R.string.network_error, R.string.error_message)
        })
        movieViewModel.isFailure.observe(viewLifecycleOwner, Observer {
            hideProgressSpinnerDialog()
            no_data_text_view.visibleIfTrue(true)
            no_data_text_view.text = getString(R.string.no_data_found)
        })
    }

    private fun getSearchedMoviesList(title: String) {
        if (isNetworkAvailable(requireActivity())) {
            showProgressSpinnerDialog()
            no_data_text_view.visibleIfTrue(true)
            no_data_text_view.text = getString(R.string.searching)
            movieViewModel.getMoviesList(title)
        } else {
            showAlertDialog(
                requireContext(),
                R.string.network_error,
                R.string.no_network_error_message
            )
        }
    }

    private fun setMovieListAdapter() {
        movies_recycler_view.layoutManager = GridLayoutManager(requireContext(), 2)
        movieListAdapter = MovieListAdapter(movieMutableList)
        movies_recycler_view.adapter = movieListAdapter
        movieListAdapter.setItemClickListener(this)
    }

    override fun onItemClick(position: Int) {
        val movie = movieListAdapter.getItem(position)
        Log.e("onItemClick", movie.Title)
        openMovieDetails(movie.imdbID)
    }

    private fun openMovieDetails(imdbID: String) {
        val bundle = Bundle()
        bundle.putString(ID, imdbID)
        Navigation.findNavController(requireActivity(), R.id.nav_frag_container)
            .navigate(R.id.action_movieListFragment_to_movieDetailsFragment, bundle)
    }
}