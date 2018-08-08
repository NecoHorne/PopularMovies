package com.necohorne.popularmovies.Utils;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.necohorne.popularmovies.Data.MovieDatabase;
import com.necohorne.popularmovies.Model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        movies = movieDatabase.MovieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getMovies(){
        return movies;
    }

}
