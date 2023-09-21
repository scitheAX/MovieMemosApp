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

package com.example.android.moviememosapp.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.moviememosapp.R
import com.example.android.moviememosapp.database.MovieDatabase
import com.example.android.moviememosapp.databinding.FragmentMovieDetailBinding

class MovieDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentMovieDetailBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_movie_detail, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = MovieDetailFragmentArgs.fromBundle(requireArguments())

        val dataSource = MovieDatabase.getInstance(application).movieDatabaseDao
        val viewModelFactory = MovieDetailViewModelFactory(arguments.movieMemosKey, dataSource)

        val movieDetailViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(MovieDetailViewModel::class.java)

        binding.movieDetailViewModel = movieDetailViewModel

        binding.lifecycleOwner = this

        movieDetailViewModel.navigateToMovieList.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                        MovieDetailFragmentDirections.actionMovieDetailFragmentToMovieListFragment())

                movieDetailViewModel.doneNavigating()
            }
        })

        return binding.root
    }


}