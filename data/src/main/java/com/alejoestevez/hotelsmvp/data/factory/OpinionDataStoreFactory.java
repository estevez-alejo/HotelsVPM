package com.alejoestevez.hotelsmvp.data.factory;

import android.content.Context;

import com.alejoestevez.hotelsmvp.data.datastore.IOpinionDataStore;
import com.alejoestevez.hotelsmvp.data.datastore.remote.RemoteOpinionDataStore;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OpinionDataStoreFactory implements IDataStoreFactory {

    Context context;

    @Inject
    public OpinionDataStoreFactory(Context context) {
        this.context = context;
    }

    @Override
    public IOpinionDataStore Remote() {
        return new RemoteOpinionDataStore(context);
    }

    @Override
    public IOpinionDataStore Local() {
        return null;
    }
}

