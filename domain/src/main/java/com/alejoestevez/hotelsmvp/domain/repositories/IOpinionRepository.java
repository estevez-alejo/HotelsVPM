package com.alejoestevez.hotelsmvp.domain.repositories;

import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.domain.model.Rating;

import java.util.Map;

import io.reactivex.Observable;

public interface IOpinionRepository {

    Observable<Map<String, Opinion>> getOpinions(int hotelId);

    Observable<Opinion> createOpinion(int hotelId,Opinion opinion);

    Observable<Rating> createRating(int hotelId, int rating);

}
