package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.repositories.IFileStorageRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class LoadImageUrlUseCase extends UseCaseBase<byte[], String> {

    private IFileStorageRepository iFileStorageRepository;
    private Scheduler schedulerThread;

    @Inject
    public LoadImageUrlUseCase(IFileStorageRepository iFileStorageRepository, Scheduler schedulerThread) {
        this.iFileStorageRepository = iFileStorageRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<byte[]> implementUseCase(DisposableObserver observer, String url) {

        //Invocamos el método del repositorio responsable de cargar una imagen en base a una url.
        Observable<byte[]> observable = iFileStorageRepository.loadImageBitmapFromUrl(url);

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }


}
