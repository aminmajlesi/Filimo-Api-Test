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


    //////////////
/*    val breakingNews: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()

    var breakingNewsList: MutableLiveData<List<Data>> = MutableLiveData()


    init {
        getBreakingNews()
    }

    fun getBreakingNews() = viewModelScope.launch {
        try {
//            safeBreakingNewsCall()
            val response = moviesRepository.getMoviesList()
            if (response.isSuccessful) {
                Log.d("dataState", "getBreakingNews called  ")
                breakingNewsList.value = response.body()?.data
            } else {
                // handle error
            }
        } catch (e: Exception) {
        }
    }


    private fun handleBreakingNewsResponse(response: Response<MoviesResponse>) : Resource<MoviesResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




    private suspend fun safeBreakingNewsCall() {
        breakingNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = moviesRepository.getMoviesList()
                breakingNews.postValue(handleBreakingNewsResponse(response))

            } else {
                breakingNews.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is java.io.IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }*/


    ////////////////




    val breakingNews: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: MoviesResponse? = null

    val searchNews: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: MoviesResponse? = null
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
                breakingNewsPage++
                if(breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.data
                    val newArticles = resultResponse.data
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<MoviesResponse>) : Resource<MoviesResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if(searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.data
                    val newArticles = resultResponse.data
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeSearchMoviesCall(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = moviesRepository.searchMovies(searchQuery)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeMoviesListCall() {
        breakingNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = moviesRepository.getMoviesList()
                breakingNews.postValue(handleMoviesListResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
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












