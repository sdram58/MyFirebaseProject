package com.catata.myfirebaseproject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Libro implements Serializable {
    String author;
    String description;
    int id;
    String publication_date;
    String title;
    String url_image;

    public Libro() {
    }

    public Libro(String author, String description, int id, String publication_date, String title, String url_image) {
        this.author = author;
        this.description = description;
        this.id = id;
        this.publication_date = publication_date;
        this.title = title;
        this.url_image = url_image;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(String publication_date) {
        this.publication_date = publication_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("description", description);
        result.put("author", author);
        result.put("title", title);
        result.put("publication_date", publication_date);
        result.put("url_image", url_image);
        result.put("id", id);

        return result;
    }

    public void update(Libro l){
        if(this.id == l.id){
            this.title = l.title;
            this.description = l.description;
            this.publication_date = l.publication_date;
            this.url_image = l.url_image;
            this.author = l.author;
        }
    }
}
