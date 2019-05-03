package com.alejoestevez.hotelsmvp.data.service;

import com.alejoestevez.hotelsmvp.domain.model.Hotel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IHotelService {
    @GET("hotels.json")
    Call<List<Hotel>> getHotels();

    @GET("ratings/{id}.json")
    Call<Map<String, Integer>> getRating(@Path(value = "id", encoded = true) int hotelId);
}
