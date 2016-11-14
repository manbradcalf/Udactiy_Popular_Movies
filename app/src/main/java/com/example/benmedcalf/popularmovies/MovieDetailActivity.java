package com.example.benmedcalf.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.benmedcalf.popularmovies.Adapter.ReviewsAdapter;
import com.example.benmedcalf.popularmovies.Adapter.ThumbnailTrailerAdapter;
import com.example.benmedcalf.popularmovies.Database.FavoriteMoviesDBHelper;
import com.example.benmedcalf.popularmovies.Model.Movie;
import com.example.benmedcalf.popularmovies.Model.ReviewResult;
import com.example.benmedcalf.popularmovies.Model.Reviews;
import com.example.benmedcalf.popularmovies.Model.VideoResult;
import com.example.benmedcalf.popularmovies.Model.Videos;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by ben.medcalf on 5/8/16.
 */
public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "com.example.benmedcalf.popularmovies.movie_id";
    public static final String BASE_URL_FOR_IMAGES = "http://image.tmdb.org/t/p/w342/";
    public TextView mDescription;
    public TextView mTitle;
    public TextView mReleaseDate;
    public Movie mMovie;
    public MovieDetailFragment mDetailFragment;
    public RecyclerView mTrailerRecyclerView;
    public RecyclerView mReviewRecyclerView;
    public ImageView mPoster;
    public RatingBar mRatingBar;
    public CollapsingToolbarLayout mCollapsingToolbarLayout;
    public ToggleButton mToggleButton;
    public FavoriteMoviesDBHelper mDBHelper;

    private static final String SHARED_PREF = "SHARED_PREF";


    /* Creating final Target object here to pass to Picasso,
     * in order to prevent the Target
     * from being garbage collected.
     * The seemingly random garbage collection
     * of the Target was preventing
     * the image from loading in theToolbar
     * http://stackoverflow.com/questions/25975006/picasso-onbitmaploaded-never-called/36310327#36310327
     * */
    final Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mPoster.setImageBitmap(bitmap);
            Log.e("App", "Success to load poster in onBitmapLoaded method");
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d("onBitmapFailed", ": Bitmap failed!");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public static Intent newIntent(Context packageContext, Movie movie) {
        Intent intent = new Intent(packageContext, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);

        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(EXTRA_MOVIE_ID);
        } else {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE_ID);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MOVIE_ID, mMovie);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(bundle);


        // Commit fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, fragment)
                .commit();

        Log.d(MovieDetailActivity.class.getSimpleName(), "Launched Movie Detail Activity");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMovieTrailer(Movie movie) {

        MoviesDataBaseAPI service = MoviesDataBaseAPI.Factory.getInstance();
        Call<Videos> call = service.getVideo(movie.getId(), BuildConfig.MOVIES_TMDB_API_KEY);
        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, final Response<Videos> response) {

                List<VideoResult> videoResults = response.body().getResults();
                ThumbnailTrailerAdapter adapter = new ThumbnailTrailerAdapter(MovieDetailActivity.this, videoResults);
                mTrailerRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {

            }
        });
    }

    private void getMovieReviews(Movie movie) {

        // TODO: Add something that checks for no reviews. If no reviews, say "No reviews to show"

        MoviesDataBaseAPI service = MoviesDataBaseAPI.Factory.getInstance();
        Call<Reviews> call = service.getReviews(movie.getId(), BuildConfig.MOVIES_TMDB_API_KEY);
        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                List<ReviewResult> reviewResults = response.body().getResults();
                ReviewsAdapter adapter = new ReviewsAdapter(MovieDetailActivity.this, reviewResults);
                mReviewRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {

            }
        });
    }

    private void setFavoriteToggle(final Movie movie) {

        final String movieTitle = movie.getTitle();
        final int movieId = movie.getId();

        final SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        final Editor editor = sharedPreferences.edit();

        if (mToggleButton != null) {
            // 0 is default value returned if no movie matches movieTitle key
            if (sharedPreferences.getInt(movieTitle, 0) == 0) {
                mToggleButton.setChecked(false);
                mToggleButton.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_border, null));
            } else {
                mToggleButton.setChecked(true);
                mToggleButton.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_solid, null));
                editor.putInt(movieTitle, movieId).apply();
            }

            mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mToggleButton.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_solid, null));

                        if (!sharedPreferences.contains(movieTitle)) {
                            editor.putInt(movieTitle, movieId).commit();
                            mDBHelper.addMovie(movie);
                        }
                    } else {
                        // TODO: Delete the favored movie if already favored
                        if (sharedPreferences.contains(movieTitle)) {
                            sharedPreferences.edit().remove(movieTitle).apply();
                            mDBHelper.deleteMovie(movieId);
                        }
                        mToggleButton.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_border, null));
                        editor.remove(movieTitle);
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mDetailFragment.setActionBar();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(EXTRA_MOVIE_ID, mMovie);
    }
}
