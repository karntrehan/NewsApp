package com.example.janekxyz.newsapp.News;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.janekxyz.newsapp.R;
import com.example.janekxyz.newsapp.Search.SearchFragment;

import java.io.Serializable;

/**
 * Created by Janekxyz on 14.06.2017.
 */

public class NewsFragmentAdapter extends FragmentPagerAdapter{
    private Context context;
    public Fragment newsFragment;
    public Fragment searchFragment;
    private FragmentManager fragmentManager;

    NewsFragmentListener listener = new NewsFragmentListener();

    Bundle bundle = new Bundle();

    public NewsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        fragmentManager = fm;
        bundle.putSerializable("listener",listener);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if(newsFragment == null){
                    newsFragment = new NewsFragment();
                    newsFragment.setArguments(bundle);
                }
                return newsFragment;
            case 1:
                if(searchFragment == null){
                    searchFragment = new SearchFragment();
                    searchFragment.setArguments(bundle);
                }
                return searchFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.news);
            case 1:
                return context.getResources().getString(R.string.search);
            default:
                return null;
        }
    }

    private class NewsFragmentListener implements Serializable, NewsPageFragmentListener{

        @Override
        public void onSwitchToNews() {
            fragmentManager.beginTransaction().remove(newsFragment).commit();
            if(newsFragment instanceof NewsFragment){
                newsFragment = new NewsFragment();
                newsFragment.setArguments(bundle);
            } else {
                newsFragment = new SearchFragment();
                newsFragment.setArguments(bundle);
            }

            notifyDataSetChanged();
        }
    }
}
