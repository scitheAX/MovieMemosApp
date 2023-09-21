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

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.moviememosapp.database.MovieDatabaseDao
import com.example.android.moviememosapp.database.MovieMemo
import kotlinx.coroutines.launch

class MoviewMemoEditViewModel(
    private val movieMemosKey: Long = 0L,
    dataSource: MovieDatabaseDao) : ViewModel() {

    val database = dataSource

    private val currentMovie: LiveData<MovieMemo>

    fun getCurrentMovie() = currentMovie


    private var _movieTitle = MutableLiveData<String>()
    val movieTitle: LiveData<String> get() = _movieTitle

    fun setMovieTitle(title: String) {
        _movieTitle.value = title
    }

    private var _movieRating = MutableLiveData<Int>()
    val movieRating: LiveData<Int> get() = _movieRating

    fun setMovieRating(rating: Float) {
        _movieRating.value = rating.toInt()
    }

    private var _movieReview = MutableLiveData<String>()
    val movieReview: LiveData<String> get() = _movieReview

    fun setMovieReview(review: String) {
        _movieReview.value = review
    }

    init {
        currentMovie = database.getMovieWithId(movieMemosKey)
        _movieTitle.value = currentMovie.value?.movieTitle
        _movieReview.value = currentMovie.value?.movieReview
        _movieRating.value = currentMovie.value?.movieRating
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean?>()
    val showSnackBarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    fun showingSnackbar() {
        _showSnackbarEvent.value = true
    }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }

    private val _navigateToMovieList = MutableLiveData<Boolean?>()

    val navigateToMovieList: LiveData<Boolean?>
        get() = _navigateToMovieList

    fun doneNavigating() {
        _navigateToMovieList.value = null
    }

    fun onSetMovieMemos() {
        viewModelScope.launch {
            val currentMovie = database.get(movieMemosKey)
            currentMovie.movieTitle = _movieTitle.value.toString()
            currentMovie.movieReview = _movieReview.value.toString()
            currentMovie.movieRating = _movieRating.value!!.toInt()
            database.update(currentMovie)

            _navigateToMovieList.value = true
        }
    }

    fun onDeleteMovieMemos() {
        viewModelScope.launch {
            database.delete(movieMemosKey)
            _navigateToMovieList.value = true
        }
    }
}
