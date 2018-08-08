package com.necohorne.popularmovies.Activities;

import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.necohorne.popularmovies.Data.MovieDatabase;
import com.necohorne.popularmovies.Model.Movie;
import com.necohorne.popularmovies.Model.Review;
import com.necohorne.popularmovies.Model.Video;
import com.necohorne.popularmovies.R;
import com.necohorne.popularmovies.Utils.Constants;
import com.necohorne.popularmovies.Utils.JsonUtils;
import com.necohorne.popularmovies.Utils.NetworkUtils;
import com.necohorne.popularmovies.Utils.RecyclerAdapters.ReviewRecyclerAdapter;
import com.necohorne.popularmovies.Utils.RecyclerAdapters.TrailerRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private static int pageNumber = 1;
    private static int pageTotal = 1;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private ReviewRecyclerAdapter mReviewRecyclerAdapter;
    private Movie mMovie;
    private MovieDatabase mDatabase;
    private MenuItem mFaveIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMovie = getMovieFromIntent();
        if(mMovie != null){
            mDatabase = MovieDatabase.getInstance(getApplicationContext());
            initUI(mMovie);
            new FetchTrailers().execute(mMovie);
            new FetchReviews().execute(mMovie);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.detail_activity_menu, menu );
        //creating a field for the below menu item in order to change the color in the database checks.
        mFaveIcon = menu.findItem(R.id.detail_fav_menu_item);
        new CheckFave().execute();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.detail_fav_menu_item:
                addToFavs();
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    private void addToFavs() {
        new DataBaseOperation().execute();
    }

    private void initUI(Movie movie) {

        ImageView moviePosterIV = findViewById(R.id.detail_poster_iv);
        Picasso.with(DetailActivity.this)
                .load(Constants.BASE_IMAGE_URL + movie.getPosterPath())
                .placeholder(R.drawable.ic_popcorn)
                .into(moviePosterIV);

        setTitle(movie.getOriginalTitle());

        TextView movieReleaseTV = findViewById(R.id.release_date_tv);
        movieReleaseTV.setText(movie.getReleaseDate());

        TextView movieVoteAverageTV = findViewById(R.id.vote_average_tv);
        movieVoteAverageTV.setText(String.valueOf(movie.getVoteAverage()));

        TextView movieOverviewTV = findViewById(R.id.over_view_tv);
        movieOverviewTV.setText(movie.getOverview());

        TextView movieLanguageTV = findViewById(R.id.language_tv);
        movieLanguageTV.setText(movie.getOriginalLanguage());

        initTrailerRecycler();
        initReviewRecycler();
    }

    private void initTrailerRecycler(){
        mTrailerRecyclerView = findViewById(R.id.trailer_recycler_view);
        //horizontal scrollview just looks better for trailer thumbnails so the layout manager calls the horizontal layout for this recycler
        LinearLayoutManager layoutManager= new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(layoutManager);
    }

    private void initReviewRecycler(){
        mReviewRecyclerView = findViewById(R.id.review_recycler_view);
        LinearLayoutManager layoutManager= new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(layoutManager);

        //when scrolled to the bottom of the recycler the below will check the total number of pages of reviews from the JsonUtils.getReviewPageTotal(jsonResult) called in FetchReviews.
        //if the page number is less than the page total it will increment the page total get the next reviews add them to the list and update the adapter.

        mReviewRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)) {
                    if(pageNumber < pageTotal){
                        pageNumber += 1;
                        new FetchNextReviewPageJson().execute(mMovie);
                    }
                }
            }
        });
    }

    private Movie getMovieFromIntent(){
        Bundle data = getIntent().getExtras();
        if(data != null) {
            return data.getParcelable(Constants.MOVIE_BUNDLE);
        }
        return null;
    }

    public class FetchTrailers extends AsyncTask<Movie , Void, String>{

        @Override
        protected String doInBackground(Movie... movies) {
            Movie movie = movies[0];
            try {
                return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getMovieTrailerUrl(String.valueOf(movie.getId())));
            } catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            if(jsonResult != null && !jsonResult.equals("")){
                ArrayList<Video> videos = JsonUtils.getVideoList(jsonResult);
                if(videos.size() > 0){
                    ArrayList<String> youtubeLinks = JsonUtils.returnYoutubeList(videos);
                    TrailerRecyclerAdapter trailerRecyclerAdapter = new TrailerRecyclerAdapter(youtubeLinks, DetailActivity.this);
                    mTrailerRecyclerView.setAdapter(trailerRecyclerAdapter);
                }
            }
        }
    }

    public class FetchReviews extends AsyncTask<Movie, Void, String>{

        @Override
        protected String doInBackground(Movie... movies) {
            Movie movie = movies[0];
            try {
                return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getMovieReviewUrl(String.valueOf(movie.getId()), pageNumber));
            } catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            if(jsonResult != null && !jsonResult.equals("")){
                ArrayList<Review> reviews = JsonUtils.getReviewList(jsonResult);
                pageTotal = JsonUtils.getReviewPageTotal(jsonResult);
                if(reviews.size() > 0){
                    mReviewRecyclerAdapter = new ReviewRecyclerAdapter(reviews, DetailActivity.this);
                    mReviewRecyclerView.setAdapter(mReviewRecyclerAdapter);
                }
            }
        }
    }

    public class FetchNextReviewPageJson extends AsyncTask<Movie, Void, String>{

        @Override
        protected String doInBackground(Movie... movies) {

            Movie movie = movies[0];
            try {
                return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getMovieReviewUrl(String.valueOf(movie.getId()), pageNumber));
            } catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            if(jsonResult != null && !jsonResult.equals("")){
                ArrayList<Review> reviews = JsonUtils.getNextPageReviewList(jsonResult);
                if(reviews != null){
                    if(reviews.size() > 0){
                        mReviewRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    public class DataBaseOperation extends AsyncTask<Void, Void, Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {

            if(mDatabase.MovieDao().searchMovie(mMovie.getId()) != null){
                Movie movieID = mDatabase.MovieDao().searchMovie(mMovie.getId());
                mDatabase.MovieDao().deleteMovie(movieID);
                return 1;
            } else {
                mDatabase.MovieDao().insertMovie(mMovie);
                return 2;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == 1){
                Toast.makeText(DetailActivity.this, "Removed from your favourites list!", Toast.LENGTH_LONG).show();
                mFaveIcon.getIcon().mutate().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
            }else {
                Toast.makeText(DetailActivity.this, "Added to your favourites list!", Toast.LENGTH_LONG).show();
                mFaveIcon.getIcon().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public class CheckFave extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            return mDatabase.MovieDao().searchMovie(mMovie.getId()) != null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(mFaveIcon != null){
                if(aBoolean){
                    mFaveIcon.getIcon().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                }else {
                    mFaveIcon.getIcon().mutate().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }
}
