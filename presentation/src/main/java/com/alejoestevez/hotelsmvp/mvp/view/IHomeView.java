package com.alejoestevez.hotelsmvp.mvp.view;

import com.alejoestevez.hotelsmvp.domain.model.Hotel;

import java.util.List;

//Interfaz de comunicación de la activity principal
public interface IHomeView extends ISessionView {
    //Mostrar hoteles por pantalla
    void renderData(List<Hotel> value);

    //Mostrar puntuación de cada hotel.
    void renderRating(Double value, int hotelId);
}

