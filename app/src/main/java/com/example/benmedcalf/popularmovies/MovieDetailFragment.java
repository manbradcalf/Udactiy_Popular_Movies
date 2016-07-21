package com.example.benmedcalf.popularmovies;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.benmedcalf.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by ben.medcalf on 7/21/16.
 */
public class MovieDetailFragment extends Fragment {

    public static final String EXTRA_MOVIE_ID = "com.example.benmedcalf.popularmovies.movie_id";
    public static final String BASE_URL_FOR_IMAGES = "http://image.tmdb.org/t/p/w342/";
    public TextView mDescription;
    public TextView mTitle;
    public TextView mReleaseDate;
    public ImageView mPoster;
    public RatingBar mRatingBar;
    public CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Movie mMovie;

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
            Log.e("App","Success to load poster in onBitmapLoaded method");
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d("onBitmapFailed", ": Bitmap failed!");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();


        if (arguments != null) {
            View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
            mMovie = arguments.getParcelable(EXTRA_MOVIE_ID);
            mDescription = (TextView) rootView.findViewById(R.id.description);
            mTitle = (TextView) rootView.findViewById(R.id.movie_title);
            mPoster = (ImageView) rootView.findViewById(R.id.movie_poster_detail);
            mReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
            mRatingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);

            mTitle.setText(mMovie.getTitle());
            String releaseDateText = "Released: " + mMovie.getReleaseDate();
            mReleaseDate.setText(releaseDateText);
            mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

        /* Setting Expanded Title Color to transparent here because having the title ellipsized
        over the poster image looks hella ugly */
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            mCollapsingToolbarLayout.setTitle(mMovie.getTitle());
//            mDescription.setText(description);
            mRatingBar.setRating(mMovie.getVoteAverage()/2);
            rootView.setVisibility(View.VISIBLE);
            return rootView;
        }

        return inflater.inflate(R.layout.empty_state, container, false);
    }


}
