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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_memo_table")
data class MovieMemo(
        @PrimaryKey(autoGenerate = true)
        var movieId: Long = 0L,

        @ColumnInfo(name = "movie_title")
        var movieTitle: String = "",

        @ColumnInfo(name = "movie_review")
        var movieReview: String = "",

        @ColumnInfo(name = "movie_rating")
        var movieRating: Int = -1)
