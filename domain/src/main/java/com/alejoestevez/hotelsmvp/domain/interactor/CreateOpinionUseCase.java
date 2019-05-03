package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.interactor.parameters.OpinionParameters;
import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.domain.repositories.IOpinionRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class CreateOpinionUseCase extends UseCaseBase<Opinion, OpinionParameters.Parameters> {

    private IOpinionRepository iOpinionRepository;
    private Scheduler schedulerThread;

    @Inject
    public CreateOpinionUseCase(IOpinionRepository iOpinionRepository, Scheduler schedulerThread) {
        this.iOpinionRepository = iOpinionRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<Opinion> implementUseCase(DisposableObserver observer, OpinionParameters.Parameters parameters) {
        //Invocamos el método del repositorio responsable de crear el comentario de un hotel.
        Observable<Opinion> observable = iOpinionRepository.createOpinion(parameters.getHotelId(),parameters.getOpinion());

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }

}
