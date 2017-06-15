package com.example.janekxyz.newsapp.News;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.janekxyz.newsapp.R;
import com.example.janekxyz.newsapp.Search.SearchFragment;

/**
 * Created by Janekxyz on 14.06.2017.
 */

public class NewsFragmentAdapter extends FragmentPagerAdapter{
    private Context context;

    public NewsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new NewsFragment();
            case 1:
                return new SearchFragment();
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
}
