package com.necohorne.popularmovies.Utils;

import com.necohorne.popularmovies.Model.Movie;
import com.necohorne.popularmovies.Model.Review;
import com.necohorne.popularmovies.Model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.necohorne.popularmovies.Utils.Constants.ADULT;
import static com.necohorne.popularmovies.Utils.Constants.BACKDROP_PATH;
import static com.necohorne.popularmovies.Utils.Constants.GENRE_IDS;
import static com.necohorne.popularmovies.Utils.Constants.ID;
import static com.necohorne.popularmovies.Utils.Constants.IMDB_ID;
import static com.necohorne.popularmovies.Utils.Constants.ORIGINAL_LANGUAGE;
import static com.necohorne.popularmovies.Utils.Constants.ORIGINAL_TITLE;
import static com.necohorne.popularmovies.Utils.Constants.OVERVIEW;
import static com.necohorne.popularmovies.Utils.Constants.POPULARITY;
import static com.necohorne.popularmovies.Utils.Constants.POSTER_PATH;
import static com.necohorne.popularmovies.Utils.Constants.RELEASE_DATE;
import static com.necohorne.popularmovies.Utils.Constants.RESULTS;
import static com.necohorne.popularmovies.Utils.Constants.REVIEW_AUTHOR;
import static com.necohorne.popularmovies.Utils.Constants.REVIEW_CONTENT;
import static com.necohorne.popularmovies.Utils.Constants.REVIEW_PAGES;
import static com.necohorne.popularmovies.Utils.Constants.REVIEW_URL;
import static com.necohorne.popularmovies.Utils.Constants.VIDEO;
import static com.necohorne.popularmovies.Utils.Constants.VIDEO_COUNTRY;
import static com.necohorne.popularmovies.Utils.Constants.VIDEO_KEY;
import static com.necohorne.popularmovies.Utils.Constants.VIDEO_LANGUAGE;
import static com.necohorne.popularmovies.Utils.Constants.VIDEO_NAME;
import static com.necohorne.popularmovies.Utils.Constants.VIDEO_SITE;
import static com.necohorne.popularmovies.Utils.Constants.VIDEO_SIZE;
import static com.necohorne.popularmovies.Utils.Constants.VIDEO_TYPE;
import static com.necohorne.popularmovies.Utils.Constants.VOTE_AVERAGE;
import static com.necohorne.popularmovies.Utils.Constants.VOTE_COUNT;
import static com.necohorne.popularmovies.Utils.Constants.YOUTUBE;
import static com.necohorne.popularmovies.Utils.Constants.YOUTUBE_URL;

public class JsonUtils {

    private static ArrayList<Movie> movieList;
    private static ArrayList<Review> reviewList;

    public static ArrayList<Movie> getMovieList(String json){

        //create a movie list, check if its clear when function starts as this will be called in the first load.
        movieList = new ArrayList<>();
        if(movieList.size() != 0){
            movieList.clear();
        }

        //get the results from the jsonArray, create new movie objects for the results and add to movies array list.
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

    public static ArrayList<Video> getVideoList(String json) {

        ArrayList<Video> videoList = new ArrayList<>();
        if(videoList.size() != 0){
            videoList.clear();
    }

        try {
        JSONObject videoJSON = new JSONObject(json);
        JSONArray jsonArray = videoJSON.optJSONArray(RESULTS);
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject trailer = jsonArray.optJSONObject(i);
            Video newVideo = getVideo(trailer);
            videoList.add(newVideo);
        }
    } catch(JSONException e) {
        e.printStackTrace();
    }
        return videoList;
}

    public static ArrayList<Review> getReviewList(String json) {

        reviewList = new ArrayList<>();
        if(reviewList.size() != 0){
            reviewList.clear();
        }

        try {
            JSONObject reviewJSON = new JSONObject(json);
            JSONArray jsonArray = reviewJSON.optJSONArray(RESULTS);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject review = jsonArray.optJSONObject(i);
                Review newReview = getReview(review);
                reviewList.add(newReview);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return reviewList;
    }

    public static ArrayList<Review> getNextPageReviewList(String json){

        try {
            JSONObject reviewJSON = new JSONObject(json);
            JSONArray jsonArray = reviewJSON.optJSONArray(RESULTS);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject review = jsonArray.optJSONObject(i);
                Review newReview = getReview(review);
                reviewList.add(newReview);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return reviewList;
    }

    public static ArrayList<String> returnYoutubeList(ArrayList<Video> videoList){

        ArrayList<String> youtubeLinks = new ArrayList<>();
        for(Video video: videoList){
            if(video.getSite().equalsIgnoreCase(YOUTUBE)){
                String url = YOUTUBE_URL + video.getKey();
                youtubeLinks.add(url);
            }
        }
        return youtubeLinks;
    }

    public static int getReviewPageTotal(String json){

        try {
            JSONObject reviewJSON = new JSONObject(json);
            return reviewJSON.optInt(REVIEW_PAGES);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return 0;
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

    private static Video getVideo(JSONObject trailer) {

        Video video = new Video();

        String id = trailer.optString(ID);
        video.setId(id);

        String language = trailer.optString(VIDEO_LANGUAGE);
        video.setLanguage(language);

        String country = trailer.optString(VIDEO_COUNTRY);
        video.setCountry(country);

        String key = trailer.optString(VIDEO_KEY);
        video.setKey(key);

        String name = trailer.optString(VIDEO_NAME);
        video.setName(name);

        String site = trailer.optString(VIDEO_SITE);
        video.setSite(site);

        int size = trailer.optInt(VIDEO_SIZE);
        video.setSize(size);

        String type = trailer.optString(VIDEO_TYPE);
        video.setType(type);

        return video;
    }

    private static Review getReview(JSONObject review) {

        Review newReview = new Review();

        String author = review.optString(REVIEW_AUTHOR);
        newReview.setAuthor(author);

        String id = review.optString(ID);
        newReview.setId(id);

        String content = review.optString(REVIEW_CONTENT);
        newReview.setContent(content);

        String url = review.optString(REVIEW_URL);
        newReview.setUrl(url);

        return newReview;
    }
}
