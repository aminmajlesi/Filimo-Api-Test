package com.example.movieapp.repository

import com.example.movieapp.api.RetrofitInstance


class MoviesRepository {
    suspend fun getMoviesList() =
        RetrofitInstance.api.getMoviesList()

    suspend fun searchMovies(searchQuery: String) =
        RetrofitInstance.api.searchForMovies(searchQuery)

}