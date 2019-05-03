package com.alejoestevez.hotelsmvp.data.datastore;

import com.alejoestevez.hotelsmvp.domain.model.User;

import io.reactivex.Observable;

//Interfaz del almacen de datos del diccionario.
public interface ISessionDataStore extends IDataStore {
    boolean saveSession(User user);

    User getSession();

    Observable<String> getPublicProfile(String uid);

    Observable<User> savePublicProfile(User user);
}

