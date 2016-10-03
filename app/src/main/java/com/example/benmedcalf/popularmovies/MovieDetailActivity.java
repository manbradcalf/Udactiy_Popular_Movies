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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.benmedcalf.popularmovies.Database.FavoriteMoviesDBHelper;
import com.example.benmedcalf.popularmovies.Model.Movie;
import com.example.benmedcalf.popularmovies.Model.Result;
import com.example.benmedcalf.popularmovies.Model.Video;
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
    public TextView mVideoId;
    public TextView mVideoKey;
    public TextView mVideoName;
    public TextView mVideoSite;
    public TextView mVideoSize;
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
        setContentView(R.layout.movie_detail_fragment);

        Intent intent = getIntent();
        final Movie movie = intent.getParcelableExtra(EXTRA_MOVIE_ID);
        String description = movie.getOverview();
        final String movieTitle = movie.getTitle();
        final int movieId = movie.getId();

        final SharedPreferences sharedpreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        final Editor editor = sharedpreferences.edit();

        mDescription = (TextView) findViewById(R.id.description);
        mTitle = (TextView) findViewById(R.id.movie_title);
        mPoster = (ImageView) findViewById(R.id.movie_poster_detail);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mRatingBar = (RatingBar) findViewById(R.id.rating_bar);
        mTitle.setText(movie.getTitle());
        String releaseDateText = "Released: " + movie.getReleaseDate();
        mReleaseDate.setText(releaseDateText);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mDBHelper = new FavoriteMoviesDBHelper(this);

        // Trailer shiz
        mVideoId = (TextView) findViewById(R.id.video_id);
        mVideoName = (TextView) findViewById(R.id.video_name);
        mVideoSize = (TextView) findViewById(R.id.video_size);
        mVideoKey = (TextView) findViewById(R.id.video_key);
        mVideoSite = (TextView) findViewById(R.id.video_site);

        mToggleButton = (ToggleButton) findViewById(R.id.toggle_favorite);
        if (mToggleButton != null) {
            // 0 is default value returned if no movie matches movieTitle key
            if (sharedpreferences.getInt(movieTitle, 0) == 0) {
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

                                                                 if (!sharedpreferences.contains(movieTitle)) {
                                                                     editor.putInt(movieTitle, movieId).commit();
                                                                     mDBHelper.addMovie(movie);
                                                                 }
                                                             } else {
                                                                     // TODO: Delete the favored movie if already favored
                                                                     if (sharedpreferences.contains(movieTitle)) {
                                                                         sharedpreferences.edit().remove(movieTitle).apply();
                                                                         mDBHelper.deleteMovie(movieId);
                                                                     }
                                                                     mToggleButton.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_border, null));
                                                                     editor.remove(movieTitle);
                                                                 }
                                                             }
                                                     });
        /* Setting Expanded Title Color to transparent here because having the title ellipsized
        over the poster image looks hella ugly */
                    mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            mCollapsingToolbarLayout.setTitle(movie.getTitle());
            mDescription.setText(description);
            mRatingBar.setRating(movie.getVoteAverage() / 2);
            getMovieTrailer(movie);

            setSupportActionBar((Toolbar) findViewById(R.id.detail_toolbar));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


      /* first answer here
      http://stackoverflow.com/questions/24682217/get-bitmap-from-imageview-loaded-with-picasso */
            Picasso.with(this)
                    .load(BASE_URL_FOR_IMAGES + movie.getPosterPath())
                    .into(mTarget);

            Log.d(MovieDetailActivity.class.getSimpleName(), "Launched Movie Detail Activity");
        }
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

        Call<Video> call_video_result = service.getVideo(movie.getId(), BuildConfig.MOVIES_TMDB_API_KEY);
        call_video_result.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                Video video = response.body();
                List<Result> results = response.body().getResults();

                mVideoId.setText(video.getId());
                mVideoKey.setText(results.get());
                mVideoName.setText(result.getName());
                mVideoSite.setText(result.getSite());
                mVideoSize.setText(result.getSize().toString());


            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {

            }
        });
    }

}
