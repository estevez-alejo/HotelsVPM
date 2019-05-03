package com.alejoestevez.hotelsmvp.data.factory;

import com.alejoestevez.hotelsmvp.data.datastore.IDataStore;

//Interfaz con los métodos que retornarán la clase responsable de almacenamiento local o remoto.
public interface IDataStoreFactory {
    IDataStore Remote();

    IDataStore Local();
}
