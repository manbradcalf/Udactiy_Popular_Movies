package com.example.benmedcalf.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.movie_detail_fragment_frame) != null) {
            // The detail container view will be present only in the large-screen
            // layouts (res/layout-sw600dp). If this view is present, then the activity
            // should be in two-pane mode
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_fragment_frame, new MovieDetailFragment(),
                                DETAILFRAGMENT_TAG).commit();
        } else {
            mTwoPane = false;
            getSupportFragmentManager().beginTransaction().add(
                    R.id.container,new MoviesGridFragment())
                    .commit();
        }

//        MoviesGridFragment moviesGridFragment = ((MoviesGridFragment)getSupportFragmentManager()
//        .findFragmentById(R.id.movie_detail_fragment));


    }
}
