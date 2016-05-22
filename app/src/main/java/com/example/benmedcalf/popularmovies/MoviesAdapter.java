package com.example.benmedcalf.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benmedcalf.popularmovies.Model.Movie;
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
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String BASE_URL_FOR_IMAGES = "http://image.tmdb.org/t/p/w185/";


    public MoviesAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMovieList = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_card, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Float rating;

        Movie movie = mMovieList.get(position);

        //Set the movie so the ViewHolder's onClickListener can create an intent to detail actiity
        holder.setMovie(movie);

        //Load image
        Picasso.with(mContext)
                .load(BASE_URL_FOR_IMAGES + movie.getPosterPath())
                .placeholder(R.color.colorAccent).into(holder.mImageView);


        //Calculate movie's rating on a 5 star scale and set it to the star rating view on the card
        rating = movie.getPopularity()/2/10;
        holder.mRatingBar.setRating(rating);
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
