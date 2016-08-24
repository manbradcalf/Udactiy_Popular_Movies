package com.example.benmedcalf.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.benmedcalf.popularmovies.Database.FavoriteMoviesContract;
import com.example.benmedcalf.popularmovies.Database.FavoriteMoviesDBHelper;
import com.example.benmedcalf.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


/**
 * Created by ben.medcalf on 5/8/16.
 */
public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "com.example.benmedcalf.popularmovies.movie_id";
    public static final String BASE_URL_FOR_IMAGES = "http://image.tmdb.org/t/p/w342/";
    public TextView mDescription;
    public TextView mTitle;
    public TextView mReleaseDate;
    public ImageView mPoster;
    public RatingBar mRatingBar;
    public CollapsingToolbarLayout mCollapsingToolbarLayout;
    public ToggleButton mToggleButton;
    public FavoriteMoviesDBHelper mDBHelper;

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
        mToggleButton = (ToggleButton) findViewById(R.id.toggle_favorite);
        if (mToggleButton != null) {
            if (!movie.isFavorite()) {
                mToggleButton.setChecked(false);
                mToggleButton.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_border, null));
            } else {
                mToggleButton.setChecked(true);
                mToggleButton.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_solid, null));
            }

            mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        movie.setFavorite(true);
                        // Gets the data repository in write mode
                        SQLiteDatabase db = mDBHelper.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        // TODO: make a method here that puts a whole movie into db
                        ContentValues values = new ContentValues();
                        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID, movie.getId());
                        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
                        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
                        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
                        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
                        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
                        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount());
                        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
                        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_FAVORED, movie.isFavorite());
                        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_POPULARITY, movie.getPopularity());

                        // Insert the new row, returning the primary key value of the new row
                        long newRowId;
                        newRowId = db.insert(
                                FavoriteMoviesContract.FavoritesEntry.TABLE_NAME,
                                FavoriteMoviesContract.FavoritesEntry.COLUMN_NAME_NULLABLE,
                                values);

                    mToggleButton.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_solid, null));
                    } else {
                        mToggleButton.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_border, null));
                    }
                }
            });
        }

        /* Setting Expanded Title Color to transparent here because having the title ellipsized
        over the poster image looks hella ugly */
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        mCollapsingToolbarLayout.setTitle(movie.getTitle());
        mDescription.setText(description);
        mRatingBar.setRating(movie.getVoteAverage() / 2);

        setSupportActionBar((Toolbar) findViewById(R.id.detail_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


      /* first answer here
      http://stackoverflow.com/questions/24682217/get-bitmap-from-imageview-loaded-with-picasso */
        Picasso.with(this)
                .load(BASE_URL_FOR_IMAGES + movie.getPosterPath())
                .into(mTarget);

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
}
