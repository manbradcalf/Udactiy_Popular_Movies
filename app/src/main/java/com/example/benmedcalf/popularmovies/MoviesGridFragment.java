package com.example.benmedcalf.popularmovies;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben.medcalf on 5/8/16.
 */
public class MoviesGridFragment extends android.support.v4.app.Fragment {

    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

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
        mRecyclerView.setAdapter(mMoviesAdapter);

        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            movies.add(new Movie());
        }

        mMoviesAdapter.setMovieList(movies);


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

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.poster_image_view);
        }
    }

}
