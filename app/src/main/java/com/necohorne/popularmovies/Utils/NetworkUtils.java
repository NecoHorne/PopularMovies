package com.necohorne.popularmovies.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.necohorne.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static com.necohorne.popularmovies.Utils.Constants.BASE_URL;
import static com.necohorne.popularmovies.Utils.Constants.MOVIE_PATH;
import static com.necohorne.popularmovies.Utils.Constants.PAGE_PATH;
import static com.necohorne.popularmovies.Utils.Constants.REVIEW_PATH;
import static com.necohorne.popularmovies.Utils.Constants.SORT_BY_HIGHEST_RATED;
import static com.necohorne.popularmovies.Utils.Constants.SORT_BY_POPULARITY;
import static com.necohorne.popularmovies.Utils.Constants.SORT_STRING;
import static com.necohorne.popularmovies.Utils.Constants.VIDEO_PATH;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String API_KEY = BuildConfig.API_KEY;

    public static URL buildPopularUrl(int pageNumber) {
        Uri builtUri = Uri.parse(BASE_URL + SORT_BY_POPULARITY + API_KEY + SORT_STRING + PAGE_PATH + String.valueOf(pageNumber));

        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.d(TAG, url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildHighRatedUrl(int pageNumber) {
        Uri builtUri = Uri.parse(BASE_URL + SORT_BY_HIGHEST_RATED + API_KEY + SORT_STRING + PAGE_PATH + String.valueOf(pageNumber));

        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.d(TAG, url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getMovieTrailer (String movieID){
        Uri builtUri = Uri.parse(BASE_URL + MOVIE_PATH + movieID + VIDEO_PATH + API_KEY + SORT_STRING);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.d(TAG, url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getMovieReviews (String movieID, String pageNum){

        Uri builtUri = Uri.parse(BASE_URL + MOVIE_PATH + movieID + REVIEW_PATH + API_KEY + SORT_STRING + PAGE_PATH + pageNum);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.d(TAG, url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if(connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null;
    }

    public static boolean hasInternetAccess(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(TAG, "No network available!");
        }
        return false;
    }

}
