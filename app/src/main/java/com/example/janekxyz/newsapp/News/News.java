package com.example.janekxyz.newsapp.News;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Janekxyz on 07.06.2017.
 */

public class News {

    @SerializedName("webTitle")
    private String title;

    @SerializedName("sectionName")
    private String section;

    @SerializedName("webPublicationDate")
    private String date;

    @SerializedName("webUrl")
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

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", section='" + section + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
