package com.alexpalau.popmovies.popularmovies.model;

/**
 * Created by apalau on 20/03/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class VideosResponse {

    @SerializedName("results")
    private List<Video> results;


    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }


}
