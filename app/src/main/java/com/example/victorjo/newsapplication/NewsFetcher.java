package com.example.victorjo.newsapplication;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsFetcher {

    private static final String TAG = "NewsFetcher";
    private static final String API_KEY = "dfae292f5cea4b3288e92fc5fba4ab94";
    private static final String FETCH_NEWS_HEADLINES = "https://newsapi.org/v2/top-headlines?";
    private static final String FETCH_NEWS_EVERYTHING = "https://newsapi.org/v2/everything?";
    private static final String SEARCH_QUERY ="q";
    private static final Uri END_POINT = Uri
            .parse(FETCH_NEWS_HEADLINES)
            .buildUpon()
            .appendQueryParameter("country", "ng")
            .appendQueryParameter("category", "general")
            .appendQueryParameter("apiKey", API_KEY)
            .build();

    public byte[] getUrlBytes(String url_spec) throws IOException {

        URL url = new URL(url_spec);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            InputStream inputStream = httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage() + "with " + url_spec);
            }
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byteArrayOutputStream.close();

            return byteArrayOutputStream.toByteArray();
        }finally {
            httpURLConnection.disconnect();
        }
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }



    private String buildUrl(String method, String query){
        Uri.Builder uriBuilder = END_POINT.buildUpon();

        if (method.equals(SEARCH_QUERY)) {
            uriBuilder.appendQueryParameter("q", query);
        }
        return uriBuilder.build().toString();
    }

    private List<FeedItem> downloadNewsItems(String url){
        List<FeedItem> items = new ArrayList<>();
        try{

            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " +  jsonString);
            parseItems(items, jsonString);
        }catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items: " + ioe);
        }
        return items;
    }

    private void parseItems(List<FeedItem> items, String jsonString){

        Gson gson = new Gson();
        FeedItem feedItem = gson.fromJson(jsonString, FeedItem.class);

        items.addAll(feedItem.getFeeds());
    }

    public List<FeedItem> fetchRecentNews(String query) {
        String url = buildUrl(FETCH_NEWS_HEADLINES, query);
        return downloadNewsItems(url);
    }

    public List<FeedItem> fetchEverythingNews(String query) {
        String url = buildUrl(FETCH_NEWS_EVERYTHING, query);
        return downloadNewsItems(url);
    }


    public List<FeedItem>searchNews(String query) {
        String url = buildUrl(SEARCH_QUERY, query);
        return downloadNewsItems(url);
    }






}
