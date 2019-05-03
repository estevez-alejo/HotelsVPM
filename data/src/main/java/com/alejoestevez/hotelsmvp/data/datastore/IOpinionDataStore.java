package com.alejoestevez.hotelsmvp.data.datastore;

import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.domain.model.Rating;
import com.alejoestevez.hotelsmvp.domain.model.User;

import java.util.Map;

import io.reactivex.Observable;

public interface IOpinionDataStore extends IDataStore {
    Observable<Map<String, Opinion>> getOpinions(int hotelId);

    Observable<Opinion> createOpinion(User user, int hotelId, Opinion opinion);

    Observable<Rating> createRating(User user, int hotelId, int rating);
}
