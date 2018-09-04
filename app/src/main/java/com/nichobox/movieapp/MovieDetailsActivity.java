package com.nichobox.movieapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.nichobox.movieapp.adapter.MovieAdapter;
import com.nichobox.movieapp.model.Movie;
import com.nichobox.movieapp.model.MoviesResponse;
import com.nichobox.movieapp.rest.ApiClient;
import com.nichobox.movieapp.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private final static String API_KEY = "";
    private ImageView poster;
    private TextView title;
    private TextView releaseDate;
    private TextView overView;
    private TextView voteAverage;
    private int movieId;
    private ProgressDialog pDialog;
    private Movie movie;
    private String posterPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            movieId = Integer.parseInt(intent.getStringExtra(Intent.EXTRA_TEXT));
        }else{
            finish();
        }

        pDialog = new ProgressDialog(this);

        poster = findViewById(R.id.poster);
        title = findViewById(R.id.title_tv);
        releaseDate = findViewById(R.id.release_tv);
        overView = findViewById(R.id.plot_tv);
        voteAverage = findViewById(R.id.vote_averge_tv);

        pDialog.setMessage("Loading movie");
        pDialog.show();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Movie> call = apiService.getMovieDetails(movieId,API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie>call, Response<Movie> response) {
                pDialog.hide();
                movie = response.body();
                title.setText(movie.getTitle());
                releaseDate.setText(movie.getReleaseDate());
                overView.setText(movie.getOverview());
                voteAverage.setText(movie.getVoteAverage().toString());
                posterPath = response.body().getPosterPath();
                Picasso.with(MovieDetailsActivity.this).load("http://image.tmdb.org/t/p/w500/"+movie.getPosterPath()).into(poster);
            }

            @Override
            public void onFailure(Call<Movie>call, Throwable t) {
                // Log error here since request failed
                pDialog.hide();
                Log.e("Elokaa", t.toString());
            }
        });
    }
}
