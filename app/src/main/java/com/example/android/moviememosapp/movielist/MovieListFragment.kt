/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.moviememosapp.movielist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.moviememosapp.R
import com.example.android.moviememosapp.database.MovieDatabase
import com.example.android.moviememosapp.databinding.FragmentMovieListBinding
import com.google.android.material.snackbar.Snackbar

class MovieListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentMovieListBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_movie_list, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = MovieDatabase.getInstance(application).movieDatabaseDao
        val viewModelFactory = MovieListViewModelFactory(dataSource, application)

        val movieListViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(MovieListViewModel::class.java)

        binding.movieListViewModel = movieListViewModel

        val adapter = MovieMemosAdapter(MovieMemosClickListener { movieId ->
            movieListViewModel.onMovieMemoClicked(movieId)
        }, MovieMemosLongClickListener { movieId ->
            movieListViewModel.onMovieMemoLongClicked(movieId)
            return@MovieMemosLongClickListener true
        })
        binding.movieList.adapter = adapter

        val divider = androidx.recyclerview.widget.DividerItemDecoration(
            binding.movieList.context, LinearLayoutManager(this.context).orientation)
        divider.setDrawable(resources.getDrawable(R.drawable.divider))
        binding.movieList.addItemDecoration(divider)

        movieListViewModel.movies.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.lifecycleOwner = this

        movieListViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT
                ).show()

                movieListViewModel.doneShowingSnackbar()
            }
        })


        movieListViewModel.navigateToMemoEdit.observe(viewLifecycleOwner, Observer { movie ->
            movie?.let {
                this.findNavController().navigate(
                        MovieListFragmentDirections
                                .actionMovieListFragmentToMovieMemoEditFragment(movie))
                Log.i("checkKey",movie.toString())
                movieListViewModel.doneNavigating()
            }
        })

        movieListViewModel.navigateToMovieDetail.observe(viewLifecycleOwner, Observer { movie ->
            movie?.let {
                this.findNavController().navigate(
                    MovieListFragmentDirections
                        .actionMovieListFragmentToMovieDetailFragment(movie)
                )
                movieListViewModel.onMovieDetailNavigated()
            }
        })

        val manager = LinearLayoutManager(activity)
        binding.movieList.layoutManager = manager


        return binding.root
    }
}
