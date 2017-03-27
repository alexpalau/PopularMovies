package com.alexpalau.popmovies.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.alexpalau.popmovies.popularmovies.BuildConfig;
import com.alexpalau.popmovies.popularmovies.data.MoviesContract;
import com.alexpalau.popmovies.popularmovies.model.Movie;
import com.alexpalau.popmovies.popularmovies.model.Review;
import com.alexpalau.popmovies.popularmovies.model.Video;

import java.util.List;

/**
 * Created by apalau on 03/02/2017.
 */

public class Utilities {

    private static final String TAG = Utilities.class.getSimpleName();

    private static final String API_MOVIES_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM = "api_key";
    public static final String API_KEY_VALUE= BuildConfig.MOVIEDB__API_KEY;

    private static final String BASIC_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String BASIC_IMAGE_SIZE = "w185/";

    public static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";


    public static final String SORT_ACTION = "sort";
    public static final String TYPE_POPULAR = "popular";
    public static final String TYPE_TOP_RATED = "top_rated";
    public static final String TYPE_FAVORITE = "favorites";

    public static final String[] POPULAR_PROJECTION = {
            MoviesContract.PopularEntry.COLUMN_MOVIE_ID,
            MoviesContract.PopularEntry.COLUMN_TITLE,
            MoviesContract.PopularEntry.COLUMN_IMAGE,
            MoviesContract.PopularEntry.COLUMN_OVERVIEW,
            MoviesContract.PopularEntry.COLUMN_RATING,
            MoviesContract.PopularEntry.COLUMN_POPULARITY,
            MoviesContract.PopularEntry.COLUMN_DATE
    };

    public static final String[] TOP_RATED_PROJECTION = {
            MoviesContract.TopRatedEntry.COLUMN_MOVIE_ID,
            MoviesContract.TopRatedEntry.COLUMN_TITLE,
            MoviesContract.TopRatedEntry.COLUMN_IMAGE,
            MoviesContract.TopRatedEntry.COLUMN_OVERVIEW,
            MoviesContract.TopRatedEntry.COLUMN_RATING,
            MoviesContract.TopRatedEntry.COLUMN_POPULARITY,
            MoviesContract.TopRatedEntry.COLUMN_DATE
    };

