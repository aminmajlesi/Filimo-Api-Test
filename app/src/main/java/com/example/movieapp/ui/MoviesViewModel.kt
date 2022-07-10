package com.example.movieapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movieapp.MoviesApplication
import com.example.movieapp.models.Data
import com.example.movieapp.models.MoviesResponse
import com.example.movieapp.repository.MoviesRepository
import com.example.movieapp.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class MoviesViewModel(
    app: Application,
    val moviesRepository: MoviesRepository
) : AndroidViewModel(app) {

    val listMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()

    val searchMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    var searchMoviesResponse: MoviesResponse? = null
    var newSearchQuery:String? = null
    var oldSearchQuery:String? = null


    init {
        getMoviesList()
    }

    fun getMoviesList() = viewModelScope.launch {
        safeMoviesListCall()
    }

    fun searchMovies(searchQuery: String) = viewModelScope.launch {
        safeSearchMoviesCall(searchQuery)
    }

    private fun handleMoviesListResponse(response: Response<MoviesResponse>) : Resource<MoviesResponse> {

        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())

    }

    private fun handleSearchMoviesResponse(response: Response<MoviesResponse>) : Resource<MoviesResponse> {

        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())


    }

    private suspend fun safeSearchMoviesCall(searchQuery: String) {
        newSearchQuery = searchQuery
        searchMovies.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = moviesRepository.searchMovies(searchQuery)
                searchMovies.postValue(handleSearchMoviesResponse(response))
            } else {
                searchMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> searchMovies.postValue(Resource.Error("Network Failure"))
                else -> searchMovies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeMoviesListCall() {
        listMovies.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = moviesRepository.getMoviesList()
                listMovies.postValue(handleMoviesListResponse(response))
            } else {
                listMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> listMovies.postValue(Resource.Error("Network Failure"))
                else -> listMovies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MoviesApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return false
    }
}












