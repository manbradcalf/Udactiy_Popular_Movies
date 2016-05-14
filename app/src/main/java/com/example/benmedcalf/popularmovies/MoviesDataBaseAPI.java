package com.example.benmedcalf.popularmovies;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ben.medcalf on 5/13/16.
 */
public interface MoviesDataBaseAPI {

    //Returns JSON for top rated movies
    @GET("3/movie/top_rated")
    Call<List<Movie>> getTopRated(@Query("api_key") String api_key);

    //Returns JSON for most popular movies
    @GET
    Call<List<Movie>>  getMostPopular(@Query("api_key") String api_key);
}


