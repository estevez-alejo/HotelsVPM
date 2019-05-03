package com.alejoestevez.hotelsmvp.mvp.view;

//Interfaz de comunicación del Login del Email
public interface IEmailLoginView extends ISessionView {
    //Mostrar mensaje en caso de que el email no sea válido.
    void showEmailMessageError(String s);

    //En caso de que el email sea válido, limpiar los mensajes de error y vaciar el email.
    void showEmailNoErrors();

    //Mostrar mensaje en caso de error de contraseña no válida.
    void showPasswordMessageError(String s);

    //En caso de que la contraseña sea válida, limpiar los mensajes de error y vaciar la contraseña.
    void showPasswordNoErrors();
}
