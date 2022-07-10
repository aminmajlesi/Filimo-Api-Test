package com.example.movieapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.adapters.MoviesAdapter
import com.example.movieapp.databinding.FragmentListMovieBinding
import com.example.movieapp.databinding.FragmentSearchMovieBinding
import com.example.movieapp.repository.MoviesRepository
import com.example.movieapp.ui.MainActivity
import com.example.movieapp.ui.MoviesViewModel
import com.example.movieapp.ui.MoviesViewModelProviderFactory
import com.example.movieapp.util.Constants
import com.example.movieapp.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.movieapp.util.Resource
import kotlinx.android.synthetic.main.fragment_search_movie.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
/**
 * Author By Amin Majlesi
 */
class FragmentSearchMovie : Fragment(R.layout.fragment_search_movie) {

    lateinit var viewModel: MoviesViewModel
    lateinit var moviesAdapter: MoviesAdapter
    val TAG = "FragmentSearchMovie"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsRepository = MoviesRepository()
        val viewModelProviderFactory = MoviesViewModelProviderFactory(requireActivity().application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MoviesViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.searchMovies(editable.toString())
                    }
                }
            }
        }

        viewModel.searchMovies.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        moviesAdapter.differ.submitList(newsResponse.data.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter()
        rvSearchMovies.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }
}