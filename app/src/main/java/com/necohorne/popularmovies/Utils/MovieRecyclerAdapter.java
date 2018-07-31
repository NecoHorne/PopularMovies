package com.necohorne.popularmovies.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.necohorne.popularmovies.Activities.DetailActivity;
import com.necohorne.popularmovies.Model.Movie;
import com.necohorne.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder>{

    private static final String TAG = "MovieRecyclerAdapter";
    private Context mContext;
    private ArrayList<Movie> mMovies;


    public MovieRecyclerAdapter(ArrayList<Movie> movies, Context context) {
        mMovies = movies;
        mContext = context;
    }

    @NonNull
    @Override
    public MovieRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerAdapter.ViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        Picasso.with(mContext)
                .load(NetworkUtils.BASE_IMAGE_URL + movie.getPosterPath())
                .placeholder(R.drawable.ic_popcorn)
                .into(holder.gridPoster);

        holder.gridPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra(Constants.MOVIE_BUNDLE, movie);
                mContext.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView gridPoster;

        public ViewHolder(View itemView) {
            super(itemView);
            gridPoster = itemView.findViewById(R.id.grid_poster_iv);
        }
    }
}
