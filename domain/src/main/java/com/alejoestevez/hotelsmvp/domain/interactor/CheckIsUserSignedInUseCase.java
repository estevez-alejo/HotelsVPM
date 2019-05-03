package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.domain.repositories.ISignInRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class CheckIsUserSignedInUseCase extends UseCaseBase<User, Void> {

    private ISignInRepository iSignInRepository;
    private Scheduler schedulerThread;

    @Inject
    public CheckIsUserSignedInUseCase(ISignInRepository iSignInRepository, Scheduler schedulerThread) {
        this.iSignInRepository = iSignInRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<User> implementUseCase(DisposableObserver observer, Void parameters) {
        //Invocamos el método del repositorio responsable de verificar si hay una sesión abierta.
        Observable<User> observable = iSignInRepository.isSessionOpen();

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }

}
