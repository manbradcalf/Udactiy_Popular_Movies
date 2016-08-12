package com.example.benmedcalf.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.benmedcalf.popularmovies.Adapter.MoviesAdapter;
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
    public MoviesAdapter mMoviesAdapter;
    private List<Movie> mMoviesList;
    private Toolbar mToolbar;
    public static final int SORT_POPULARITY_TAG = 0;
    public static final int SORT_TOP_RATED_TAG = 1;
    private int mSortPreference;
    public static final String SORT_TYPE = "SORTTYPE";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    public static final String EXTRA_MOVIE_ID = "com.example.benmedcalf.popularmovies.movie_id";


    public MoviesGridFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mSortPreference = savedInstanceState.getInt(SORT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movies_recycler_view);

        if (!MainActivity.mTwoPane) {
            //Set up toolbar and make it a SupportActionBar to enable menu options
            mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
            mToolbar.setTitle("Popular Movies");
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        //Create gridlayoutmanager and set it to the recyclerview
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        //Create Movies adapter and set it to the recyclerview
        mMoviesAdapter = new MoviesAdapter(getContext());
        mRecyclerView.setAdapter(mMoviesAdapter);

        callDB(mSortPreference);

        return view;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_movies_grid_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.sort_popularity:
                callDB(SORT_POPULARITY_TAG);
                return true;

            case R.id.sort_rating:
                callDB(SORT_TOP_RATED_TAG);
                return true;

            default:
                break;
        }
        return false;
    }

    private void callDB(int sort_tag) {

        MoviesDataBaseAPI service = MoviesDataBaseAPI.Factory.getInstance();

        if (isOnline(getContext())) {
            switch (sort_tag) {
                case SORT_POPULARITY_TAG:
                    Call<Example> call_popular = service.getMostPopular(BuildConfig.MOVIES_TMDB_API_KEY);
                    call_popular.enqueue(new Callback<Example>() {
                        @Override
                        public void onResponse(Call<Example> call, Response<Example> response) {
                            Example example = response.body();

                            mMoviesList = example.getResults();
                            mMoviesAdapter.setMovieList(mMoviesList);
                            mSortPreference = SORT_POPULARITY_TAG;
                        }

                        @Override
                        public void onFailure(Call<Example> call, Throwable t) {
                            t.printStackTrace();
                            Toast toast = Toast.makeText(getContext(), "Oops! An error occurred", Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    });
                    break;

                case SORT_TOP_RATED_TAG:
                    Call<Example> call_top_rated = service.getTopRated(BuildConfig.MOVIES_TMDB_API_KEY);
                    call_top_rated.enqueue(new Callback<Example>() {
                        @Override
                        public void onResponse(Call<Example> call, Response<Example> response) {

                            Example example = response.body();

                            mMoviesList = example.getResults();
                            mMoviesAdapter.setMovieList(mMoviesList);
                            mSortPreference = SORT_TOP_RATED_TAG;
                        }

                        @Override
                        public void onFailure(Call<Example> call, Throwable t) {
                            t.printStackTrace();
                            Toast toast = Toast.makeText(getContext(), "Oops! An error occurred", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

            }
        } else {
            Snackbar.make(mRecyclerView, "Unable to connect", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .setAction("Go To Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .show();
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager mngr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mngr.getActiveNetworkInfo();

        return !(info == null || (info.getState() != NetworkInfo.State.CONNECTED));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SORT_TYPE, mSortPreference);
    }
}