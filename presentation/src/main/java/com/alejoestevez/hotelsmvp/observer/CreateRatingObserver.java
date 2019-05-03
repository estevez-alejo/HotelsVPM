package com.alejoestevez.hotelsmvp.observer;

import com.alejoestevez.hotelsmvp.domain.model.Rating;
import com.alejoestevez.hotelsmvp.mvp.view.IOpinionView;

import io.reactivex.observers.DisposableObserver;

//Observador para crear puntuación de un hotel.
public class CreateRatingObserver extends DisposableObserver<Rating> {

    private IOpinionView iOpinionView;

    public CreateRatingObserver(IOpinionView iOpinionView)
    {
        this.iOpinionView = iOpinionView;
    }

    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(Rating value) {
        iOpinionView.ratingCreated(value);
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
