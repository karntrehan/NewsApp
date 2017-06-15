package com.example.janekxyz.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.janekxyz.newsapp.News.NewsFragmentAdapter;
import com.example.janekxyz.newsapp.Settings.Settings;

//  implements LoaderManager.LoaderCallbacks<List<News>>
public class MainActivity extends AppCompatActivity{

    public ViewPager viewPager;

    public NewsFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new NewsFragmentAdapter(getApplicationContext(), getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tab = (TabLayout) findViewById(R.id.tab);
        tab.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.settings){
            Intent settings = new Intent(this, Settings.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
