package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.repositories.IUserProfileRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class GetPublicProfileUseCase extends UseCaseBase<String, String> {

    private IUserProfileRepository iUserProfileRepository;
    private Scheduler schedulerThread;

    @Inject
    public GetPublicProfileUseCase(IUserProfileRepository iUserProfileRepository, Scheduler schedulerThread) {
        this.iUserProfileRepository = iUserProfileRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<String> implementUseCase(DisposableObserver observer, String uid) {
        //Invocamos el método del repositorio responsable de cerrar sesión.
        Observable<String> observable = iUserProfileRepository.getPublicProfile(uid);

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }

}

