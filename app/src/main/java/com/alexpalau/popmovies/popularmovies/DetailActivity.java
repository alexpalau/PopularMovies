package com.alexpalau.popmovies.popularmovies;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexpalau.popmovies.popularmovies.adapters.ReviewsAdapter;
import com.alexpalau.popmovies.popularmovies.adapters.VideosAdapter;
import com.alexpalau.popmovies.popularmovies.data.MoviesContract;
import com.alexpalau.popmovies.popularmovies.model.Review;
import com.alexpalau.popmovies.popularmovies.model.ReviewsResponse;
import com.alexpalau.popmovies.popularmovies.model.Video;
import com.alexpalau.popmovies.popularmovies.model.VideosResponse;
import com.alexpalau.popmovies.popularmovies.rest.ApiClient;
import com.alexpalau.popmovies.popularmovies.rest.ApiInterface;
import com.alexpalau.popmovies.popularmovies.utilities.Utilities;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements
//        ReviewsAdapter.ReviewsAdapterOnClickHandler,
        VideosAdapter.VideosAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private TextView mTitle;
    private TextView mRealeaseDate;
    private ImageView mPoster;
    private TextView mReview;
    private TextView mUserRating;
    private ImageButton mFavorite;

    private RecyclerView mReviewsRecyclerView;
    private ReviewsAdapter mReviewsAdapter;
    private int mPositionReviews = RecyclerView.NO_POSITION;


    private RecyclerView mVideosRecyclerView;
    private VideosAdapter mVideosAdapter;
    private int mPositionVideos = RecyclerView.NO_POSITION;

    private Uri mUriMovie;
    private String mTypeMovie;
    private static final int ID_POPULAR_DETAIL_LOADER = 51;
    private static final int ID_TOP_RATED_DETAIL_LOADER = 52;
    private static final int ID_FAVORITE_DETAIL_LOADER = 53;
    private static final int ID_CHECK_FAVORITE_LOADER = 54;
    private static final int ID_REVIEWS_LOADER = 55;
    private static final int ID_VIDEOS_LOADER = 56;

    private boolean isFavorite;
    private int mMovieId;
    private Cursor mCursorMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = (TextView) findViewById(R.id.tv_title);
        mRealeaseDate = (TextView) findViewById(R.id.tv_release_date);
        mPoster = (ImageView) findViewById(R.id.iv_poster);
        mReview = (TextView) findViewById(R.id.tv_review);
        mUserRating = (TextView) findViewById(R.id.tv_rating);
        mFavorite = (ImageButton) findViewById(R.id.b_favorite);

        mVideosRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_videos);
        mVideosRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mVideosRecyclerView.setHasFixedSize(true);
        mVideosAdapter = new VideosAdapter(this.getApplicationContext(),this);
        mVideosRecyclerView.setAdapter(mVideosAdapter);

        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mReviewsRecyclerView.setHasFixedSize(true);
//        mReviewsAdapter = new ReviewsAdapter(this.getApplicationContext(),this);
        mReviewsAdapter = new ReviewsAdapter(this.getApplicationContext());
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);


        mUriMovie = getIntent().getData();
        mTypeMovie= getIntent().getStringExtra(Utilities.SORT_ACTION);

        mMovieId = Integer.parseInt(mUriMovie.getPathSegments().get(1));
        isFavorite = isFavoriteMovie(mMovieId);
