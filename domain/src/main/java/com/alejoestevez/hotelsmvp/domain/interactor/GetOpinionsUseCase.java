package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.domain.repositories.IOpinionRepository;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class GetOpinionsUseCase extends UseCaseBase<Map<String,Opinion>, Integer> {

    private IOpinionRepository iOpinionRepository;
    private Scheduler schedulerThread;

    @Inject
    public GetOpinionsUseCase(IOpinionRepository iOpinionRepository, Scheduler schedulerThread) {
        this.iOpinionRepository = iOpinionRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<Map<String,Opinion>> implementUseCase(DisposableObserver observer, Integer hotelId) {
        //Invocamos el método del repositorio responsable de obtener los comentarios de un hotel determinado.
        Observable<Map<String,Opinion>> observable = iOpinionRepository.getOpinions(hotelId);

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }

}
