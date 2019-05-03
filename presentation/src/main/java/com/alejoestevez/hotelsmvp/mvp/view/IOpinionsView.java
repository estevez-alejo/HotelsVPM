package com.alejoestevez.hotelsmvp.mvp.view;

import com.alejoestevez.hotelsmvp.domain.model.Opinion;

import java.util.Map;

//Interfaz de comunicación de vista hotel con las opiniones.
public interface IOpinionsView extends ISessionView {
    //Mostramos por pantalla las opiniones de cada usuario en un hotel
    void renderData(Map<String, Opinion> value);

    //Mostramos el nombre del usuario que ha escrito la opinion
    void renderPublicProfile(String value, String uid);
}
