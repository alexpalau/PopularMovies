package com.alexpalau.popmovies.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alexpalau.popmovies.popularmovies.data.MoviesContract.FavoriteEntry;
import com.alexpalau.popmovies.popularmovies.data.MoviesContract.PopularEntry;
import com.alexpalau.popmovies.popularmovies.data.MoviesContract.TopRatedEntry;


/**
 * Created by apalau on 15/03/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    /*
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_POPULAR_TABLE =

                "CREATE TABLE " + PopularEntry.TABLE_NAME + " (" +

                        PopularEntry._ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        PopularEntry.COLUMN_MOVIE_ID     + " INTEGER NOT NULL, "                 +
                        PopularEntry.COLUMN_TITLE        + " TEXT NOT NULL, "                 +
                        PopularEntry.COLUMN_IMAGE        + " TEXT NOT NULL, "                 +
                        PopularEntry.COLUMN_OVERVIEW     + " TEXT NOT NULL, "                 +
                        PopularEntry.COLUMN_RATING       + " INTEGER NOT NULL, "                 +
                        PopularEntry.COLUMN_POPULARITY   + " INTEGER NOT NULL, "                 +
                        PopularEntry.COLUMN_DATE         + " TEXT NOT NULL, "                 +

                /*
                 * To ensure this table can only contain one movie_id entry , we declare
                 * the date column to be unique. We also specify "ON CONFLICT REPLACE". This tells
                 * SQLite that if we have a movie entry with an id and we attempt to
                 * insert another movie entry with that id, we replace the old movie entry.
                 */
                        " UNIQUE (" + PopularEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_TOP_RATED_TABLE =

                "CREATE TABLE " + TopRatedEntry.TABLE_NAME + " (" +

                        TopRatedEntry._ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        TopRatedEntry.COLUMN_MOVIE_ID     + " INTEGER NOT NULL, "                 +
                        TopRatedEntry.COLUMN_TITLE        + " TEXT NOT NULL, "                 +
                        TopRatedEntry.COLUMN_IMAGE        + " TEXT NOT NULL, "                 +
                        TopRatedEntry.COLUMN_OVERVIEW     + " TEXT NOT NULL, "                 +
                        TopRatedEntry.COLUMN_RATING       + " INTEGER NOT NULL, "                 +
                        TopRatedEntry.COLUMN_POPULARITY    + " INTEGER NOT NULL, "                 +
                        TopRatedEntry.COLUMN_DATE         + " TEXT NOT NULL, "                 +

                        " UNIQUE (" + TopRatedEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_FAVORITES_TABLE =

                "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +

                        FavoriteEntry._ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        FavoriteEntry.COLUMN_MOVIE_ID     + " INTEGER NOT NULL, "                 +
                        FavoriteEntry.COLUMN_TITLE        + " TEXT NOT NULL, "                 +
                        FavoriteEntry.COLUMN_IMAGE        + " TEXT NOT NULL, "                 +
                        FavoriteEntry.COLUMN_OVERVIEW     + " TEXT NOT NULL, "                 +
                        FavoriteEntry.COLUMN_RATING       + " INTEGER NOT NULL, "                 +
                        FavoriteEntry.COLUMN_POPULARITY   + " INTEGER NOT NULL, "                 +
                        FavoriteEntry.COLUMN_DATE         + " TEXT NOT NULL, "                 +

                        " UNIQUE (" + FavoriteEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_REVIEWS_TABLE =

                "CREATE TABLE " + MoviesContract.ReviewEntry.TABLE_NAME + " (" +

                        MoviesContract.ReviewEntry._ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MoviesContract.ReviewEntry.COLUMN_MOVIE_ID     + " INTEGER NOT NULL, "                 +
                        MoviesContract.ReviewEntry.COLUMN_REVIEW_ID    + " TEXT NOT NULL, "                 +
                        MoviesContract.ReviewEntry.COLUMN_AUTHOR       + " TEXT NOT NULL, "                 +
                        MoviesContract.ReviewEntry.COLUMN_CONTENT      + " TEXT NOT NULL, "                 +
                        MoviesContract.ReviewEntry.COLUMN_URL          + " TEXT NOT NULL, "                 +

                        " UNIQUE (" + MoviesContract.ReviewEntry.COLUMN_MOVIE_ID  + "," + MoviesContract.ReviewEntry.COLUMN_REVIEW_ID  +") ON CONFLICT REPLACE);";

        final String SQL_CREATE_VIDEOS_TABLE =

                "CREATE TABLE " + MoviesContract.VideoEntry.TABLE_NAME + " (" +

                        MoviesContract.VideoEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MoviesContract.VideoEntry.COLUMN_MOVIE_ID    + " INTEGER NOT NULL, "                 +
                        MoviesContract.VideoEntry.COLUMN_VIDEO_ID    + " TEXT NOT NULL, "                 +
                        MoviesContract.VideoEntry.COLUMN_KEY         + " TEXT NOT NULL, "                 +
                        MoviesContract.VideoEntry.COLUMN_NAME        + " TEXT NOT NULL, "                 +

                        " UNIQUE (" + MoviesContract.VideoEntry.COLUMN_MOVIE_ID  + "," + MoviesContract.VideoEntry.COLUMN_VIDEO_ID  +") ON CONFLICT REPLACE);";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite databases objects.
         */
        db.execSQL(SQL_CREATE_POPULAR_TABLE);
        db.execSQL(SQL_CREATE_TOP_RATED_TABLE);
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
        db.execSQL(SQL_CREATE_REVIEWS_TABLE);
        db.execSQL(SQL_CREATE_VIDEOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + PopularEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + TopRatedEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.ReviewEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.VideoEntry.TABLE_NAME);
//        onCreate(db);
    }
}
