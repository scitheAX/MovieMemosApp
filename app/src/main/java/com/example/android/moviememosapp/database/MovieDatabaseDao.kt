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

package com.example.android.moviememosapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MovieDatabaseDao {

    @Insert
    suspend fun insert(movie: MovieMemo)

    @Update
    suspend fun update(movie: MovieMemo)

    @Query("SELECT * from movie_memo_table WHERE movieId = :key")
    suspend fun get(key: Long): MovieMemo

    @Query("DELETE FROM movie_memo_table WHERE movieId = :key")
    suspend fun delete(key: Long)

    @Query("DELETE FROM movie_memo_table")
    suspend fun clear()



    @Query("SELECT * FROM movie_memo_table ORDER BY movieId DESC")
    fun getAllMovies(): LiveData<List<MovieMemo>>


    @Query("SELECT * FROM movie_memo_table ORDER BY movieId DESC LIMIT 1")
    suspend fun getCurrentMovie(): MovieMemo?


    @Query("SELECT * from movie_memo_table WHERE movieId = :key")
    fun getMovieWithId(key: Long): LiveData<MovieMemo>
}
