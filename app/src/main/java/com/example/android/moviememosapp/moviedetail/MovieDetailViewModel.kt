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

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviememosapp.database.MovieDatabaseDao
import com.example.android.moviememosapp.database.MovieMemo


class MovieDetailViewModel(
    private val movieMemosKey: Long = 0L,
    dataSource: MovieDatabaseDao) : ViewModel() {

    val database = dataSource

    private val movie: LiveData<MovieMemo>

    fun getMovie() = movie


    init {
        Log.i("DetailMovie","key=${movieMemosKey}")
        movie=database.getMovieWithId(movieMemosKey)
        Log.i("DetailMovie","${movie.value?.movieTitle}")
        Log.i("DetailMovie","${movie.value?.movieRating}")
        Log.i("DetailMovie","${movie.value?.movieReview}")
    }

    private val _navigateToMovieList = MutableLiveData<Boolean?>()

    val navigateToMovieList: LiveData<Boolean?>
        get() = _navigateToMovieList



    fun doneNavigating() {
        _navigateToMovieList.value = null
    }

    fun onClose() {
        _navigateToMovieList.value = true
    }

}