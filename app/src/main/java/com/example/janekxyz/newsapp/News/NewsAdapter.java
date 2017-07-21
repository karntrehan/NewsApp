package com.example.janekxyz.newsapp.News;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.janekxyz.newsapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Janekxyz on 10.06.2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    public NewsAdapter(@NonNull Context context, List<News> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item,parent,false);
        }

        News news = getItem(position);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(news.getTitle());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        imageView.setImageResource(checkImage(news.getSection()));

        TextView section = (TextView) convertView.findViewById(R.id.section);
        section.setText(news.getSection());

        TextView days = (TextView) convertView.findViewById(R.id.day);
        days.setText(changeDateToDays(news.getDate()));

        TextView time = (TextView) convertView.findViewById(R.id.time);
        time.setText(changeDateToTime(news.getDate()));

        return convertView;
    }


    private String changeDateToDays(String date){
        String days = null;

        try {
            Date dateOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date);
            days = new SimpleDateFormat("yyyy-MM-dd").format(dateOut);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "There was a problem while changing date - days");
        }

        return days;
    }

    private String changeDateToTime(String date){
        String time = null;

        try {
            Date dateOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date);
            time = new SimpleDateFormat("H:mm:ss").format(dateOut);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "There was a problem while changing date - time");
        }

        return time;
    }

    private int checkImage(String section){
        switch (section){
            case "UK news":
            case "US news":
                return R.drawable.news;
            case "Sport":
            case "Football":
                return R.drawable.sport;
            case "Business":
                return R.drawable.business;
            case "Global development":
            case "World news":
            case "Travel":
                return R.drawable.global;
            case "Television & radio":
            case "Technology":
                return R.drawable.technology;
            case "Life and style":
                return R.drawable.life;
            case "Music":
                return R.drawable.music;
            case "Politics":
                return R.drawable.politic;
            case "Opinion":
                return R.drawable.opinion;
            case "Books":
                return R.drawable.books;
            default:
                return R.drawable.info;
        }
    }
}
