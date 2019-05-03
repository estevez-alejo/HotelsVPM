package com.alejoestevez.hotelsmvp.di.module;

import com.alejoestevez.hotelsmvp.domain.interactor.CheckIsUserSignedInUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.SignInUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.SignOutUseCase;
import com.alejoestevez.hotelsmvp.mvp.presenter.LoginPresenter;
import com.alejoestevez.hotelsmvp.mvp.view.ILoginView;
import com.alejoestevez.hotelsmvp.ui.LoginActivity;
import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

//Módulo del Login, encargado de proveer las dependencias de la activity del Login.
@Module
public abstract class LoginModule {

    @Provides
    static LoginPresenter provideLoginPresenter(
            ILoginView iLoginView, SignInUseCase signInUseCase, SignOutUseCase signOutUseCase, CheckIsUserSignedInUseCase checkIsUserSignedInUseCase, GoogleApiClient googleApiClient
    ) {
        return new LoginPresenter(iLoginView, signInUseCase, signOutUseCase, checkIsUserSignedInUseCase, googleApiClient);
    }

    @Binds
    abstract ILoginView provideLoginView(LoginActivity loginActivity);
}
