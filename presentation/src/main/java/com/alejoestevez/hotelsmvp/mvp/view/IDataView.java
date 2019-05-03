package com.alejoestevez.hotelsmvp.mvp.view;

//Interfaz que debe cumplir toda vista que definamos.
public interface IDataView {
    //Muestra cargando
    void showLoading();

    //Oculta la carga
    void hideLoading();

    //Muestra un mensaje
    void showMessage(String message);
}

