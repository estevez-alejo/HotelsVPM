package com.alejoestevez.hotelsmvp.mvp.view;

import com.alejoestevez.hotelsmvp.domain.model.User;

//Interfaz de comunicación de la Edición del perfil.
public interface IEditProfileView extends IDataView {
    //Url de la imagen que hemos subido al FirebaseStorage
    void photoUploaded(String value);

    //Usuario actualizado
    void updatedUser(User value);
}
