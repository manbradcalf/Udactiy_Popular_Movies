package com.example.benmedcalf.popularmovies.Database;

import android.provider.BaseColumns;

/**
 * Created by ben.medcalf on 8/18/16.
 */
public final class FavoriteMoviesContract {
    // To Prevent someone from accidentally instantiating the cotract class,
    // give it an empty constructor

    public FavoriteMoviesContract() {

    }

    /* Inner class that defines the table contents */
    public static abstract class FavoritesEntry implements BaseColumns {
            public static final String TABLE_NAME = "movies";
            public static final String COLUMN_MOVIE_ID = "movie_id";
            public static final String COLUMN_MOVIE_TITLE = "movie_title";
            public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";
            public static final String COLUMN_MOVIE_POPULARITY = "movie_popularity";
            public static final String COLUMN_MOVIE_VOTE_COUNT = "movie_vote_count";
            public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movie_vote_average";
            public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";
            public static final String COLUMN_MOVIE_FAVORED = "movie_favored";
            public static final String COLUMN_MOVIE_POSTER_PATH = "movie_poster_path";
            public static final String COLUMN_MOVIE_BACKDROP_PATH = "movie_backdrop_path";
    }
}
