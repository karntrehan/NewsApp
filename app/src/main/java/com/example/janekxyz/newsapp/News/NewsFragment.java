package com.example.janekxyz.newsapp.News;


import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.janekxyz.newsapp.R;
import com.example.janekxyz.newsapp.Search.Search;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {
    // Search object with search values
    Search search = null;

    // Loader id for LoaderManager
    private static final int LOADER_ID = 1;

    // API KEY that I have to register to get access to the guardian API
    private static final String API_KEY = "47a30750-4041-4cce-9e99-4e602fdeff81";

    // Default json query that will be used when user won't choose any actions. It's connected with API_KEY
    private static final String DEFAULT_JSON_QUERY = "https://content.guardianapis.com/search?api-key="+API_KEY;

    // Adapter
    private NewsAdapter adapter;

    private TextView empty;


    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news_fragment, container, false);

        empty = (TextView) view.findViewById(R.id.empty);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        System.out.println("Network info: "+networkInfo);

        if(networkInfo != null && networkInfo.isConnected()){
            ListView listView = (ListView) view.findViewById(R.id.list);
            listView.setEmptyView(empty);

            adapter = new NewsAdapter(getActivity(), new ArrayList<News>());

            listView.setAdapter(adapter);

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID,null,this);
        } else {
            View loading = view.findViewById(R.id.progress);
            loading.setVisibility(View.GONE);

            empty.setText(R.string.no_internet);
        }

        return view;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // page size set in menu by user
        String pageSize = sharedPreferences.getString(getString(R.string.settings_page_size_key), getString(R.string.settings_page_size_default));

        if(search != null) {
            Uri baseUri = Uri.parse(DEFAULT_JSON_QUERY);
            Uri.Builder builder = baseUri.buildUpon();

            if(search.getTitle() != null) {
                builder.appendQueryParameter("q", search.getTitle());
            }

            if(search.getDate() != null) {
                builder.appendQueryParameter("from-date", search.getDate());
            }

            if(search.getCategory() != null) {
                builder.appendQueryParameter("section", search.getCategory());
            }

            builder.appendQueryParameter("page-size", pageSize);

            System.out.println("REQUEST: " + builder.toString());
            return new NewsLoader(getActivity(), builder.toString());
        } else {
            return new NewsLoader(getActivity(), DEFAULT_JSON_QUERY);
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<News>> loader, List<News> data) {
        empty.setText(R.string.no_news);

        ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        adapter.clear();

        if(data != null && !data.isEmpty()){
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<News>> loader) {
        adapter.clear();
    }

    public void setObject(Search search){
        this.search = search;
    }
}
