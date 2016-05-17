package com.example.benmedcalf.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ben.medcalf on 5/13/16.
 */
public interface MoviesDataBaseAPI {

    //Returns JSON for top rated movies
    @GET("movie/top_rated")
    public Call<MoviesResponse> getTopRated(@Query("api_key") String api_key);

    //Returns JSON for most popular movies
    @GET("movie/popular")
    public Call<MoviesResponse>  getMostPopular(@Query("api_key") String api_key);
}


