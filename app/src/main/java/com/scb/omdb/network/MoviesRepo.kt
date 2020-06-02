package com.scb.omdb.network

import com.scb.omdb.model.MovieDetails
import com.scb.omdb.model.MoviesListResponse

class MoviesRepo(private val api: MoviesApi) : BaseRepo() {

    suspend fun getMoviesList(title: String, type: String): MoviesListResponse? {
        return safeApiCall(
            call = { api.getMoviesListAsync(title, type).await() },
            errorMessage = "Error occurred while retrieving data"
        )
    }

    suspend fun getMovieDetails(id: String): MovieDetails? {
        return safeApiCall(
            call = { api.getMovieDetailsAsync(id).await() },
            errorMessage = "Error occurred while retrieving data"
        )
    }
}