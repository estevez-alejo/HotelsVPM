package com.alejoestevez.hotelsmvp.data.factory;

import android.content.Context;

import com.alejoestevez.hotelsmvp.data.datastore.IHotelDataStore;
import com.alejoestevez.hotelsmvp.data.datastore.remote.RemoteHotelDataStore;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HotelDataStoreFactory implements IDataStoreFactory {

    Context context;

    @Inject
    public HotelDataStoreFactory(Context context) {
        this.context = context;
    }

    @Override
    public IHotelDataStore Remote() {
        return new RemoteHotelDataStore(context);
    }

    @Override
    public IHotelDataStore Local() {
        return null;
    }
}
