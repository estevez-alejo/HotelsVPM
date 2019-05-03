package com.alejoestevez.hotelsmvp.data.realm.DTO;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserSession extends RealmObject {

    @PrimaryKey
    private String UserId;

    private String Name;
    private String Email;
    private String PhotoUrl;

    private String ProfilePhoto;

    private String Provider;
    private String AuthToken;

    public Date getLastAccess() {
        return LastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        LastAccess = lastAccess;
    }

    private Date LastAccess;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getProvider() {
        return Provider;
    }

    public void setProvider(String provider) {
        Provider = provider;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

}
