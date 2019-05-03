package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.interactor.parameters.SignInParameters;
import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.domain.repositories.ISignInRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class SignInUseCase extends UseCaseBase<User, SignInParameters.Parameters> {

    private ISignInRepository iSignInRepository;
    private Scheduler schedulerThread;

    @Inject
    public SignInUseCase(ISignInRepository iSignInRepository, Scheduler schedulerThread) {
        this.iSignInRepository = iSignInRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<User> implementUseCase(DisposableObserver observer, SignInParameters.Parameters parameters) {
        //Invocamos el método del repositorio responsable de iniciar sesión
        Observable<User> observable = iSignInRepository.signIn(parameters.getSessionProvider(), parameters.getAccountIdToken());

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }


}
