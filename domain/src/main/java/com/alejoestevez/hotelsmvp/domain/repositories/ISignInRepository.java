package com.alejoestevez.hotelsmvp.domain.repositories;

import com.alejoestevez.hotelsmvp.domain.model.User;

import io.reactivex.Observable;

public interface ISignInRepository {

    Observable<User> isSessionOpen();

    Observable<User> signIn(String provider, String accountIdToken);

    Observable<User> logInWithEmail(String email, String password);

    Observable<User> signUpAccountWithEmail(String email, String password);

    Observable signOut();

}
