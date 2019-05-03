package com.alejoestevez.hotelsmvp.data.service;

import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.domain.model.Rating;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IOpinionService {
    @GET("opinions/{id}.json")
    Call<Map<String, Opinion>> getOpinions(@Path(value = "id", encoded = true) int hotelId);

    @PUT("opinions/{id}/{uid}.json")
    Call<Opinion> createOpinion(@Path(value = "id", encoded = true) int hotelId
            , @Path(value = "uid", encoded = true) String uid
            , @Query("auth") String authorization
            , @Body Opinion opinion);

    @PUT("ratings/{id}/{uid}.json")
    Call<Rating> createRating(@Path(value = "id", encoded = true) int hotelId
            , @Path(value = "uid", encoded = true) String uid
            , @Query("auth") String authorization
            , @Body int rating);

}
