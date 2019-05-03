package com.alejoestevez.hotelsmvp.mvp.view;

//Interfaz de comunicación del perfil del usuario
public interface IProfileView extends ISessionView {
    //Bytes recibidos tras cargar la imagen, en un recurso determinado.
    void photoLoaded(byte[] value, int resourceId);
}
