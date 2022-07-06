package com.example.movieapp.api

import com.example.movieapp.models.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPI {
    @GET("v2/top-headlines")
    suspend fun getMoviesList(

    ): Response<MoviesResponse>

    @GET("v2/everything")
    suspend fun searchForMovies(
        @Query("q")
        searchQuery: String
    ): Response<MoviesResponse>
}