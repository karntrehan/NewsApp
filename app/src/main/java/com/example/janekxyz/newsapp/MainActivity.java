package com.example.janekxyz.newsapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.janekxyz.newsapp.News.News;
import com.example.janekxyz.newsapp.News.NewsAdapter;
import com.example.janekxyz.newsapp.News.NewsLoader;
import com.example.janekxyz.newsapp.Search.SearchActivity;
import com.example.janekxyz.newsapp.Settings.Settings;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    // Loader id for LoaderManager
    private static final int LOADER_ID = 1;

    // API KEY that I have to register to get access to the guardian API
    private static final String API_KEY = "47a30750-4041-4cce-9e99-4e602fdeff81";

    // Default json query that will be used when user won't choose any actions. It's connected with API_KEY
    private static String defaultJsonQuery = "https://content.guardianapis.com/search?api-key="+API_KEY;

    // News adapter for downloading data in another thread
    private NewsAdapter adapter;

    // Text view shows "no news found" when there is no news downloaded from Guardian API
    private TextView empty;

    // title for building query
    private String title = null;

    // category for building query
    private String category = null;

    // date for building query
    private String date = null;

    private String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        empty = (TextView) findViewById(R.id.empty);

        checkInternet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // when activity stops and then returns loader will be reseted
        if(!isConnected()){
            empty.setText(R.string.no_internet);
            adapter.clear();
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.destroyLoader(LOADER_ID);
        }
        getLoaderManager().restartLoader(LOADER_ID,null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                return true;
            case R.id.search:
                Intent search = new Intent(this, SearchActivity.class);
                search.putExtra("query", defaultJsonQuery);
                startActivityForResult(search, 1);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                title = data.getStringExtra("title");
                category = data.getStringExtra("category");
                date = data.getStringExtra("date");
                defaultJsonQuery = data.getStringExtra("query");
            }
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // page size set in menu by user
        String pageSize = sharedPreferences.getString(getString(R.string.settings_page_size_key), getString(R.string.settings_page_size_default));

        Uri baseUri = Uri.parse(defaultJsonQuery);
        Uri.Builder builder = baseUri.buildUpon();

        if(title != null) {
            builder.appendQueryParameter("q", title);
        }

        if(date != null) {
            builder.appendQueryParameter("from-date", date);
        }

        if(category != null) {
            builder.appendQueryParameter("section", category);
        }

        defaultJsonQuery = builder.toString();

        builder.appendQueryParameter("page-size", pageSize);

        return new NewsLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        empty.setText(R.string.no_news);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        adapter.clear();

        if(data != null && !data.isEmpty()){
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }

    private void checkInternet(){


        if(isConnected()){
            ListView listView = (ListView) findViewById(R.id.list);
            listView.setEmptyView(empty);

            adapter = new NewsAdapter(this, new ArrayList<News>());

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    News news = adapter.getItem(position);
                    // get uri from string as url to website
                    Uri newsUri = Uri.parse(news.getUrl());

                    Intent intent = new Intent(Intent.ACTION_VIEW, newsUri);

                    startActivity(intent);
                }
            });

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID,null,this);
        } else {
            // When there in no internet connection progress bar will show
            View loading = findViewById(R.id.progress);
            // and it will gone
            loading.setVisibility(View.GONE);

            // instead of progress bar "no internet connection" will appear
            empty.setText(R.string.no_internet);
        }
    }

    // Checking if internet connection is available
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        else {
            return false;
        }
    }
}