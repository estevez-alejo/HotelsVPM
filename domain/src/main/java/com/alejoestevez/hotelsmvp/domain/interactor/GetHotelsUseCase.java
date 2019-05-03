package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.model.Hotel;
import com.alejoestevez.hotelsmvp.domain.repositories.IHotelRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class GetHotelsUseCase extends UseCaseBase<List<Hotel>, Void> {

    private IHotelRepository iHotelRepository;
    private Scheduler schedulerThread;

    @Inject
    public GetHotelsUseCase(IHotelRepository iHotelRepository, Scheduler schedulerThread) {
        this.iHotelRepository = iHotelRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<List<Hotel>> implementUseCase(DisposableObserver observer, Void parameters) {
        //Invocamos el método del repositorio responsable de obtener los hoteles.
        Observable<List<Hotel>> observable = iHotelRepository.getHotels();

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }

}

