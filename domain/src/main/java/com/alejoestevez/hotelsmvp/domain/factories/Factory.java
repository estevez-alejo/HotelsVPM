package com.alejoestevez.hotelsmvp.domain.factories;

import com.alejoestevez.hotelsmvp.domain.model.Opinion;

import java.util.Date;

public class Factory {
    public static final class OpinionFactory {
        public static Opinion Create(int rating,String message,Date creationDate){
            return new Opinion(rating,message,creationDate);
        }
    }
}
