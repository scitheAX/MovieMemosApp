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

import android.app.Application
import androidx.lifecycle.*
import com.example.android.moviememosapp.database.MovieDatabaseDao
import com.example.android.moviememosapp.database.MovieMemo
//import com.example.android.moviememosapp.formatNights
import kotlinx.coroutines.launch

class MovieListViewModel(dataSource: MovieDatabaseDao, application: Application) : ViewModel() {

    val database = dataSource

    private var currentMovie = MutableLiveData<MovieMemo?>()

    val movies = database.getAllMovies()

    val startButtonVisible = Transformations.map(currentMovie) {
        null == it
    }

    val stopButtonVisible = Transformations.map(currentMovie) {
        null != it
    }

    val clearButtonVisible = Transformations.map(movies) {
        it?.isNotEmpty()
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean?>()

    val showSnackBarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    private val _navigateToMemoEdit = MutableLiveData<Long?>()
    val navigateToMemoEdit: LiveData<Long?>
        get() = _navigateToMemoEdit

    private val _navigateToMovieDetail = MutableLiveData<Long?>()
    val navigateToMovieDetail: LiveData<Long?>
        get() = _navigateToMovieDetail


    init {
        initializeCurrentoMovie()
    }

    private fun initializeCurrentoMovie() {
        viewModelScope.launch {
            currentMovie.value = getCurrentMovieFromDatabase()
        }
    }

    private suspend fun getCurrentMovieFromDatabase(): MovieMemo? {
        var movie = database.getCurrentMovie()
        if (movie?.movieReview != movie?.movieTitle) {
            movie = null
        }
        return movie
    }

    private suspend fun insert(movie: MovieMemo) {
        database.insert(movie)
    }

    private suspend fun update(movie: MovieMemo) {
        database.update(movie)
    }

    private suspend fun clear() {
        database.clear()
    }

    fun onAdd() {
        viewModelScope.launch {
            val newMovie = MovieMemo()

            insert(newMovie)

            currentMovie.value = getCurrentMovieFromDatabase()

            _navigateToMemoEdit.value = currentMovie.value?.movieId
        }
    }

    fun onClear() {
        viewModelScope.launch {
            clear()

            currentMovie.value = null

            _showSnackbarEvent.value = true
        }
    }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }

    fun onMovieMemoClicked(id: Long) {
        _navigateToMovieDetail.value = id
    }

    fun onMovieDetailNavigated() {
        _navigateToMovieDetail.value = null
    }

    fun onMovieMemoLongClicked(id: Long) {
        _navigateToMemoEdit.value = id
    }

    fun doneNavigating() {
        _navigateToMemoEdit.value = null
    }
}