package com.alejoestevez.hotelsmvp.domain.interactor.parameters;

public class HotelParameters {
    public static final class Parameters {

        private int hotelId;

        public int getHotelId() {
            return hotelId;
        }

        private Parameters(int hotelId) {
            this.hotelId = hotelId;
        }

        public static HotelParameters.Parameters Create(int hotelId) {
            return new HotelParameters.Parameters(hotelId);
        }
    }
}