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

package com.example.android.moviememosapp.moviememoedit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.android.moviememosapp.databinding.FragmentMovieMemoEditBinding
import com.google.android.material.snackbar.Snackbar

class MovieMemoEditFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentMovieMemoEditBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_movie_memo_edit, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = MovieMemoEditFragmentArgs.fromBundle(requireArguments())

        val dataSource = MovieDatabase.getInstance(application).movieDatabaseDao
        val viewModelFactory = MoviewMemoEditViewModelFactory(arguments.movieMemosKey, dataSource)

        val moviewMemoEditViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(MoviewMemoEditViewModel::class.java)

        binding.movieMemoEditViewModel = moviewMemoEditViewModel

        binding.lifecycleOwner = this

        binding.movieTitleText.editText!!.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length > binding.movieTitleText.counterMaxLength) {
                    binding.movieTitleText.editText!!.error = "タイトルは20文字以内で入力してください"
                }else if(s.isEmpty()) {
                    binding.movieTitleText.editText!!.error = "タイトルを１文字以上入力してください"
                } else {
                    binding.movieTitleText.editText!!.error = null
                    moviewMemoEditViewModel.setMovieTitle(s.toString())
                }
            }
        })

        binding.movieRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            moviewMemoEditViewModel.setMovieRating(rating)
        }

        binding.movieReviewText.editText!!.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length > binding.movieReviewText.counterMaxLength) {
                    binding.movieReviewText.editText!!.error = "感想は200文字以内で入力してください"
                } else {
                    binding.movieReviewText.editText!!.error = null
                    moviewMemoEditViewModel.setMovieReview(s.toString())
                }
            }
        })

        binding.saveButton.setOnClickListener {
            if (binding.movieTitleText.editText!!.error == null &&
                moviewMemoEditViewModel.movieTitle.value != "" &&
                moviewMemoEditViewModel.movieRating.value != null &&
                binding.movieReviewText.editText!!.error == null) {
                moviewMemoEditViewModel.onSetMovieMemos()
            }else {
                moviewMemoEditViewModel.showingSnackbar()
            }
        }

        binding.deleteButton.setOnClickListener {
            moviewMemoEditViewModel.onDeleteMovieMemos()
        }

        moviewMemoEditViewModel.navigateToMovieList.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                        MovieMemoEditFragmentDirections.actionMovieMemoEditFragmentToMovieListFragment())
                moviewMemoEditViewModel.doneNavigating()
            }
        })

        moviewMemoEditViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.error_message),
                    Snackbar.LENGTH_SHORT
                ).show()
                moviewMemoEditViewModel.doneShowingSnackbar()
            }
        })

        return binding.root
    }
}
