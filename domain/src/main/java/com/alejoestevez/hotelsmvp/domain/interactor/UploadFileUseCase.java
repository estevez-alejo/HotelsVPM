package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.repositories.IUserProfileRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class UploadFileUseCase extends UseCaseBase<String, byte[]> {

    private IUserProfileRepository iUserProfileRepository;
    private Scheduler schedulerThread;

    @Inject
    public UploadFileUseCase(IUserProfileRepository iUserProfileRepository, Scheduler schedulerThread) {
        this.iUserProfileRepository = iUserProfileRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<String> implementUseCase(DisposableObserver observer, byte[] data) {
        //Observable para subir ficheros  y obtener la Url donde se ha almacenado
        Observable<String> observable = iUserProfileRepository.uploadFile(data);

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }


}

