package com.example.benmedcalf.popularmovies.Event;

import com.example.benmedcalf.popularmovies.Model.Movie;

public class MovieSelectedEvent
{
    Movie movie;

    public MovieSelectedEvent(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

}
