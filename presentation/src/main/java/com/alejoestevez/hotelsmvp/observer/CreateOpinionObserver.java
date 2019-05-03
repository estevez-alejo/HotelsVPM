package com.alejoestevez.hotelsmvp.observer;

import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.mvp.view.IOpinionView;

import io.reactivex.observers.DisposableObserver;

//Observador para crear opinión de un hotel.
public class CreateOpinionObserver extends DisposableObserver<Opinion> {

    private IOpinionView iOpinionView;

    public CreateOpinionObserver(IOpinionView iOpinionView) {
        this.iOpinionView = iOpinionView;
    }
    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(Opinion value) {
        iOpinionView.opinionCreated(value);
    }

    //En caso de error, entrará por aqui.
    @Override
    public void onError(Throwable e) {
        iOpinionView.hideLoading();
    }

    //En caso de finalizar la tarea, entrará por aqui.
    @Override
    public void onComplete() {
        iOpinionView.hideLoading();
    }
}

