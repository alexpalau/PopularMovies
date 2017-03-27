package com.alexpalau.popmovies.popularmovies.rest;

/**
 * Created by apalau on 20/03/2017.
 */


import com.alexpalau.popmovies.popularmovies.model.MoviesResponse;
import com.alexpalau.popmovies.popularmovies.model.ReviewsResponse;
import com.alexpalau.popmovies.popularmovies.model.VideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideosResponse> getMovieVideos(@Path("id") int id, @Query("api_key") String apiKey);


}
