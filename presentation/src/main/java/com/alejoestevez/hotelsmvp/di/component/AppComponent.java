package com.alejoestevez.hotelsmvp.di.component;

import com.alejoestevez.hotelsmvp.App;
import com.alejoestevez.hotelsmvp.di.builder.BuildersModule;
import com.alejoestevez.hotelsmvp.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

//El componente se encarga de referenciar los objetos Singleton a las activity.
//Debemos indicar el módulo/s que utiliza el componente.

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        BuildersModule.class
}
)
public interface AppComponent {

    //Constructor del componente de la aplicación
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(App application);

        AppComponent build();
    }

    //Método inyector de la aplicación
    void inject(App app);
}
