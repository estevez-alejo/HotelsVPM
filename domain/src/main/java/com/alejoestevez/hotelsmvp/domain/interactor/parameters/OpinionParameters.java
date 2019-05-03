package com.alejoestevez.hotelsmvp.domain.interactor.parameters;

import com.alejoestevez.hotelsmvp.domain.model.Opinion;

public class OpinionParameters {
    public static final class Parameters {

        private int hotelId;
        private Opinion opinion;

        public int getHotelId() {
            return hotelId;
        }

        public Opinion getOpinion() {
            return opinion;
        }

        private Parameters(int hotelId, Opinion opinion) {
            this.hotelId = hotelId;
            this.opinion = opinion;
        }

        public static OpinionParameters.Parameters Create(int hotelId, Opinion opinion) {
            return new OpinionParameters.Parameters(hotelId, opinion);
        }
    }
}
