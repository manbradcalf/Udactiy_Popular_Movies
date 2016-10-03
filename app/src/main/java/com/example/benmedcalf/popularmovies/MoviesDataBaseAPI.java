package com.example.benmedcalf.popularmovies;

import com.example.benmedcalf.popularmovies.Model.Example;
import com.example.benmedcalf.popularmovies.Model.Video;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ben.medcalf on 5/13/16.
 */
public interface MoviesDataBaseAPI {

    String BASE_URL = "http://api.themoviedb.org/3/";
    String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    //Returns JSON for top rated movies
    @GET("movie/top_rated")
    Call<Example> getTopRated(@Query("api_key") String api_key);

    //Returns JSON for most popular movies
    @GET("movie/popular")
    Call<Example>  getMostPopular(@Query("api_key") String api_key);

    //Returns unique ID for Youtube video
    @GET("movie/{id}/videos")
    Call<Video> getVideo(@Path("id") Integer movieId, @Query("api_key") String api_key);

    class Factory {

        private static MoviesDataBaseAPI service;

        public static MoviesDataBaseAPI getInstance() {

            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                service = retrofit.create(MoviesDataBaseAPI.class);
                return service;
            } else {
                return service;
            }

        }
    }
}


