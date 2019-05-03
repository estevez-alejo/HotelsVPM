package com.alejoestevez.hotelsmvp.data.factory;

import android.content.Context;

import com.alejoestevez.hotelsmvp.data.datastore.ISessionDataStore;
import com.alejoestevez.hotelsmvp.data.datastore.local.LocalSessionDataStore;
import com.alejoestevez.hotelsmvp.data.datastore.remote.RemoteSessionDataStore;

import javax.inject.Inject;
import javax.inject.Singleton;

//Clase que implementa la factoria que retorna las instancias de las clases que gestionan el acceso local o remoto.
@Singleton
public class SessionDataStoreFactory implements IDataStoreFactory {

    Context context;

    @Inject
    public SessionDataStoreFactory(Context context) {
        this.context = context;
    }

    @Override
    public ISessionDataStore Remote() {
        return new RemoteSessionDataStore(context);
    }

    @Override
    public ISessionDataStore Local() {
        return new LocalSessionDataStore(context);
    }
}