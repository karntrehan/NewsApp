package com.example.janekxyz.newsapp.Connection;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.janekxyz.newsapp.News.News;
import com.example.janekxyz.newsapp.News.NewsResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.string.ok;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Janekxyz on 07.06.2017.
 */

public class HttpConnection {

    // Class name for log
    private static final String LOG_TAG = HttpConnection.class.getSimpleName();

    private static OkHttpClient okHttpClient;
    private static Gson gson;

    //Create a static okHttpClient instance
    public static OkHttpClient getClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }

    //Create a static gson instance
    public static Gson getGson() {
        if (gson == null)
            gson = new Gson();
        return gson;
    }

    /**
     * Change String path to URL object
     *
     * @param path to website url
     * @return URL object
     */
    private static URL makeURL(String path) {
        URL url = null;

        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem with making URL from path");
        }

        return url;
    }

    /**
     * Connect to server and retrieve json response from URL given as argument.
     *
     * @param url of website
     * @return json response
     * @throws IOException
     */
    private static String httpConnection(URL url) throws IOException {
        String json = "";

        if (url == null) {
            return json;
        }

        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                json = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return json;
    }


    /**
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }

        return stringBuilder.toString();
    }

    private static List<News> extractJsonData(String json) {

        List<News> listNews = new ArrayList<>();

        try {
            // root json object that allow to dig in json tree
            JSONObject root = new JSONObject(json);

            // child of root object that contain all data about response like number of pages
            // total results for this request and array of results
            JSONObject response = root.getJSONObject("response");

            // array json object with total number of json objects (news)
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                // objects inside results array
                JSONObject eachObject = results.getJSONObject(i);

                // title of article
                String title = eachObject.getString("webTitle");

                // section to which article belongs
                String section = eachObject.getString("sectionName");

                // date that article was published
                String date = eachObject.getString("webPublicationDate");

                // url of the article
                String url = eachObject.getString("webUrl");

                News news = new News(title, section, date, url);

                listNews.add(news);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with get data from Json");
        }

        return listNews;
    }


    public static List<News> fetchData(String urlString) {
        URL url = makeURL(urlString);

        String json = null;

        try {
            json = httpConnection(url);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractJsonData(json);
    }
}
