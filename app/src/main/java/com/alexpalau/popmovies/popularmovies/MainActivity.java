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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alexpalau.popmovies.popularmovies.adapters.MoviesAdapter;
import com.alexpalau.popmovies.popularmovies.data.MoviesContract;
import com.alexpalau.popmovies.popularmovies.model.Movie;
import com.alexpalau.popmovies.popularmovies.model.MoviesResponse;
import com.alexpalau.popmovies.popularmovies.rest.ApiClient;
import com.alexpalau.popmovies.popularmovies.rest.ApiInterface;
import com.alexpalau.popmovies.popularmovies.utilities.Utilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.MoviesAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private MoviesAdapter mMoviesAdapter;
    private String mSortBy;

    private static final String ACTION_SORT_POPULAR = "popular";
    private static final String ACTION_SORT_TOP_RATED = "top_rated";
    private static final String ACTION_SORT_FAVORITES = "favorites";

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int ID_POPULAR_LOADER = 10;
    private static final int ID_TOP_RATED_LOADER = 20;
    private static final int ID_FAVORITES_LOADER = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator =(ProgressBar) findViewById(R.id.pb_loading_indicator);


        int mNoOfColumns = Utilities.calculateColumns(getApplicationContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,mNoOfColumns);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this.getApplicationContext(),this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        if (savedInstanceState!=null){
            mSortBy=savedInstanceState.getString(Utilities.SORT_ACTION);
        }

        loadMoviesData();

    }

    private void loadMoviesData() {

            if (mSortBy==null || mSortBy.equals(ACTION_SORT_POPULAR)) {
                if (Utilities.isOnline(getApplicationContext())) getPopularMoviesApi();
                mSortBy=ACTION_SORT_POPULAR;
//                getPopularMoviesDB();
                getSupportLoaderManager().initLoader(ID_POPULAR_LOADER,null,this);
            }
            else if (mSortBy.equals(ACTION_SORT_TOP_RATED)) {
                if (Utilities.isOnline(getApplicationContext())) getTopRatedMoviesApi();
//                getTopRatedMoviesDB();
                getSupportLoaderManager().initLoader(ID_TOP_RATED_LOADER,null,this);
            }
            else if (mSortBy.equals(ACTION_SORT_FAVORITES)) {
                getSupportLoaderManager().initLoader(ID_FAVORITES_LOADER,null,this);
            }
        //mRecyclerView.smoothScrollToPosition(mPosition);
        showMovies();
//            else if (mSortBy.equals(ACTION_SORT_FAVORITES)) getFavoriteMovies();
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
        //showErrorMessage();



//    private void getPopularMoviesDB() {
//        ContentResolver moviesContentResolver =getApplicationContext().getContentResolver();
//        Cursor data= moviesContentResolver.query(
//                    MoviesContract.PopularEntry.CONTENT_URI,
//                    Utilities.POPULAR_PROJECTION,
//                    null,
//                    null,
//                    MoviesContract.PopularEntry.COLUMN_POPULARITY + " DESC");
//        mMoviesAdapter.swapCursor(data);
//    }

//    private void getTopRatedMoviesDB() {
//        ContentResolver moviesContentResolver = getApplicationContext().getContentResolver();
//        Cursor data= moviesContentResolver.query(
//                MoviesContract.TopRatedEntry.CONTENT_URI,
//                Utilities.TOP_RATED_PROJECTION,
//                null,
//                null,
//                MoviesContract.TopRatedEntry.COLUMN_RATING + " DESC");
//        mMoviesAdapter.swapCursor(data);
//    }



    private void getPopularMoviesApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getPopularMovies(Utilities.API_KEY_VALUE);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                Log.d(TAG,getString(R.string.log_get_popular)+ movies.size());
                //mMoviesAdapter.setMoviesData(movies);
                if(movies != null && movies.size()!=0){

                    ContentValues[] popularValues = Utilities.getContentValuesFromMovies(movies,Utilities.TYPE_POPULAR);
                    ContentResolver moviesContentResolver = getApplicationContext().getContentResolver();
                    moviesContentResolver.bulkInsert(MoviesContract.PopularEntry.CONTENT_URI,popularValues);
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getTopRatedMoviesApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getTopRatedMovies(Utilities.API_KEY_VALUE);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                Log.d(TAG,"Top Rated: Number of movies received: "+ movies.size());
                //mMoviesAdapter.setMoviesData(movies);
                if(movies != null && movies.size()!=0){

                    ContentValues[] popularValues = Utilities.getContentValuesFromMovies(movies,Utilities.TYPE_TOP_RATED);
                    ContentResolver moviesContentResolver = getContentResolver();
                    moviesContentResolver.bulkInsert(MoviesContract.TopRatedEntry.CONTENT_URI,popularValues);
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }



    private void showMovies() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
//        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_sort_by_popularity){
            mSortBy=ACTION_SORT_POPULAR;
            loadMoviesData();
            return true;
        }
        if(id == R.id.action_sort_by_rating){
            mSortBy=ACTION_SORT_TOP_RATED;
            loadMoviesData();
            return true;
        }
        if(id == R.id.action_sort_by_favorites){
            mSortBy=ACTION_SORT_FAVORITES;
            loadMoviesData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int idMovie) {
        Intent movieDetailIntent = new Intent(MainActivity.this,DetailActivity.class);
        Uri uriForMovieClicked=null;
        switch (mSortBy){
            case ACTION_SORT_POPULAR:
                uriForMovieClicked = MoviesContract.PopularEntry.buildPopularUriId(idMovie);
                movieDetailIntent.putExtra(Utilities.SORT_ACTION,Utilities.TYPE_POPULAR);
                break;
            case ACTION_SORT_TOP_RATED:
                uriForMovieClicked = MoviesContract.TopRatedEntry.buildTopRatedUriId(idMovie);
                movieDetailIntent.putExtra(Utilities.SORT_ACTION,Utilities.TYPE_TOP_RATED);
                break;
            case ACTION_SORT_FAVORITES:
                uriForMovieClicked = MoviesContract.FavoriteEntry.buildFavoriteUriId(idMovie);
                movieDetailIntent.putExtra(Utilities.SORT_ACTION,Utilities.TYPE_FAVORITE);
                break;
        }
        movieDetailIntent.setData(uriForMovieClicked);
//        startActivity(movieDetailIntent);
        startActivityForResult(movieDetailIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                mSortBy=data.getStringExtra("SortBy");
                Toast.makeText(this,"Return for DetailActivity sort by: "+ mSortBy,Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Utilities.SORT_ACTION,mSortBy);
        super.onSaveInstanceState(outState);
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        mSortBy=savedInstanceState.getString(Utilities.SORT_ACTION);
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    /*
                ---------LOADER CALLBACKS--------------
            */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case ID_POPULAR_LOADER:
                Uri popularUri = MoviesContract.PopularEntry.CONTENT_URI;
                return new CursorLoader(this,
                        popularUri,
                        Utilities.POPULAR_PROJECTION,
                        null,
                        null,
                        MoviesContract.PopularEntry.COLUMN_POPULARITY + " DESC");

            case ID_TOP_RATED_LOADER:
                Uri topRatedUri = MoviesContract.TopRatedEntry.CONTENT_URI;
                return new CursorLoader(this,
                        topRatedUri,
                        Utilities.TOP_RATED_PROJECTION,
                        null,
                        null,
                        MoviesContract.TopRatedEntry.COLUMN_RATING + " DESC");

            case ID_FAVORITES_LOADER:
                Uri favoritesUri = MoviesContract.FavoriteEntry.CONTENT_URI;
                return new CursorLoader(this,
                        favoritesUri,
                        Utilities.TOP_RATED_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mMoviesAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showMovies();
//        loadMoviesData();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }


}
