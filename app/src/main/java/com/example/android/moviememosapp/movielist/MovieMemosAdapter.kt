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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.moviememosapp.database.MovieMemo
import com.example.android.moviememosapp.databinding.ListItemMovieMemosLinearBinding

class MovieMemosAdapter(val clickListener: MovieMemosClickListener,val longClickListener: MovieMemosLongClickListener)
    : ListAdapter<MovieMemo, MovieMemosAdapter.ViewHolder>(MovieMemosDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, longClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemMovieMemosLinearBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: MovieMemo, clickListener: MovieMemosClickListener, longClickListener: MovieMemosLongClickListener) {
            binding.movie = item
            binding.executePendingBindings()
            binding.clickListener = clickListener
            binding.longClickListener = longClickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMovieMemosLinearBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class MovieMemosDiffCallback : DiffUtil.ItemCallback<MovieMemo>() {

    override fun areItemsTheSame(oldItem: MovieMemo, newItem: MovieMemo): Boolean {
        return oldItem.movieId == newItem.movieId
    }


    override fun areContentsTheSame(oldItem: MovieMemo, newItem: MovieMemo): Boolean {
        return oldItem == newItem
    }
}

class MovieMemosClickListener(val clickListener: (movieId: Long) -> Unit) {
    fun onClick(movie: MovieMemo) = clickListener(movie.movieId)
}

class MovieMemosLongClickListener(val LongClickListener: (movieId: Long) -> Boolean) {
    fun onLongClick(movie: MovieMemo) = LongClickListener(movie.movieId)
}

