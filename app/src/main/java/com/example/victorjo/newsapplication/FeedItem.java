package com.example.victorjo.newsapplication;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class FeedItem {
    private UUID id;
    private boolean bookmarked;
    @SerializedName("source") private Source source;
    @SerializedName("title") private String title;
    @SerializedName("author") private String author;
    @SerializedName("description") private String description;
    @SerializedName("url") private String url;
    @SerializedName("urlToImage") private String urlToImage;

    @SerializedName("articles") private List<FeedItem> articles;

    FeedItem(){
        this(UUID.randomUUID());
    }

    private FeedItem(final UUID uuid){
        this.id = uuid;
    }


    private Source getSource() {
        return source;
    }

    public String getName(){
        return getSource().getName();
    }

    public void setName(String name) {
        new Source().setName(name);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public Uri getNewsUri(){
        return Uri.parse(getUrl()).buildUpon().build();
    }

    @Override
    public String toString() {
        return "FeedItem{" +
                "Source" + source +
                "name='" + getName() + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }

    public List<FeedItem> getFeeds(){
        return articles;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }


    private class Source{

        @SerializedName("name") private String name;

        private String getName() {
            return name;
        }

        private void setName(String name) {
            this.name = name;
        }
    }
}
