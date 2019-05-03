package com.alejoestevez.hotelsmvp.mvp.view;

import com.alejoestevez.hotelsmvp.domain.model.User;

//Interfaz de comunicación para la sesión de usuarios.
public interface ISessionView extends IDataView {
    //Cuando tenemos los datos del usuario conectado, los obtenemos.
    void updateUI(User user);

    //Acción a realizar cuando tenemos sesión activa.
    void onSignedIn();

    //Acción a realizar cuando hemos cerrado sesión.
    void onSignedOut();

}
