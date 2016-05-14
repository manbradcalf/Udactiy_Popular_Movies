package com.example.benmedcalf.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benmedcalf.popularmovies.MoviesGridFragment.MovieViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben.medcalf on 5/8/16.
 */
public class MoviesAdapter
        extends RecyclerView.Adapter<MoviesGridFragment.MovieViewHolder> {

    private List<Movie> mMovieList;
    private LayoutInflater mInflater;
    private Context mContext;

    public MoviesAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMovieList = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_poster, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie movie = mMovieList.get(position);
        Picasso.with(mContext)
                .load(movie.getPoster())
                .placeholder(R.color.colorAccent)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList) {
        this.mMovieList.clear();
        this.mMovieList.addAll(movieList);
        notifyDataSetChanged();
    }
}
