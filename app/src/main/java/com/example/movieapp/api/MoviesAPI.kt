package com.example.movieapp.api

import com.example.movieapp.models.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPI {
    @GET("api/en/v1/movie/movie/list/tagid/1000300/text/simple/sug/on")
    suspend fun getMoviesList(

    ): Response<MoviesResponse>

    @GET("api/en/v1/movie/movie/list/tagid/1000300/text/{Query}/sug/on")
    suspend fun searchForMovies(
        @Query("Query")
        searchQuery: String
    ): Response<MoviesResponse>
}