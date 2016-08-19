package com.example.benmedcalf.popularmovies.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.benmedcalf.popularmovies.Database.FavoriteMoviesContract.FavoritesEntry;

/**
 * Created by ben.medcalf on 8/18/16.
 */
public class FavoriteMoviesDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FavoriteMovies.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String FLOAT_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE" + FavoritesEntry.TABLE_NAME + " (" +
                    FavoritesEntry._ID + " INTEGER PRIMARY KEY," +
                    FavoritesEntry.COLUMN_MOVIE_ID + INT_TYPE + " NOT NULL" + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_TITLE + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_OVERVIEW + TEXT_TYPE + " NOT NULL" +  COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_POPULARITY + FLOAT_TYPE + " NOT NULL" + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_VOTE_COUNT + INT_TYPE + " NOT NULL" + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_VOTE_AVERAGE + FLOAT_TYPE + " NOT NULL" + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                    // SQLite 3 has no boolean data type. Booleans are stored as INTEGER,
                    // 0 for false and 1 for true
                    FavoritesEntry.COLUMN_MOVIE_FAVORED + INT_TYPE + " NOT NULL" + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_POSTER_PATH + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_BACKDROP_PATH + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME;

    public FavoriteMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
