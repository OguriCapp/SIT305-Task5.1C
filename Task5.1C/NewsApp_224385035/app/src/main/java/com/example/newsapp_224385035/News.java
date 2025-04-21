package com.example.newsapp_224385035;

import java.io.Serializable;

public class News implements Serializable {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String content;
    private String publishDate;
    private String url;

    public News(String id, String title, String description, String imageUrl, String content, String publishDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.content = content;
        this.publishDate = publishDate;
        this.url = "";
    }

    public News(String id, String title, String description, String imageUrl, String content, String publishDate, String url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.content = content;
        this.publishDate = publishDate;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }

    public String getPublishDate() {
        return publishDate;
    }
    
    public String getUrl() {
        return url;
    }
} 
