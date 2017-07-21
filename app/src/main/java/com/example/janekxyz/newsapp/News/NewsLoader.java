package com.example.janekxyz.newsapp.News;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.janekxyz.newsapp.Connection.HttpConnection;

import java.util.List;

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
        if(url == null){
            return null;
        }

        return HttpConnection.fetchData(url);

    }
}
