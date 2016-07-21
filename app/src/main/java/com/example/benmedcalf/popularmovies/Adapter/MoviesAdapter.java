package com.example.benmedcalf.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.benmedcalf.popularmovies.Event.MovieSelectedEvent;
import com.example.benmedcalf.popularmovies.Model.Movie;
import com.example.benmedcalf.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * MovieAdapterClass
 */
public class MoviesAdapter
        extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> mMovieList;
    private LayoutInflater mInflater;
    private Context mContext;
    public static final String BASE_URL_FOR_IMAGES = "http://image.tmdb.org/t/p/w185/";


    public MoviesAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMovieList = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_card, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        final Movie movie = mMovieList.get(position);
        holder.setMovie(movie);

        //Load image
        Picasso.with(mContext)
                .load(BASE_URL_FOR_IMAGES + movie.getPosterPath())
                .placeholder(R.color.colorAccent).into(holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EventBus.getDefault().post(new MovieSelectedEvent(movie));
            }
        });
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

    /** View Holder Class **/

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public Movie mMovie;

        public void setMovie(Movie movie) {
            mMovie = movie;
        }

        public MovieViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.poster_image_view);
        }

    }
}

