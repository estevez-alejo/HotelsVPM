package com.alejoestevez.hotelsmvp.domain.repositories;

import com.alejoestevez.hotelsmvp.domain.model.User;

import io.reactivex.Observable;

public interface IUserProfileRepository {
    Observable<User> save(User user);

    Observable<String> uploadFile(byte[] data);

    Observable<String> getPublicProfile(String uid);
}
