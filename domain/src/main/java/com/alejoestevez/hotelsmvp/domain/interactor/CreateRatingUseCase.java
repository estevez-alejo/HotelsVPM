package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.interactor.parameters.OpinionParameters;
import com.alejoestevez.hotelsmvp.domain.model.Rating;
import com.alejoestevez.hotelsmvp.domain.repositories.IOpinionRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class CreateRatingUseCase extends UseCaseBase<Rating, OpinionParameters.Parameters> {

    private IOpinionRepository iOpinionRepository;
    private Scheduler schedulerThread;

    @Inject
    public CreateRatingUseCase(IOpinionRepository iOpinionRepository, Scheduler schedulerThread) {
        this.iOpinionRepository = iOpinionRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<Rating> implementUseCase(DisposableObserver observer, OpinionParameters.Parameters parameters) {
        //Invocamos el método del repositorio responsable de crear la puntuación de un hotel.
        Observable<Rating> observable = iOpinionRepository.createRating(parameters.getHotelId(),parameters.getOpinion().getRating());

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }

}
