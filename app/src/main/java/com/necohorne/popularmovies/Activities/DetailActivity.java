package com.necohorne.popularmovies.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.necohorne.popularmovies.Model.Movie;
import com.necohorne.popularmovies.R;
import com.necohorne.popularmovies.Utils.Constants;
import com.necohorne.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Movie movie = getMovieFromIntent();
        initUI(movie);
    }

    private void initUI(Movie movie) {

        ImageView moviePosterIV = findViewById(R.id.detail_poster_iv);
        Picasso.with(DetailActivity.this)
                .load(NetworkUtils.BASE_IMAGE_URL + movie.getPosterPath())
                .placeholder(R.drawable.ic_popcorn)
                .into(moviePosterIV);

        TextView movieTitleTV = findViewById(R.id.movie_title_tv);
        movieTitleTV.setText(movie.getOriginalTitle());

        TextView movieReleaseTV = findViewById(R.id.release_date_tv);
        movieReleaseTV.setText(movie.getReleaseDate());

        TextView movieVoteAverageTV = findViewById(R.id.vote_average_tv);
        movieVoteAverageTV.setText(String.valueOf(movie.getVoteAverage()));

        TextView movieOverviewTV = findViewById(R.id.over_view_tv);
        movieOverviewTV.setText(movie.getOverview());

        TextView movieLanguageTV = findViewById(R.id.language_tv);
        movieLanguageTV.setText(movie.getOriginalLanguage());

    }

    private Movie getMovieFromIntent(){
        Bundle data = getIntent().getExtras();
        Movie movie = data.getParcelable(Constants.MOVIE_BUNDLE);
        return movie;
    }
}
