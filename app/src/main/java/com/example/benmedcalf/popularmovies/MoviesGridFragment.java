package com.example.benmedcalf.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.benmedcalf.popularmovies.Model.Example;
import com.example.benmedcalf.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;

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
    private MoviesAdapter mMoviesAdapter;
    private List<Movie> mMoviesList;
    private Toolbar mToolbar;
    public static final int SORT_POPULARITY_TAG = 0;
    public static final int SORT_TOP_RATED_TAG = 1;
    private int mSortPreference;
    public static final String SORT_TYPE = "SORTTYPE";


    public MoviesGridFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mSortPreference = savedInstanceState.getInt(SORT_TYPE);
        }
        callDB(mSortPreference);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movies_recycler_view);

        //Set up toolbar and make it a SupportActionBar to enable menu options
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("Popular Movies");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        //Create gridlayoutmanager and set it to the recyclerview
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        //Create Movies adapter and set it to the recyclerview
        mMoviesAdapter = new MoviesAdapter(getContext());
        mRecyclerView.setAdapter(mMoviesAdapter);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
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

        switch (sort_tag) {
            case SORT_POPULARITY_TAG:
                Call<Example> call_popular = service.getMostPopular("94a68d6f98f7825e429d10ff7af24af3");
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
                Call<Example> call_top_rated = service.getTopRated("94a68d6f98f7825e429d10ff7af24af3");
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SORT_TYPE, mSortPreference);
    }

    /** View Holder Class **/

    public class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView mImageView;
        public Movie mMovie;

        public void setMovie(Movie movie) {
            mMovie = movie;
        }

        public MovieViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.poster_image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = MovieDetailActivity.newIntent(getActivity(), mMovie);
            startActivity(intent);
        }
    }

    /** Movies Adapter Class **/

    public class MoviesAdapter
            extends RecyclerView.Adapter<MoviesGridFragment.MovieViewHolder> {

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

            Movie movie = mMovieList.get(position);
            holder.setMovie(movie);

            //Load image
            Picasso.with(mContext)
                    .load(BASE_URL_FOR_IMAGES + movie.getPosterPath())
                    .placeholder(R.color.colorAccent).into(holder.mImageView);
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
}