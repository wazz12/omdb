package com.scb.omdb

import com.scb.omdb.network.ApiFactory
import com.scb.omdb.network.MoviesRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiUnitTests {

    private val movieRepo: MoviesRepo = MoviesRepo(ApiFactory.moviesApi)
    private val title = "spider"
    private val type = "movie"
    private val movieId = "tt0948470"

    @Test
    fun movie_list_api() = runBlocking {
        val movieList = movieRepo.getMoviesList(title, type)
        assertEquals(movieList?.Response, true)
        assertNotNull(movieList?.Search)
    }

    @Test
    fun movie_details_api() = runBlocking {
        val movieDetails = movieRepo.getMovieDetails(movieId)
        assertNotNull(movieDetails)
        assertNotNull(movieDetails)
        assertEquals(movieDetails?.imdbID, movieId)
    }
}