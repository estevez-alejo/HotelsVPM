package com.alejoestevez.hotelsmvp.data.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ISessionService {
    @GET("public_profile/{uid}.json")
    Call<String> getPublicProfile(@Path(value = "uid", encoded = true) String uid);

    @PUT("public_profile/{uid}.json")
    Call<String> createPublicProfile(
            @Path(value = "uid", encoded = true) String uid
            , @Query("auth") String authorization
            , @Body String name);
}
