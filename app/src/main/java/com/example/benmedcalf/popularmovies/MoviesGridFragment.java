package com.example.benmedcalf.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.benmedcalf.popularmovies.Model.Example;
import com.example.benmedcalf.popularmovies.Model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ben.medcalf on 5/8/16.
 */
public class MoviesGridFragment extends android.support.v4.app.Fragment {

    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private List<Movie> mMoviesList;
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static final String TAG = "MOVIESGRIDFRAGMENT";
    public static final String EXTRA_MOVIE_ID = "com.example.benmedcalf.popularmovies.movie_id";


    public MoviesGridFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movies_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMoviesAdapter = new MoviesAdapter(getContext());

        callDB();

        mRecyclerView.setAdapter(mMoviesAdapter);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        //updateMovies();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_movies_grid_fragment, menu);
    }

    private void callDB() {

        MoviesDataBaseAPI service = MoviesDataBaseAPI.Factory.getInstance();

        Call<Example> call = service.getMostPopular("94a68d6f98f7825e429d10ff7af24af3");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Example example = response.body();
                mMoviesList = example.getResults();

                mMoviesAdapter.setMovieList(mMoviesList);
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder
    {

        public ImageView mImageView;
        public RatingBar mRatingBar;
        public Movie mMovie;
        private Context context;

        public void setMovie(Movie movie) {
            mMovie = movie;
        }

        public MovieViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mImageView = (ImageView) itemView.findViewById(R.id.poster_image_view);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }

    }
}