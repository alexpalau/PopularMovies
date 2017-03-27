package com.alexpalau.popmovies.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by apalau on 15/03/2017.
 */

public class MoviesContract {

    /*
    * The "Content authority" is a name for the entire content provider, similar to the
    * relationship between a domain name and its website. A convenient string to use for the
    * content authority is the package name for the app, which is guaranteed to be unique on the
    * Play Store.
    */
    public static final String CONTENT_AUTHORITY = "com.alexpalau.popmovies.popularmovies";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOP_RATED = "top_rated";
    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_MOVIE = "videos";

    /* Inner class that defines the table contents of the popular table */
    public static final class PopularEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Movie table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_POPULAR)
                .build();
        /* Used internally as the name of our popular table. */
        public static final String TABLE_NAME = "popular";

        /* Movie ID as returned by API */
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "posterImage";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "voteAverage";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_DATE = "date";


        public static Uri buildPopularUriId(int movieId ) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .build();
        }




    }

    /* Inner class that defines the table contents of the popular table */
    public static final class TopRatedEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the TopRated table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TOP_RATED)
                .build();
        /* Used internally as the name of our top_rated table. */
        public static final String TABLE_NAME = "top_rated";

        /* Movie ID as returned by API */
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "posterImage";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "voteAverage";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_DATE = "date";


        public static Uri buildTopRatedUriId(int movieId ) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .build();
        }




    }

    /* Inner class that defines the table contents of the favorite table */
    public static final class FavoriteEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the TopRated table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();
        /* Used internally as the name of our favorites table. */
        public static final String TABLE_NAME = "favorite";

        /* Movie ID as returned by API */
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "posterImage";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "voteAverage";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_DATE = "date";


        public static Uri buildFavoriteUriId(int movieId ) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .build();
        }




    }

    /* Inner class that defines the table contents of the favorite table */
    public static final class ReviewEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Reviews table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEWS)
                .build();
        /* Used internally as the name of our reviews table. */
        public static final String TABLE_NAME = "reviews";

        /* Movie ID as returned by API */
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";


        public static Uri buildReviewsUriId(int movieId ) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .build();
        }



    }

    /* Inner class that defines the table contents of the videos table */
    public static final class VideoEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Videos table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_VIDEOS)
                .build();
        /* Used internally as the name of our reviews table. */
        public static final String TABLE_NAME = "videos";

        /* Movie ID as returned by API */
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";


        public static Uri buildVideosUriId(int movieId ) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .build();
        }



    }
}
