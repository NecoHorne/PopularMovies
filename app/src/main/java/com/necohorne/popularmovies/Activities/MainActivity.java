package com.necohorne.popularmovies.Activities;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.necohorne.popularmovies.Data.MovieDatabase;
import com.necohorne.popularmovies.Utils.MainViewModel;
import com.necohorne.popularmovies.Model.Movie;
import com.necohorne.popularmovies.R;
import com.necohorne.popularmovies.Utils.JsonUtils;
import com.necohorne.popularmovies.Utils.NetworkUtils;
import com.necohorne.popularmovies.Utils.RecyclerAdapters.MovieRecyclerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int sortByInt = 0;
    private static int pageNumber = 1;
    //this variable indicates the page number in the url used to pull the data from the website.
    private static final String SORTED_BY = "sorting";

    private RecyclerView mRecyclerView;
    private MovieRecyclerAdapter mMovieRecyclerAdapter;
    private MovieDatabase mDatabase;
    private ArrayList<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(SORTED_BY)){
                sortByInt = savedInstanceState.getInt(SORTED_BY);
            } else {
                sortByInt = 1;
            }
        }else {
            sortByInt = 1;
        }

        initRecycler();

        new HasConnection().execute();
        mDatabase = MovieDatabase.getInstance(getApplicationContext());
        engageViewModel();
    }

    private void initRecycler(){
        mRecyclerView = findViewById(R.id.main_activity_rv);
        GridLayoutManager movieLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        mRecyclerView.setLayoutManager(movieLayoutManager);

        //the below checks when the recycler has reached the bottom of the page, increments the url page number and adds the next page's movies to the list
        if(sortByInt == 1 || sortByInt == 2){
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if(!recyclerView.canScrollVertically(1)) {
                        pageNumber += 1;
                        new FetchNextPageJson().execute();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main_activity_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_sort:
                sortByDialog();
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SORTED_BY, sortByInt);
    }

    private void sortByDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.dialog_sort_by, null);
        dialogBuilder.setView(view);
        final AlertDialog alertDialog = dialogBuilder.create();

        TextView popularSort = view.findViewById( R.id.dialog_popular);
        TextView ratingSort = view.findViewById( R.id.dialog_rated);
        TextView favouriteSort = view.findViewById(R.id.dialog_fav);

        alertDialog.show();

        popularSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset page number.
                pageNumber  = 1;
                sortByInt = 1;
                //call the FetchMovieJson so the list get cleared and re populated.
                new FetchMovieJson().execute();
                alertDialog.dismiss();
                Toast.makeText(MainActivity.this, "Sorted by Popular", Toast.LENGTH_LONG).show();
            }
        });

        ratingSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset page number.
                pageNumber  = 1;
                sortByInt = 2;
                //call the FetchMovieJson so the list get cleared and re populated.
                new FetchMovieJson().execute();
                alertDialog.dismiss();
                Toast.makeText(MainActivity.this, "Sorted by Highest Rated", Toast.LENGTH_LONG).show();
            }
        });

        favouriteSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByInt = 3;
                new FetchMovieJson().execute();
                alertDialog.dismiss();
                Toast.makeText(MainActivity.this, "Sorted by Favourite", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void engageViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovies = (ArrayList<Movie>) movies;
                if(sortByInt == 3){
                    if(mMovies != null){
                        mMovieRecyclerAdapter = new MovieRecyclerAdapter(mMovies, MainActivity.this);
                        mRecyclerView.setAdapter(mMovieRecyclerAdapter);
                    }
                }
            }
        });
    }

    public class HasConnection extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            return NetworkUtils.hasInternetAccess(getApplicationContext());
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                new FetchMovieJson().execute();
            }else {
                ImageView noConnection = findViewById(R.id.no_connection_iv);
                noConnection.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "You are not connected to the internet, Please connect and try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class FetchMovieJson extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {

            //here I use a switch do determine how the user wants to sort. the sortByInt is set in the sortByDialog method in the click listener.
            switch(sortByInt){
                case 1:
                    try {
                        return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildPopularUrl(pageNumber));
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildHighRatedUrl(pageNumber));
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            if(jsonResult != null && !jsonResult.equals("")){
                ArrayList<Movie> movies = JsonUtils.getMovieList(jsonResult);
                if(movies.size() > 0){
                    mMovieRecyclerAdapter = new MovieRecyclerAdapter(movies, MainActivity.this);
                    mRecyclerView.setAdapter(mMovieRecyclerAdapter);
                }
                //here we use an else if instead of an else because it might be that we are querying the server and got a null so we make sure that we are in fact checking the database.
            } else if(sortByInt == 3) {
                    engageViewModel();
            }
        }
    }

    public class FetchNextPageJson extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {

            switch(sortByInt){
                case 1:
                    try {
                        return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildPopularUrl(pageNumber));
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildHighRatedUrl(pageNumber));
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResult) {

            if(jsonResult != null && !jsonResult.equals("")){
                ArrayList<Movie> movies = JsonUtils.getNextPageList(jsonResult);
                if(movies.size() > 0){
                    mMovieRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
