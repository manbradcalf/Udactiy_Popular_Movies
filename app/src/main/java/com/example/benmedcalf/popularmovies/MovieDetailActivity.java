package com.example.benmedcalf.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benmedcalf.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by ben.medcalf on 5/8/16.
 */
public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "com.example.benmedcalf.popularmovies.movie_id";
    public static final String BASE_URL_FOR_IMAGES = "http://image.tmdb.org/t/p/w185/";
    public TextView mDescription;
    public ImageView mPoster;

    public static Intent newIntent(Context packageContext, Movie movie) {
        Intent intent = new Intent(packageContext, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail_activity);
        mDescription = (TextView) findViewById(R.id.detail_text_view);
        mPoster = (ImageView) findViewById(R.id.detail_poster_image_view);
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(EXTRA_MOVIE_ID);
        String description = movie.getOverview();

        mDescription.setText(description);

        Picasso.with(this)
                .load(BASE_URL_FOR_IMAGES + movie.getPosterPath())
                .placeholder(R.color.colorAccent).into(mPoster);

        Log.d(MovieDetailActivity.class.getSimpleName(), "Launched Movie Detail Activity");
    }
}