//        if(isFavorite) Toast.makeText(this, R.string.movie_favorite,Toast.LENGTH_LONG).show();
//        else Toast.makeText(this,"Movie NOT a favorites",Toast.LENGTH_LONG).show();

        if(mTypeMovie.equals(Utilities.TYPE_POPULAR)) getSupportLoaderManager().initLoader(ID_POPULAR_DETAIL_LOADER,null,this);
        else if(mTypeMovie.equals(Utilities.TYPE_TOP_RATED)) getSupportLoaderManager().initLoader(ID_TOP_RATED_DETAIL_LOADER,null,this);
        else getSupportLoaderManager().initLoader(ID_FAVORITE_DETAIL_LOADER,null,this);





    }

    private boolean isFavoriteMovie(int mMovieId) {
        getSupportLoaderManager().initLoader(ID_CHECK_FAVORITE_LOADER,null,this);

        ContentResolver favoritesContentResolver = getContentResolver();
        Uri favoriteUriId = MoviesContract.FavoriteEntry.buildFavoriteUriId(mMovieId);
        Cursor data =favoritesContentResolver.query(favoriteUriId,Utilities.FAVORITES_PROJECTION,null,null,null);
        return data.moveToFirst();
    }

    private void loadMovieVideos(Integer id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<VideosResponse> call = apiService.getMovieVideos(id, Utilities.API_KEY_VALUE);
        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                List<Video> videos = response.body().getResults();
                Log.d(TAG,"Reviews: Number of reviews received: "+ videos.size());
                if(videos != null && videos.size()!=0){

                    ContentValues[] videosValues = Utilities.getContentValuesFromVideos(videos,mMovieId);
                    ContentResolver videosContentResolver = getApplicationContext().getContentResolver();
                    videosContentResolver.bulkInsert(MoviesContract.VideoEntry.CONTENT_URI,videosValues);
                }
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void loadMovieReviews(Integer id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ReviewsResponse> call = apiService.getMovieReviews(id, Utilities.API_KEY_VALUE);
        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                List<Review> reviews = response.body().getResults();
                Log.d(TAG,"Videos: Number of videos received: "+ reviews.size());
                if(reviews != null && reviews.size()!=0){

                    ContentValues[] reviewValues = Utilities.getContentValuesFromReviews(reviews,mMovieId);
                    ContentResolver reviewsContentResolver = getApplicationContext().getContentResolver();
                    reviewsContentResolver.bulkInsert(MoviesContract.ReviewEntry.CONTENT_URI,reviewValues);
                }
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onClick(String videoKey) {
        String url = Utilities.YOUTUBE_URL + videoKey;
        Log.d(TAG,"Video URL: "+ url);
        Utilities.openLink(url,this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("SortBy", mTypeMovie);
        setResult(Activity.RESULT_OK, intent);
        finish();
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("SortBy", mTypeMovie);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
         ---------LOADER CALLBACKS--------------
        */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
         switch (id){
             case ID_POPULAR_DETAIL_LOADER:
                 return new CursorLoader(this,
                         mUriMovie,
                         Utilities.POPULAR_PROJECTION,
                         null,
                         null,
                         null);

             case ID_TOP_RATED_DETAIL_LOADER:
                 return new CursorLoader(this,
                         mUriMovie,
                         Utilities.TOP_RATED_PROJECTION,
                         null,
                         null,
                         null);
             case ID_FAVORITE_DETAIL_LOADER:
                 return new CursorLoader(this,
                         mUriMovie,
                         Utilities.FAVORITES_PROJECTION,
                         null,
                         null,
                         null);
             case ID_CHECK_FAVORITE_LOADER:
                 Uri favoriteUriId = MoviesContract.FavoriteEntry.buildFavoriteUriId(mMovieId);
                 return new CursorLoader(this,
                         favoriteUriId,
                         Utilities.FAVORITES_PROJECTION,
                         null,
                         null,
                         null);
             case ID_REVIEWS_LOADER:
                 Uri reviewsUri = MoviesContract.ReviewEntry.buildReviewsUriId(mMovieId);
                 return new CursorLoader(this,
                         reviewsUri,
                         Utilities.REVIEWS_PROJECTION,
                         null,
                         null,
                         null);
             case ID_VIDEOS_LOADER:
                 Uri videosUri = MoviesContract.VideoEntry.buildVideosUriId(mMovieId);
                 return new CursorLoader(this,
                         videosUri,
                         Utilities.VIDEOS_PROJECTION,
                         null,
                         null,
                         null);
             default:
                 throw new RuntimeException("Loader Not Implemented: " + id);
         }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            switch (loader.getId()){
                case ID_POPULAR_DETAIL_LOADER:
                case ID_TOP_RATED_DETAIL_LOADER:
                case ID_FAVORITE_DETAIL_LOADER:
                    populateMovieDetails(data);
                    mCursorMovie=data;
                    break;
                case ID_CHECK_FAVORITE_LOADER:
                    isFavorite=data.moveToFirst();
                    break;
                case ID_REVIEWS_LOADER:
                    if(data.moveToFirst()){
                        mReviewsRecyclerView.setVisibility(View.VISIBLE);
                        mReviewsAdapter.swapCursor(data);
                        if (mPositionReviews == RecyclerView.NO_POSITION) mPositionReviews = 0;
                        mReviewsRecyclerView.smoothScrollToPosition(mPositionReviews);

                    }
                    else mReviewsRecyclerView.setVisibility(View.INVISIBLE);
                    break;
                case ID_VIDEOS_LOADER:
                    if(data.moveToFirst()){
                        mVideosRecyclerView.setVisibility(View.VISIBLE);
                        mVideosAdapter.swapCursor(data);
                        if (mPositionVideos == RecyclerView.NO_POSITION) mPositionVideos = 0;
                        mVideosRecyclerView.smoothScrollToPosition(mPositionVideos);

                    }
                    break;
                   // else mVideosRecyclerView.setVisibility(View.INVISIBLE);

                    //if (data.getCount() != 0) showMovies();

            }

    }

    private void populateMovieDetails(Cursor data) {
        if (data != null && data.getCount()>0) {
            data.moveToFirst();

            mTitle.setText(data.getString(Utilities.INDEX_TITLE));
            mRealeaseDate.setText( data.getString(Utilities.INDEX_DATE));
            String posterPath= Utilities.buildImageUrl(data.getString(Utilities.INDEX_IMAGE));
            Picasso.with(this.getApplicationContext()).load(posterPath).into(mPoster);
            mReview.setText(data.getString(Utilities.INDEX_OVERVIEW));
            mUserRating.setText(String.valueOf(data.getInt(Utilities.INDEX_RATING)));
            if (isFavorite) mFavorite.setImageResource(android.R.drawable.btn_star_big_on);
            //mFavorite.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

            //mMovieId= data.getInt(Utilities.INDEX_MOVIE_ID);
            if (Utilities.isOnline(getApplicationContext())) {
                loadMovieReviews(mMovieId);
                loadMovieVideos(mMovieId);
            }
            getSupportLoaderManager().initLoader(ID_REVIEWS_LOADER,null,this);
            getSupportLoaderManager().initLoader(ID_VIDEOS_LOADER,null,this);


        }
        else return;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        switch (loader.getId()){
//            case ID_POPULAR_DETAIL_LOADER:
//            case ID_TOP_RATED_DETAIL_LOADER:
//            case ID_FAVORITE_DETAIL_LOADER:
//                 mCursorMovie=null;
//            case ID_CHECK_FAVORITE_LOADER:
//                isFavorite=false;
//            case ID_REVIEWS_LOADER:
//                mReviewsAdapter.swapCursor(null);
//            case ID_VIDEOS_LOADER:
//                mVideosAdapter.swapCursor(null);
//        }
        mCursorMovie=null;
        isFavorite=false;
        mReviewsAdapter.swapCursor(null);
        mVideosAdapter.swapCursor(null);
    }

    public void favoriteYesNo(View view) {
        //mFavorite.setFocusable(true);
        //mFavorite.setPressed(true);
//        mFavorite.setFocusable(true);
//        mFavorite.setFocusableInTouchMode(true);
        //mFavorite.setHovered(true);
//        mFavorite.setActivated(true);
//        mFavorite.setSelected(true);
        //view.setSelected(!view.isSelected());
        //mFavorite.getBackground().setState(new int[]{android.R.attr.state_checked});
    //view.setBackgroundResource(android.R.drawable.btn_star_big_on);

        ContentResolver favoritesContentResolver = getContentResolver();
        //insert into favorite table
        if(!isFavorite){
            Uri favoriteUri = MoviesContract.FavoriteEntry.CONTENT_URI;
            ContentValues contentValues = Utilities.getContentValuesFavorite(mCursorMovie);
            favoritesContentResolver.insert(favoriteUri,contentValues);
            mFavorite.setImageResource(android.R.drawable.btn_star_big_on);
            Toast.makeText(this,"Movie added to favorites",Toast.LENGTH_LONG).show();

        }
        //delete from favorite table
        else {
            Uri favoriteUriId = MoviesContract.FavoriteEntry.buildFavoriteUriId(mMovieId);
            favoritesContentResolver.delete(favoriteUriId,null,null);
            mFavorite.setImageResource(android.R.drawable.btn_star_big_off);
            Toast.makeText(this,"Movie deleted from favorites",Toast.LENGTH_LONG).show();
        }
        isFavorite=!isFavorite;




    }
}
