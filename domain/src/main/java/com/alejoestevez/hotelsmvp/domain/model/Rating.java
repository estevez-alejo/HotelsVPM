package com.alejoestevez.hotelsmvp.domain.model;

public class Rating {
    private String userId;
    private int rate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


}
