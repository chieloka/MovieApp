package com.nichobox.movieapp.rest;

import com.nichobox.movieapp.model.Movie;
import com.nichobox.movieapp.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("movie/{sort}")
    Call<MoviesResponse> getPopularMovies(@Path("sort") String sort,@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
