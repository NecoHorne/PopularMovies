package com.necohorne.popularmovies.Utils;

public class Constants {

    public static final String YOUTUBE = "YouTube";

    //object bundle constants
    public static final String MOVIE_BUNDLE = "movie_bundle";

    //Youtube Constants
    public static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    public static final String YOUTUBE_IMAGE_URL = "http://img.youtube.com/vi/";
    public static final String THUMBNAIL_QUERY = "/0.jpg";

    //movie network / URL constants
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    public static final String SORT_BY_POPULARITY = "movie/popular?api_key=";
    public static final String SORT_BY_HIGHEST_RATED = "movie/top_rated?api_key=";
    public static final String SORT_STRING = "&language=en-US&";
    public static final String MOVIE_PATH = "movie/";
    public static final String PAGE_PATH = "page=";
    public static final String VIDEO_PATH = "/videos?api_key=";
    public static final String REVIEW_PATH = "/reviews?api_key=";

    //movie JSON constants
    public static final String RESULTS = "results";
    public static final String POSTER_PATH = "poster_path";
    public static final String ADULT = "adult";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";
    public static final String GENRE_IDS = "genre_ids";
    public static final String ID = "id";
    public static final String IMDB_ID = "imdb_id";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String ORIGINAL_LANGUAGE = "original_language";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String POPULARITY = "popularity";
    public static final String VOTE_COUNT = "vote_count";
    public static final String VIDEO = "video";
    public static final String VOTE_AVERAGE = "vote_average";

    //video / trailer JSON constants
    public static final String VIDEO_LANGUAGE = "iso_639_1";
    public static final String VIDEO_COUNTRY = "iso_3166_1";
    public static final String VIDEO_KEY = "key";
    public static final String VIDEO_NAME = "name";
    public static final String VIDEO_SITE = "site";
    public static final String VIDEO_SIZE = "size";
    public static final String VIDEO_TYPE = "type";

    //review JSON constants
    public static final String REVIEW_AUTHOR = "author";
    public static final String REVIEW_CONTENT = "content";
    public static final String REVIEW_URL = "url";
    public static final String REVIEW_PAGES = "total_pages";

}
