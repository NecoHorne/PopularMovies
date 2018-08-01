package com.necohorne.popularmovies.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.necohorne.popularmovies.Model.Movie;
import com.necohorne.popularmovies.R;
import com.necohorne.popularmovies.Utils.JsonUtils;
import com.necohorne.popularmovies.Utils.MovieRecyclerAdapter;
import com.necohorne.popularmovies.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private boolean sortByPopular;

    //this variable indicates the page number in the url used to pull the data from the website.
    private static int pageNumber = 1;

    private MovieRecyclerAdapter mMovieRecyclerAdapter;
    private URL searchURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new HasConnection().execute();
    }

    private void initRecycler(){
        mRecyclerView = findViewById(R.id.main_activity_rv);
        GridLayoutManager movieLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        mRecyclerView.setLayoutManager(movieLayoutManager);

        //the below checks when the recycler has reached the bottom of the page, increments the url page number and adds the next page's movies to the list
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

    private void sortByDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.dialog_sort_by, null);
        dialogBuilder.setView(view);
        final AlertDialog alertDialog = dialogBuilder.create();

        TextView popularSort = view.findViewById( R.id.dialog_popular);
        TextView ratingSort = view.findViewById( R.id.dialog_rated);

        alertDialog.show();

        popularSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset page number.
                pageNumber  = 1;
                searchURL = NetworkUtils.buildPopularUrl(pageNumber);
                sortByPopular = true;
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
                searchURL = NetworkUtils.buildHighRatedUrl(pageNumber);
                sortByPopular = false;
                //call the FetchMovieJson so the list get cleared and re populated.
                new FetchMovieJson().execute();
                alertDialog.dismiss();
                Toast.makeText(MainActivity.this, "Sorted by Highest Rated", Toast.LENGTH_LONG).show();
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
                searchURL = NetworkUtils.buildPopularUrl(pageNumber);
                sortByPopular = true;
                Toast.makeText(MainActivity.this, "Sorted by Popular", Toast.LENGTH_LONG).show();
                initRecycler();
                new FetchMovieJson().execute();
            }else {
                ImageView noConnection = findViewById(R.id.no_connection_iv);
                noConnection.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "You are not connected to the internet, Please connect and try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class FetchMovieJson extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return NetworkUtils.getResponseFromHttpUrl(searchURL);
            } catch(IOException e) {
                e.printStackTrace();
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
            }
        }
    }

    public class FetchNextPageJson extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {

            if(sortByPopular){
                searchURL = NetworkUtils.buildPopularUrl(pageNumber);
            }else {
                searchURL = NetworkUtils.buildHighRatedUrl(pageNumber);
            }

            try {
                return NetworkUtils.getResponseFromHttpUrl(searchURL);
            } catch(IOException e) {
                e.printStackTrace();
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
