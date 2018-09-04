package com.nichobox.movieapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.nichobox.movieapp.adapter.MovieAdapter;
import com.nichobox.movieapp.model.Movie;
import com.nichobox.movieapp.model.MoviesResponse;
import com.nichobox.movieapp.rest.ApiClient;
import com.nichobox.movieapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final static String API_KEY = "";
    private ArrayList<Movie> movieList;
    private ProgressDialog pDialog;
    private MovieAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        movieList = new ArrayList<>();
        mAdapter = new MovieAdapter(getApplicationContext(), movieList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new MovieAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new MovieAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movies.get(position);
                int id  = movie.getId();
                Log.d("Nicholas", String.valueOf(id));
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(id));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        getMovies("popular");


    }

    public void getMovies(String sortBy){
        pDialog.setMessage("Loading movies");
        pDialog.show();



        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getPopularMovies(sortBy, API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
                pDialog.hide();
                movies = response.body().getResults();
                Log.d("Elokaa", "Number of movies received: " + movies.size());
                recyclerView.setAdapter(new MovieAdapter(getApplicationContext(),movies));
            }

            @Override
            public void onFailure(Call<MoviesResponse>call, Throwable t) {
                // Log error here since request failed
                pDialog.hide();
                Log.e("Elokaa", t.toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            getMovies("popular");
        }else{
            getMovies("top_rated");
        }

        return super.onOptionsItemSelected(item);
    }
}
