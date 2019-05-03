package com.alejoestevez.hotelsmvp.domain.repositories;

import com.alejoestevez.hotelsmvp.domain.model.Hotel;

import java.util.List;

import io.reactivex.Observable;

public interface IHotelRepository {
    Observable<List<Hotel>> getHotels();

    Observable<Double> getRating(int hotelId);
}
