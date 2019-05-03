package com.alejoestevez.hotelsmvp.di.module;

import com.alejoestevez.hotelsmvp.domain.interactor.LogInEmailUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.SignUpEmailAccountUseCase;
import com.alejoestevez.hotelsmvp.mvp.presenter.EmailLoginPresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IEmailLoginView;
import com.alejoestevez.hotelsmvp.ui.EmailLoginActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

//Módulo del Login, encargado de proveer las dependencias de la activity del Login.
@Module
public abstract class EmailLoginModule {

    @Provides
    static EmailLoginPresenter provideEmailLoginPresenter(IEmailLoginView iLoginView,
                                                          LogInEmailUseCase logInEmailUseCase,
                                                          SignUpEmailAccountUseCase signUpEmailAccountUseCase
    ) {
        return new EmailLoginPresenter(iLoginView, logInEmailUseCase, signUpEmailAccountUseCase);
    }

    @Binds
    abstract IEmailLoginView provideEmailLoginView(EmailLoginActivity emilLoginActivity);
}
