package com.alejoestevez.hotelsmvp.di.module;

import android.content.Context;

import com.alejoestevez.hotelsmvp.data.FileStorageRepositoryImplementation;
import com.alejoestevez.hotelsmvp.data.HotelRepositoryImplementation;
import com.alejoestevez.hotelsmvp.data.OpinionRepositoryImplementation;
import com.alejoestevez.hotelsmvp.data.SignInRepositoryImplementation;
import com.alejoestevez.hotelsmvp.data.UserProfileRepositoryImplementation;
import com.alejoestevez.hotelsmvp.domain.repositories.IFileStorageRepository;
import com.alejoestevez.hotelsmvp.domain.repositories.IHotelRepository;
import com.alejoestevez.hotelsmvp.domain.repositories.IOpinionRepository;
import com.alejoestevez.hotelsmvp.domain.repositories.ISignInRepository;
import com.alejoestevez.hotelsmvp.domain.repositories.IUserProfileRepository;
import com.alejoestevez.hotelsmvp.App;
import com.alejoestevez.hotelsmvp.R;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

//Módulo Dagger para la gestión de las dependencias.
@Module
public class AppModule {
    //En el modulo se encuentran las variables y métodos que proveen dependencias, usando el tag @Provides.
    //El tag Singleton, establece que una instancia de un objeto, será creado una vez únicamente, en toda la aplicación.


    @Provides
    @Singleton
    static Context provideContext(App application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    static Scheduler provideScheduler() {
        return AndroidSchedulers.mainThread();
    }


    @Provides
    @Singleton
    static GoogleApiClient provideGoogleApiClient(App application) {

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(application.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleApiClient googleApiClient = new GoogleApiClient
                .Builder(application)
                //.enableAutoManage(application,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
        return googleApiClient;
    }

    @Provides
    @Singleton
    static CallbackManager provideCallbackManager() {
        return CallbackManager.Factory.create();
    }

    @Provides
    @Singleton
    static ISignInRepository providesSignInRepository(SignInRepositoryImplementation signInRepositoryImplementation) {
        return signInRepositoryImplementation;
    }

    @Provides
    @Singleton
    static IUserProfileRepository providesUserProfileRepository(UserProfileRepositoryImplementation userProfileRepositoryImplementation) {
        return userProfileRepositoryImplementation;
    }

    @Provides
    @Singleton
    static IFileStorageRepository providesFileStorageRepository(FileStorageRepositoryImplementation fileStorageRepositoryImplementation) {
        return fileStorageRepositoryImplementation;
    }

    @Provides
    @Singleton
    static IHotelRepository providesHotelRepository(HotelRepositoryImplementation hotelRepositoryImplementation) {
        return hotelRepositoryImplementation;
    }

    @Provides
    @Singleton
    static IOpinionRepository providesOpinionRepository(OpinionRepositoryImplementation opinionRepositoryImplementation) {
        return opinionRepositoryImplementation;
    }

    @Provides
    @Singleton
    static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

}
