package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.repositories.ISignInRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class SignOutUseCase extends UseCaseBase<Void, Void> {

    private ISignInRepository iSignInRepository;
    private Scheduler schedulerThread;

    @Inject
    public SignOutUseCase(ISignInRepository iSignInRepository, Scheduler schedulerThread) {
        this.iSignInRepository = iSignInRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable implementUseCase(DisposableObserver observer, Void parameters) {
        //Invocamos el método del repositorio responsable de cerrar sesión.
        Observable observable = iSignInRepository.signOut();

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }
}
