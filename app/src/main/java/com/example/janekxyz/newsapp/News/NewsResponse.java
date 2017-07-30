package com.example.janekxyz.newsapp.News;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by karn on 30-07-2017.
 */

public class NewsResponse {

    @SerializedName("response")
    private NewsRes newsRes;

    public NewsRes getNewsRes() {
        return newsRes;
    }

    @Override
    public String toString() {
        return "NewsResponse{" +
                "newsRes=" + newsRes +
                '}';
    }

    public static class NewsRes {
        @SerializedName("results")
        private List<News> newsList;

        public List<News> getNewsList() {
            return newsList;
        }
        @Override
        public String toString() {
            return "NewsRes{" +
                    "newsList=" + newsList +
                    '}';
        }
    }
}
