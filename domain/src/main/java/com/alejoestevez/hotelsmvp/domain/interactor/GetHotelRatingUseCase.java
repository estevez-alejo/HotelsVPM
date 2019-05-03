package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.interactor.parameters.HotelParameters;
import com.alejoestevez.hotelsmvp.domain.repositories.IHotelRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class GetHotelRatingUseCase extends UseCaseBase<Double, HotelParameters.Parameters> {

    private IHotelRepository iHotelRepository;
    private Scheduler schedulerThread;

    @Inject
    public GetHotelRatingUseCase(IHotelRepository iHotelRepository, Scheduler schedulerThread) {
        this.iHotelRepository = iHotelRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<Double> implementUseCase(DisposableObserver observer, HotelParameters.Parameters parameters) {
        //Invocamos el método del repositorio responsable de obtener la puntuación de un hotel
        Observable<Double> observable = iHotelRepository.getRating(parameters.getHotelId());

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }

}