    public static final String[] FAVORITES_PROJECTION = {
            MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID,
            MoviesContract.FavoriteEntry.COLUMN_TITLE,
            MoviesContract.FavoriteEntry.COLUMN_IMAGE,
            MoviesContract.FavoriteEntry.COLUMN_OVERVIEW,
            MoviesContract.FavoriteEntry.COLUMN_RATING,
            MoviesContract.FavoriteEntry.COLUMN_POPULARITY,
            MoviesContract.FavoriteEntry.COLUMN_DATE
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_TITLE = 1;
    public static final int INDEX_IMAGE = 2;
    public static final int INDEX_OVERVIEW = 3;
    public static final int INDEX_RATING = 4;
    public static final int INDEX_POPULARITY = 5;
    public static final int INDEX_DATE = 6;

    /* REVIEWS */

    public static final String[] REVIEWS_PROJECTION = {
            MoviesContract.ReviewEntry.COLUMN_MOVIE_ID,
            MoviesContract.ReviewEntry.COLUMN_REVIEW_ID,
            MoviesContract.ReviewEntry.COLUMN_AUTHOR,
            MoviesContract.ReviewEntry.COLUMN_CONTENT,
            MoviesContract.ReviewEntry.COLUMN_URL
    };

    public static final int INDEX_REVIEW_MOVIE_ID = 0;
    public static final int INDEX_REVIEW_ID = 1;
    public static final int INDEX_REVIEW_AUTHOR = 2;
    public static final int INDEX_REVIEW_CONTENT = 3;
    public static final int INDEX_REVIEW_URL = 4;

    /* VIDEOS */

    public static final String[] VIDEOS_PROJECTION = {
            MoviesContract.VideoEntry.COLUMN_MOVIE_ID,
            MoviesContract.VideoEntry.COLUMN_VIDEO_ID,
            MoviesContract.VideoEntry.COLUMN_KEY,
            MoviesContract.VideoEntry.COLUMN_NAME
    };

    public static final int INDEX_VIDEO_MOVIE_ID = 0;
    public static final int INDEX_VIDEO_ID = 1;
    public static final int INDEX_VIDEO_KEY = 2;
    public static final int INDEX_VIDEO_NAME = 3;


    public static String buildImageUrl(String file_path){
        return BASIC_IMAGE_URL + BASIC_IMAGE_SIZE + file_path;
    }

    public static int calculateColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void openLink(String url, Context context) {

        Uri link = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, link);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static ContentValues[] getContentValuesFromMovies(List<Movie> movies, String type) {

        ContentValues[] moviesContentValues = new ContentValues[movies.size()];
        for (int i = 0; i < movies.size() ; i++) {
            Movie movie = movies.get(i);
            ContentValues movieValues = new ContentValues();

            switch (type){
                case TYPE_POPULAR:
                    movieValues.put(MoviesContract.PopularEntry.COLUMN_MOVIE_ID, movie.getId());
                    movieValues.put(MoviesContract.PopularEntry.COLUMN_TITLE, movie.getOriginalTitle());
                    movieValues.put(MoviesContract.PopularEntry.COLUMN_IMAGE, movie.getPosterPath());
                    movieValues.put(MoviesContract.PopularEntry.COLUMN_DATE, movie.getReleaseDate());
                    movieValues.put(MoviesContract.PopularEntry.COLUMN_OVERVIEW, movie.getOverview());
                    movieValues.put(MoviesContract.PopularEntry.COLUMN_RATING, movie.getVoteAverage());
                    movieValues.put(MoviesContract.PopularEntry.COLUMN_POPULARITY, movie.getPopularity());

                case TYPE_TOP_RATED:
                    movieValues.put(MoviesContract.TopRatedEntry.COLUMN_MOVIE_ID, movie.getId());
                    movieValues.put(MoviesContract.TopRatedEntry.COLUMN_TITLE, movie.getOriginalTitle());
                    movieValues.put(MoviesContract.TopRatedEntry.COLUMN_IMAGE, movie.getPosterPath());
                    movieValues.put(MoviesContract.TopRatedEntry.COLUMN_DATE, movie.getReleaseDate());
                    movieValues.put(MoviesContract.TopRatedEntry.COLUMN_OVERVIEW, movie.getOverview());
                    movieValues.put(MoviesContract.TopRatedEntry.COLUMN_RATING, movie.getVoteAverage());
                    movieValues.put(MoviesContract.TopRatedEntry.COLUMN_POPULARITY, movie.getPopularity());

            }
            moviesContentValues[i]=movieValues;

        }

        return moviesContentValues;
    }

    public static ContentValues[] getContentValuesFromReviews(List<Review> reviews, int movieId) {

        ContentValues[] reviewsContentValues = new ContentValues[reviews.size()];
        for (int i = 0; i < reviews.size() ; i++) {
            Review review = reviews.get(i);
            ContentValues reviewValues = new ContentValues();
            reviewValues.put(MoviesContract.ReviewEntry.COLUMN_MOVIE_ID, movieId);
            reviewValues.put(MoviesContract.ReviewEntry.COLUMN_REVIEW_ID, review.getId());
            reviewValues.put(MoviesContract.ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
            reviewValues.put(MoviesContract.ReviewEntry.COLUMN_CONTENT, review.getContent());
            reviewValues.put(MoviesContract.ReviewEntry.COLUMN_URL, review.getUrl());

            reviewsContentValues[i]=reviewValues;

        }

        return reviewsContentValues;
    }

    public static ContentValues[] getContentValuesFromVideos(List<Video> videos, int movieId) {

        ContentValues[] videosContentValues = new ContentValues[videos.size()];
        for (int i = 0; i < videos.size() ; i++) {
            Video video = videos.get(i);
            ContentValues videoValues = new ContentValues();
            videoValues.put(MoviesContract.VideoEntry.COLUMN_MOVIE_ID, movieId);
            videoValues.put(MoviesContract.VideoEntry.COLUMN_VIDEO_ID, video.getId());
            videoValues.put(MoviesContract.VideoEntry.COLUMN_KEY, video.getKey());
            videoValues.put(MoviesContract.VideoEntry.COLUMN_NAME,video.getName());


            videosContentValues[i]=videoValues;

        }

        return videosContentValues;
    }


    public static ContentValues getContentValuesFavorite(Cursor data) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID, data.getInt(Utilities.INDEX_MOVIE_ID));
        movieValues.put(MoviesContract.FavoriteEntry.COLUMN_TITLE, data.getString(Utilities.INDEX_TITLE));
        movieValues.put(MoviesContract.FavoriteEntry.COLUMN_IMAGE, data.getString(Utilities.INDEX_IMAGE));
        movieValues.put(MoviesContract.FavoriteEntry.COLUMN_DATE, data.getString(Utilities.INDEX_DATE));
        movieValues.put(MoviesContract.FavoriteEntry.COLUMN_OVERVIEW, data.getString(Utilities.INDEX_OVERVIEW));
        movieValues.put(MoviesContract.FavoriteEntry.COLUMN_RATING, data.getInt(Utilities.INDEX_RATING));
        movieValues.put(MoviesContract.FavoriteEntry.COLUMN_POPULARITY, data.getInt(Utilities.INDEX_POPULARITY));
        return movieValues;
    }
}
