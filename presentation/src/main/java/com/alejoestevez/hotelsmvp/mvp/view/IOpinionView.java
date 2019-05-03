package com.alejoestevez.hotelsmvp.mvp.view;

import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.domain.model.Rating;

//Interfaz de creación de opiniones
public interface IOpinionView extends IDataView {
    //Acción, tras crear la opinion
    void opinionCreated(Opinion opinion);
    //Acción, tras crear puntuación
    void ratingCreated(Rating value);
}
