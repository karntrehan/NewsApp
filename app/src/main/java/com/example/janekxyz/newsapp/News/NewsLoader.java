package com.example.janekxyz.newsapp.News;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.janekxyz.newsapp.Connection.HttpConnection;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Janekxyz on 10.06.2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (url == null) {
            return null;
        }

        //We will start changing the HttpConnection.fetchData(url) with our
        //OKHttp connection
        final Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = HttpConnection.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                NewsResponse newsResponse = HttpConnection.getGson().fromJson(
                        response.body().string(), NewsResponse.class);
                return newsResponse.getNewsRes().getNewsList();
            } else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
