package com.example.benmedcalf.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import com.example.benmedcalf.popularmovies.Database.FavoriteMoviesContract;
import com.example.benmedcalf.popularmovies.Database.FavoriteMoviesDBHelper;
import com.example.benmedcalf.popularmovies.Model.Movie;
import com.example.benmedcalf.popularmovies.Model.MovieResult;

import java.util.ArrayList;
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
    public static final int SORT_FAVORITE_TAG = 2;
    private int mSortPreference;
    public static final String SORT_TYPE = "SORTTYPE";


    public MoviesGridFragment() {
        // Fragment constructors are empty
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
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        if (getActivity().findViewById(R.id.two_pane_toolbar) == null) {
            //Set up toolbar and make it a SupportActionBar to enable menu options
            mToolbar.setTitle("Popular Movies");
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        } else {
            mToolbar.setVisibility(View.GONE);
        }

        //Create gridlayoutmanager and set it to the recyclerview
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        //Create Movies adapter and set it to the recyclerview
        mMoviesAdapter = new MoviesAdapter(getContext());
        mRecyclerView.setAdapter(mMoviesAdapter);

        callMoviesAPI(mSortPreference);

        return view;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_movies_grid_fragment, menu);
        Drawable drawable = menu.findItem(R.id.sort_favorite).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.sort_popularity:
                callMoviesAPI(SORT_POPULARITY_TAG);
                return true;

            case R.id.sort_rating:
                callMoviesAPI(SORT_TOP_RATED_TAG);
                return true;

            case R.id.sort_favorite:
                callDB();
                return true;

            default:
                break;
        }
        return false;
    }

    private void callMoviesAPI(int sortTag) {

        MoviesDataBaseAPI service = MoviesDataBaseAPI.Factory.getInstance();

        if (isOnline(getContext())) {
            switch (sortTag) {
                case SORT_POPULARITY_TAG:
                    Call<MovieResult> call_popular = service.getMostPopular(BuildConfig.MOVIES_TMDB_API_KEY);
                    call_popular.enqueue(new Callback<MovieResult>() {
                        @Override
                        public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                            MovieResult movieResult = response.body();

                            mMoviesList = movieResult.getResults();
                            mMoviesAdapter.setMovieList(mMoviesList);
                            mSortPreference = SORT_POPULARITY_TAG;
                        }

                        @Override
                        public void onFailure(Call<MovieResult> call, Throwable t) {
                            t.printStackTrace();
                            Toast toast = Toast.makeText(getContext(), "Oops! An error occurred", Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    });
                    break;

                case SORT_TOP_RATED_TAG:
                    Call<MovieResult> call_top_rated = service.getTopRated(BuildConfig.MOVIES_TMDB_API_KEY);
                    call_top_rated.enqueue(new Callback<MovieResult>() {
                        @Override
                        public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {

                            MovieResult movieResult = response.body();

                            mMoviesList = movieResult.getResults();
                            mMoviesAdapter.setMovieList(mMoviesList);
                            mSortPreference = SORT_TOP_RATED_TAG;
                        }

                        @Override
                        public void onFailure(Call<MovieResult> call, Throwable t) {
                            t.printStackTrace();
                            Toast toast = Toast.makeText(getContext(), "Oops! An error occurred", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                case SORT_FAVORITE_TAG:
                    callDB();
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

    private void callDB() {

        if (mSortPreference != SORT_FAVORITE_TAG) {
        mSortPreference = SORT_FAVORITE_TAG;
        }
        List<Movie> moviesList = new ArrayList<>();

        SQLiteDatabase db = new FavoriteMoviesDBHelper(getContext()).getWritableDatabase();
        String[] projection = {
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_POPULARITY,
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID,
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_TITLE,
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE,
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW,
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_POSTER_PATH,
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_VOTE_COUNT,
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP_PATH,
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_FAVORED
        };

        String sortOrder =
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_VOTE_AVERAGE + " DESC";

        Cursor c = db.query(
                FavoriteMoviesContract.FavoritesEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        int movieIdColumn = c.getColumnIndexOrThrow(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID);
        int titleColumn = c.getColumnIndexOrThrow(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_TITLE);
        int releaseDateColumn = c.getColumnIndexOrThrow(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE);
        int backdropPathColumn = c.getColumnIndexOrThrow(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP_PATH);
        int overviewColumn = c.getColumnIndexOrThrow(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW);
        int popularityColumn = c.getColumnIndexOrThrow(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_POPULARITY);
        int voteAverageColumn = c.getColumnIndexOrThrow(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_VOTE_AVERAGE);
        int posterPathColumn = c.getColumnIndexOrThrow(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_POSTER_PATH);
        int voteCountColumn = c.getColumnIndexOrThrow(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_VOTE_COUNT);

        while (c.moveToNext()) {
            //The cursor is now set to the right position
            Movie movie = new Movie();

            movie.setId(c.getInt(movieIdColumn));
            movie.setTitle(c.getString(titleColumn));
            movie.setReleaseDate(c.getString(releaseDateColumn));
            movie.setBackdropPath(c.getString(backdropPathColumn));
            movie.setOverview(c.getString(overviewColumn));
            movie.setPopularity(c.getFloat(popularityColumn));
            movie.setVoteAverage(c.getFloat(voteAverageColumn));
            movie.setPosterPath(c.getString(posterPathColumn));
            movie.setVoteCount(c.getInt(voteCountColumn));

            moviesList.add(movie);
        }
        c.close();
        mMoviesList = moviesList;
        mMoviesAdapter.setMovieList(mMoviesList);
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