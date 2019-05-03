package com.alejoestevez.hotelsmvp;

import android.app.Activity;
import android.app.Application;

import com.alejoestevez.hotelsmvp.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.realm.Realm;
import io.realm.RealmConfiguration;

//Definición de la aplicación, indicando que vamos a inyectar activities.
public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        //Construimos el componente de la aplicación.
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);

        //Inicializamos Realm en nuestra app
            Realm.init(this);
            RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
            Realm.setDefaultConfiguration(realmConfiguration);
    }


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}