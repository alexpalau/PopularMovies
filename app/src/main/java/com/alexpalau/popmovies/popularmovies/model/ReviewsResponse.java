package com.alexpalau.popmovies.popularmovies.model;

/**
 * Created by apalau on 20/03/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ReviewsResponse {

    @SerializedName("results")
    private List<Review> results;


    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }


}
