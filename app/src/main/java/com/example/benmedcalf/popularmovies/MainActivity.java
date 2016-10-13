package com.example.benmedcalf.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.benmedcalf.popularmovies.Event.MovieSelectedEvent;
import com.example.benmedcalf.popularmovies.Model.Movie;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    public static final String EXTRA_MOVIE_ID = "com.example.benmedcalf.popularmovies.movie_id";
    private static boolean mTwoPane = false;
    private EventBus mEventBus = EventBus.getDefault();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);

        if (result != YouTubeInitializationResult.SUCCESS) {
            //If there are any issues we can show an error dialog.
            result.getErrorDialog(this, 0).show();
        }

        if (findViewById(R.id.two_pane_toolbar) != null) {
            // The detail container view will be present only in the large-screen
            // layouts (res/layout-sw600dp). If this view is present, then the activity
            // should be in two-pane mode
            mTwoPane = true;
            Toolbar toolbar = (Toolbar) findViewById(R.id.two_pane_toolbar);
            // Setting the grid fragment's toolbar to null here
            Toolbar gridFragmentToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (gridFragmentToolbar != null) {
                gridFragmentToolbar.setVisibility(View.GONE);
            }
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_fragment_frame, new MovieDetailFragment(),
                            DETAILFRAGMENT_TAG).commit();
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mEventBus.unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(MovieSelectedEvent event) {

        Movie movie = event.getMovie();

        if (getTwoPane()) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_MOVIE_ID, movie);
            MovieDetailFragment detailFragment = new MovieDetailFragment();
            detailFragment.setArguments(bundle);

            // Commit fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_fragment_frame, detailFragment,
                            EXTRA_MOVIE_ID).commit();
        } else {
            Intent intent = MovieDetailActivity.newIntent(this, movie);
            startActivity(intent);
        }
    }

    public static boolean getTwoPane() {

        return mTwoPane;
    }
}

