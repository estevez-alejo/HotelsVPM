package com.alejoestevez.hotelsmvp.data.datastore;

import com.alejoestevez.hotelsmvp.domain.model.Hotel;

import java.util.List;

import io.reactivex.Observable;

public interface IHotelDataStore extends IDataStore {
    Observable<List<Hotel>> getHotels();

    Observable<Double> getRating(int hotelId);
}
