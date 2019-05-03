package com.alejoestevez.hotelsmvp.domain.model;

import java.util.Date;

public class Opinion {
    private int rating;
    private String message;
    private Date creationDate;
    private String name;

    public Opinion(int rating, String message, Date creationDate) {
        this.rating=rating;
        this.message=message;
        this.creationDate=creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
