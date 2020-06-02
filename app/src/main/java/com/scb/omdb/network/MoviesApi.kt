package com.scb.omdb.network

import com.scb.omdb.model.MovieDetails
import com.scb.omdb.model.MoviesListResponse
import com.scb.omdb.network.ApiFactory.API_KEY
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("?apikey=$API_KEY")
    fun getMoviesListAsync(
        @Query("s") title: String,
        @Query("type") type: String
    ): Deferred<Response<MoviesListResponse>>

    @GET("?apikey=$API_KEY")
    fun getMovieDetailsAsync(@Query("i") id: String): Deferred<Response<MovieDetails>>
}