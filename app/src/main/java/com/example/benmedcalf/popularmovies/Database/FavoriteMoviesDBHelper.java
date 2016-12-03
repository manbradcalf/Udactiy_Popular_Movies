package com.example.benmedcalf.popularmovies.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.benmedcalf.popularmovies.Database.FavoriteMoviesContract.FavoritesEntry;
import com.example.benmedcalf.popularmovies.Model.Movie;

/**
 * Created by ben.medcalf on 8/18/16.
 */
public class FavoriteMoviesDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FavoriteMovies.db";
    private static final String NOT_NULL = " NOT NULL";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String FLOAT_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + FavoritesEntry.TABLE_NAME + " (" +
                    FavoritesEntry._ID + " INTEGER PRIMARY KEY," +
                    FavoritesEntry.COLUMN_MOVIE_ID + INT_TYPE + NOT_NULL + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_TITLE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_OVERVIEW + TEXT_TYPE + NOT_NULL +  COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_POPULARITY + FLOAT_TYPE + NOT_NULL + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_VOTE_COUNT + INT_TYPE + NOT_NULL + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_VOTE_AVERAGE + FLOAT_TYPE + NOT_NULL + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    // SQLite 3 has no boolean data type. Booleans are stored as INTEGER,
                    // 0 for false and 1 for true
                    FavoritesEntry.COLUMN_MOVIE_FAVORED + INT_TYPE + NOT_NULL + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_POSTER_PATH + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    FavoritesEntry.COLUMN_MOVIE_BACKDROP_PATH + TEXT_TYPE + NOT_NULL +
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
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addMovie(Movie movie) {

        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_POPULARITY, movie.getPopularity());
        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount());
        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
        values.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_FAVORED, movie.isFavorite());

        // Insert the new row, returning the primary key value of the new row
        db.insert(
                FavoriteMoviesContract.FavoritesEntry.TABLE_NAME,
                FavoriteMoviesContract.FavoritesEntry.COLUMN_NAME_NULLABLE,
                values);
    }

    public void deleteMovie(int movieId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(FavoritesEntry.TABLE_NAME, FavoritesEntry.COLUMN_MOVIE_ID + " = " + movieId, null);
    }
}
