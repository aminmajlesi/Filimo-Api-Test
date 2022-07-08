package com.example.movieapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.adapters.MoviesAdapter
import com.example.movieapp.databinding.FragmentListMovieBinding
import com.example.movieapp.repository.MoviesRepository
import com.example.movieapp.ui.MainActivity
import com.example.movieapp.ui.MoviesViewModel
import com.example.movieapp.ui.MoviesViewModelProviderFactory
import com.example.movieapp.util.Resource
import kotlinx.android.synthetic.main.fragment_list_movie.*

class FragmentListMovie : Fragment(R.layout.fragment_list_movie) {

    private lateinit var binding: FragmentListMovieBinding
    lateinit var viewModel: MoviesViewModel
    lateinit var moviesAdapter: MoviesAdapter
    var isLoading = false

    val TAG = "FragmentListMovie"

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


        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        moviesAdapter.differ.submitList(moviesResponse.data.toList())
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
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter()
        rvListMovie.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}