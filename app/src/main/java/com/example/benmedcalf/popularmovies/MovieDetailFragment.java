package com.example.benmedcalf.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by ben.medcalf on 7/21/16.
 */
public class MovieDetailFragment extends Fragment {

    public static final String EXTRA_MOVIE_ID = "com.example.benmedcalf.popularmovies.movie_id";
    public static final String BASE_URL_FOR_IMAGES = "http://image.tmdb.org/t/p/w342/";
    public TextView mDescriptionTextView;
    public TextView mTitle;
    public TextView mReleaseDate;
    public CardView mTrailersCardView;
    public CardView mReviewsCardView;
    public ImageView mPoster;
    public RatingBar mRatingBar;
    public Toolbar mToolbar;
    public RecyclerView mTrailerRecyclerView;
    public RecyclerView mReviewsRecyclerView;
    public CollapsingToolbarLayout mCollapsingToolbarLayout;
    public ToggleButton mToggleButton;
    private Movie mMovie;
    private FavoriteMoviesDBHelper mDBHelper;
    private static final String SHARED_PREF = "SHARED_PREF";

    /* Creating final Target object here to pass to Picasso,
 * in order to prevent the Target
 * from being garbage collected.
 * The seemingly random garbage collection
 * of the Target was preventing
 * the image from loading in theToolbar
 * http://stackoverflow.com/questions/25975006/picasso-onbitmaploaded-never-called/36310327#36310327
 * */
    final Target Target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mPoster.setImageBitmap(bitmap);
            Log.d("App", "Success to load poster in onBitmapLoaded method");
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.e("onBitmapFailed", ": Bitmap failed!");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        if (arguments != null) {
            mMovie = arguments.getParcelable(EXTRA_MOVIE_ID);
        } else {
//            mMovie = savedInstanceState.getParcelable(EXTRA_MOVIE_ID);
        }
        mDBHelper = new FavoriteMoviesDBHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            Bundle arguments = getArguments();


        if (arguments != null) {
            View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
            //Get the movie


            //Get and set description
            mDescriptionTextView = (TextView) rootView.findViewById(R.id.description);
            mDescriptionTextView.setText(mMovie.getOverview());

            //Get and set Title
            mTitle = (TextView) rootView.findViewById(R.id.movie_title);
            mTitle.setText(mMovie.getTitle());

            /* Get and set poster.
            / first answer here
            / http://stackoverflow.com/questions/24682217/get-bitmap-from-imageview-loaded-with-picasso */
            mPoster = (ImageView) rootView.findViewById(R.id.movie_poster_detail);
            Picasso.with(getContext())
                    .load(BASE_URL_FOR_IMAGES + mMovie.getPosterPath())
                    .into(Target);

            //Get and set Release Date
            mReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
            String releaseDateText = "Released: " + mMovie.getReleaseDate();
            mReleaseDate.setText(releaseDateText);

            //Get and set rating
            mRatingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);
            if (mRatingBar != null) {
                mRatingBar.setEnabled(false);
            }
            mRatingBar.setRating(mMovie.getVoteAverage() / 2);


            mToggleButton = (ToggleButton) rootView.findViewById(R.id.toggle_favorite);
            setFavoriteToggle();


            if (!MainActivity.getTwoPane()) {
                // Toolbar
                mToolbar = (Toolbar) rootView.findViewById(R.id.detail_toolbar);
                mToolbar.setTitle(mMovie.getTitle());
                ((MovieDetailActivity) getActivity()).setSupportActionBar(mToolbar);
                ((MovieDetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            // Trailers
            mTrailersCardView = (CardView) rootView.findViewById(R.id.cardview_trailers);
            mTrailerRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailers_grid);
            RecyclerView.LayoutManager layoutManagerTrailers = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            mTrailerRecyclerView.setLayoutManager(layoutManagerTrailers);

            // Reviews
            mReviewsCardView = (CardView) rootView.findViewById(R.id.cardview_reviews);
            mReviewsRecyclerView = (RecyclerView) rootView.findViewById(R.id.review_recyclerview);
            RecyclerView.LayoutManager layoutManagerReviews = new LinearLayoutManager(getActivity().getApplicationContext());
            mReviewsRecyclerView.setLayoutManager(layoutManagerReviews);
            mReviewsRecyclerView.addItemDecoration(
                    new SimpleDividerItemDecoration(getActivity().getApplicationContext()));


            if (MainActivity.getTwoPane()) {
                mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
                //Setting Expanded Title Color to transparent here because having the title ellipsized
                //over the poster image looks hella ugly */
                mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                mCollapsingToolbarLayout.setTitle(mMovie.getTitle());
            }

            rootView.setVisibility(View.VISIBLE);

            getMovieTrailer(mMovie);
            getMovieReviews(mMovie);

//            Log.d(MovieDetailActivity.class.getSimpleName(), "Launched Movie Detail Activity");
            return rootView;
        }

        return inflater.inflate(R.layout.empty_state, container, false);
    }

    private void setFavoriteToggle() {

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        final Drawable toggledOnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_solid, null);
        toggledOnDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

        final Drawable toggledOffDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_border, null);
        toggledOffDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

        if (mToggleButton != null) {
            // 0 is default value returned if no movie matches movieTitle key
            if (sharedPreferences.getInt(mMovie.getTitle(), 0) == 0) {
                mToggleButton.setChecked(false);
                mToggleButton.setBackgroundDrawable(toggledOffDrawable);
            } else {
                mToggleButton.setChecked(true);
                mToggleButton.setBackgroundDrawable(toggledOnDrawable);
            }
            mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mToggleButton.setBackgroundDrawable(toggledOnDrawable);
                        if (!sharedPreferences.contains(mMovie.getTitle())) {
                            editor.putInt(mMovie.getTitle(), mMovie.getId()).apply();
                            mDBHelper.addMovie(mMovie);
                        }
                    } else {
                        mToggleButton.setBackgroundDrawable(toggledOffDrawable);
                        sharedPreferences.edit().remove(mMovie.getTitle()).apply();
                        mDBHelper.deleteMovie(mMovie.getId());
                    }
                }

            });

        }
    }

    private void getMovieTrailer(Movie movie) {

        MoviesDataBaseAPI service = MoviesDataBaseAPI.Factory.getInstance();
        Call<Videos> call = service.getVideo(movie.getId(), BuildConfig.MOVIES_TMDB_API_KEY);
        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, final Response<Videos> response) {

                List<VideoResult> videoResults = response.body().getResults();
                if (videoResults.size() != 0) {
                    ThumbnailTrailerAdapter adapter = new ThumbnailTrailerAdapter(getActivity(), videoResults);
                    mTrailerRecyclerView.setAdapter(adapter);
                } else {
                    mTrailersCardView.setVisibility(View.GONE);
                }
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
                if (reviewResults.size() != 0) {
                    ReviewsAdapter adapter = new ReviewsAdapter(getActivity(), reviewResults);
                    mReviewsRecyclerView.setAdapter(adapter);
                } else {
                    mReviewsCardView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {

            }
        });
    }

    public void setActionBar() {
        ((MovieDetailActivity) getActivity()).setSupportActionBar(mToolbar);
        ((MovieDetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
