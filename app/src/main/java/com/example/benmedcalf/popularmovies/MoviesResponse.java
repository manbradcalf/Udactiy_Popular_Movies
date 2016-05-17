package com.example.benmedcalf.popularmovies;

import com.example.benmedcalf.popularmovies.Model.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben.medcalf on 5/16/16.
 */
public class MoviesResponse {

    List<Movie> movies;

    //public constructor is necessary for collections
    public MoviesResponse() {
        movies = new ArrayList<Movie>();
    }

    public static MoviesResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        MoviesResponse moviesResponse = gson.fromJson(response, MoviesResponse.class);
        return moviesResponse;
    }
}