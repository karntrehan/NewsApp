package com.example.janekxyz.newsapp.News;

/**
 * Created by Janekxyz on 07.06.2017.
 */

public class News {

    private String title;

    private String section;

    private String date;

    private String url;

    public News(String title, String section, String date, String url) {
        this.title = title;
        this.section = section;
        this.date = date;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
