package com.scb.omdb.feature.movieList

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scb.omdb.R
import com.scb.omdb.feature.ItemClickListener
import com.scb.omdb.model.Movies
import com.scb.omdb.utils.inflate
import com.scb.omdb.utils.setPosterImage
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieListAdapter(private val moviesMutableList: MutableList<Movies>) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.movie_list_item))

    override fun getItemCount() = moviesMutableList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(moviesMutableList[position])
        holder.bindClickListener(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(movie: Movies) {
            with(itemView) {
                movie_title_text_view.text = movie.Title
                setPosterImage(context, movie.Poster, movie_poster_image_view)
            }
        }

        fun bindClickListener(position: Int) {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(position)
            }
        }
    }

    fun getItem(position: Int): Movies {
        return moviesMutableList[position]
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}