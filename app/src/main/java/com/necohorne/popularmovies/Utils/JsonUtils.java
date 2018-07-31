package com.necohorne.popularmovies.Utils;

import com.necohorne.popularmovies.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static ArrayList<Movie> movieList;

    //movie JSON constants
    private static final String RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String ADULT = "adult";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String GENRE_IDS = "genre_ids";
    private static final String ID = "id";
    private static final String IMDB_ID = "imdb_id";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String ORIGINAL_LANGUAGE = "original_language";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String POPULARITY = "popularity";
    private static final String VOTE_COUNT = "vote_count";
    private static final String VIDEO = "video";
    private static final String VOTE_AVERAGE = "vote_average";

    public static ArrayList<Movie> getMovieList(String json){

        //create a movie list, check if its clear when function starts as this will be called in the first load.
        movieList = new ArrayList<>();
        if(movieList.size() != 0){
            movieList.clear();
        }

        //get the results from the jsonArray, create ne movie objects for the results and add to movies array list.
        try {
            JSONObject movieJSON = new JSONObject(json);
            JSONArray jsonArray = movieJSON.optJSONArray(RESULTS);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject movie = jsonArray.optJSONObject(i);
                Movie newMovie = getMovie(movie);
                movieList.add(newMovie);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return movieList;
    }

    public static ArrayList<Movie> getNextPageList(String json){

        //no need to check if list is clear this data will be added to the first list as the user scrolls.

        try {
            JSONObject movieJSON = new JSONObject(json);
            JSONArray jsonArray = movieJSON.optJSONArray(RESULTS);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject movie = jsonArray.optJSONObject(i);
                Movie newMovie = getMovie(movie);
                movieList.add(newMovie);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return movieList;
    }

    private static Movie getMovie(JSONObject movie) {

        Movie newMovie = new Movie();

        String title = movie.optString(ORIGINAL_TITLE);
        newMovie.setOriginalTitle(title);

        String posterPath = movie.optString(POSTER_PATH);
        newMovie.setPosterPath(posterPath);

        String overview = movie.optString(OVERVIEW);
        newMovie.setOverview(overview);

        String releaseDate = movie.optString(RELEASE_DATE);
        newMovie.setReleaseDate(releaseDate);

        String imdbId = movie.optString(IMDB_ID);
        newMovie.setImdbId(imdbId);

        String originalLanguage = movie.optString(ORIGINAL_LANGUAGE);
        newMovie.setOriginalLanguage(originalLanguage);

        String backdropPath = movie.optString(BACKDROP_PATH);
        newMovie.setBackdropPath(backdropPath);

        boolean adult = movie.optBoolean(ADULT);
        newMovie.setAdult(adult);

        boolean video = movie.optBoolean(VIDEO);
        newMovie.setVideo(video);

        int id = movie.optInt(ID);
        newMovie.setId(id);

        int voteCount = movie.optInt(VOTE_COUNT);
        newMovie.setVoteCount(voteCount);

        long popularity = movie.optLong(POPULARITY);
        newMovie.setPopularity(popularity);

        long voteAverage = movie.optLong(VOTE_AVERAGE);
        newMovie.setVoteAverage(voteAverage);

        JSONArray idList = movie.optJSONArray(GENRE_IDS);
        if(idList != null){
            int[] genreID = new int[idList.length()];
            for(int i = 0; i < idList.length(); i++){
                genreID[i] = idList.optInt(i);
            }
            newMovie.setGenreIds(genreID);
        }

        return newMovie;
    }
}
