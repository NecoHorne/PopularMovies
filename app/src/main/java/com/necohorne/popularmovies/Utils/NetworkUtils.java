package com.necohorne.popularmovies.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    //add your api key in the variable below.
    public static final String API_KEY = "ENTER_YOUR_API_KEY_HERE";

    //path constants
    public static final String BASE_URL = "https://api.themoviedb.org";
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    public static final String API_URL = "https://api.themoviedb.org/3/movie/550?api_key=";
    public static final String DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie?api_key=";
    public static final String SORT_BY_POPULARITY = "&language=en-US&sort_by=popularity.desc&page=";
    public static final String SORT_BY_HIGHEST_RATED = "&language=en-US&sort_by=vote_average.desc&page=";


    public static URL buildPopularUrl(int pageNumber) {
        Uri builtUri = Uri.parse(DISCOVER_URL + API_KEY + SORT_BY_POPULARITY + String.valueOf(pageNumber));

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static URL buildHighRatedUrl(int pageNumber) {
        Uri builtUri = Uri.parse(DISCOVER_URL + API_KEY + SORT_BY_HIGHEST_RATED + String.valueOf(pageNumber));

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        Log.v(TAG, "Built URI " + url);
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
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
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
