package com.alexpalau.popmovies.popularmovies.model;

/**
 * Created by apalau on 20/03/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MoviesResponse {

    @SerializedName("results")
    private List<Movie> results;


    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }


}
