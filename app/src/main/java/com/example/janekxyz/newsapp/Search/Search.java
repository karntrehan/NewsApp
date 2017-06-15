package com.example.janekxyz.newsapp.Search;

/**
 * Created by Janekxyz on 15.06.2017.
 */

public class Search {
    private String title;
    private String category;
    private String date;

    public Search(String title, String category, String date) {
        this.title = title;
        this.category = category;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

}
