package com.alexpalau.popmovies.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by apalau on 15/03/2017.
 */

public class MoviesProvider extends ContentProvider {

    /* POPULAR */
    public static final int CODE_POPULAR = 100;
    public static final int CODE_POPULAR_WITH_ID = 101;

    /* TOP_RATED */
    public static final int CODE_TOP_RATED = 200;
    public static final int CODE_TOP_RATED_WITH_ID = 201;

    /* FAVORITES */
    public static final int CODE_FAVORITES = 300;
    public static final int CODE_FAVORITES_WITH_ID = 301;

    /* REVIEWS */
    public static final int CODE_REVIEWS = 400;
    public static final int CODE_REVIEWS_WITH_ID = 401;

    /* VIDEOS */
    public static final int CODE_VIDEOS = 500;
    public static final int CODE_VIDEOS_WITH_ID = 501;

    /*
     * The URI Matcher used by this content provider.
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

         /* This URI is content://com.alexpalau.popmovies.popularmovies/popular/ */
        matcher.addURI(authority, MoviesContract.PATH_POPULAR, CODE_POPULAR);
        /* This URI would look something like content://com.alexpalau.popmovies.popularmovies/popular/1472214172 */
        matcher.addURI(authority, MoviesContract.PATH_POPULAR + "/#", CODE_POPULAR_WITH_ID);

        matcher.addURI(authority, MoviesContract.PATH_TOP_RATED, CODE_TOP_RATED);
        matcher.addURI(authority, MoviesContract.PATH_TOP_RATED + "/#", CODE_TOP_RATED_WITH_ID);

        matcher.addURI(authority, MoviesContract.PATH_FAVORITES, CODE_FAVORITES);
        matcher.addURI(authority, MoviesContract.PATH_FAVORITES + "/#", CODE_FAVORITES_WITH_ID);

        matcher.addURI(authority, MoviesContract.PATH_REVIEWS, CODE_REVIEWS);
        matcher.addURI(authority, MoviesContract.PATH_REVIEWS + "/#", CODE_REVIEWS_WITH_ID);

        matcher.addURI(authority, MoviesContract.PATH_VIDEOS, CODE_VIDEOS);
        matcher.addURI(authority, MoviesContract.PATH_VIDEOS + "/#", CODE_VIDEOS_WITH_ID);

        return matcher;

    }



    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsInserted = 0;
        switch (sUriMatcher.match(uri)) {

            case CODE_POPULAR:

                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MoviesContract.PopularEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            case CODE_TOP_RATED:

                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MoviesContract.TopRatedEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            case CODE_REVIEWS:

                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MoviesContract.ReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            case CODE_VIDEOS:

                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MoviesContract.VideoEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        switch (sUriMatcher.match(uri)){

            case CODE_POPULAR: {

                cursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.PopularEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_POPULAR_WITH_ID: {

                String movieId = uri.getPathSegments().get(1);
                String[] selectionArguments = new String[]{movieId};
                cursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.PopularEntry.TABLE_NAME,
                        projection,
                        MoviesContract.PopularEntry.COLUMN_MOVIE_ID + "=?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;

            }case CODE_TOP_RATED: {

                cursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.TopRatedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_TOP_RATED_WITH_ID: {

                String movieId = uri.getPathSegments().get(1);
                String[] selectionArguments = new String[]{movieId};
                cursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.TopRatedEntry.TABLE_NAME,
                        projection,
                        MoviesContract.TopRatedEntry.COLUMN_MOVIE_ID + "=?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;

            }case CODE_FAVORITES: {

                cursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_FAVORITES_WITH_ID: {

                String movieId = uri.getPathSegments().get(1);
                String[] selectionArguments = new String[]{movieId};
                cursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + "=?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;

            }

            case CODE_REVIEWS_WITH_ID: {

                String movieId = uri.getPathSegments().get(1);
                String[] selectionArguments = new String[]{movieId};
                cursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.ReviewEntry.TABLE_NAME,
                        projection,
                        MoviesContract.ReviewEntry.COLUMN_MOVIE_ID + "=?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_VIDEOS_WITH_ID: {

                String movieId = uri.getPathSegments().get(1);
                String[] selectionArguments = new String[]{movieId};
                cursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.VideoEntry.TABLE_NAME,
                        projection,
                        MoviesContract.VideoEntry.COLUMN_MOVIE_ID + "=?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        long id;
        switch (sUriMatcher.match(uri)){

            case CODE_POPULAR:

                id = db.insert(MoviesContract.PopularEntry.TABLE_NAME,null,values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(MoviesContract.PopularEntry.CONTENT_URI,id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            case CODE_TOP_RATED:

                id = db.insert(MoviesContract.TopRatedEntry.TABLE_NAME,null,values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(MoviesContract.TopRatedEntry.CONTENT_URI,id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            case CODE_FAVORITES:

                id = db.insert(MoviesContract.FavoriteEntry.TABLE_NAME,null,values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(MoviesContract.FavoriteEntry.CONTENT_URI,id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);

        //return constructed uri
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted=0;
        String movieId = uri.getPathSegments().get(1);
        String[] whereArguments = new String[]{movieId};
        switch (sUriMatcher.match(uri)){

            case CODE_POPULAR_WITH_ID:
                rowsDeleted = db.delete(MoviesContract.PopularEntry.TABLE_NAME, MoviesContract.PopularEntry.COLUMN_MOVIE_ID + "=?",whereArguments);
                break;

            case CODE_TOP_RATED_WITH_ID:
                rowsDeleted = db.delete(MoviesContract.TopRatedEntry.TABLE_NAME, MoviesContract.TopRatedEntry.COLUMN_MOVIE_ID + "=?",whereArguments);
                break;

            case CODE_FAVORITES_WITH_ID:
                rowsDeleted = db.delete(MoviesContract.FavoriteEntry.TABLE_NAME, MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + "=?",whereArguments);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
