package com.scb.omdb.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scb.omdb.model.MovieDetails
import com.scb.omdb.model.Movies
import com.scb.omdb.network.ApiFactory
import com.scb.omdb.network.MoviesRepo
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    private val movieRepo: MoviesRepo = MoviesRepo(ApiFactory.moviesApi)

    val isFailure = MutableLiveData<Boolean>()
    val moviesListLiveData = MutableLiveData<List<Movies>>()
    val movieDetailsLiveData = MutableLiveData<MovieDetails>()

    fun getMoviesList(title: String) {
        scope.launch {
            val moviesListResponse = movieRepo.getMoviesList(title, "movie")
            moviesListResponse?.let {
                if (it.Response)
                    moviesListLiveData.postValue(it.Search)
                else
                    isFailure.postValue(true)
            }
        }
    }

    fun getMovieDetails(id: String) {
        scope.launch {
            val movieDetail = movieRepo.getMovieDetails(id)
            movieDetailsLiveData.postValue(movieDetail)
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}