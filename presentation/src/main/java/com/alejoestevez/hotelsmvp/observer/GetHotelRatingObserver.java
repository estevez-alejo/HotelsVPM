package com.alejoestevez.hotelsmvp.observer;

import com.alejoestevez.hotelsmvp.mvp.view.IHomeView;

import io.reactivex.observers.DisposableObserver;

//Observador para obtener puntuación de un hotel.
public class GetHotelRatingObserver extends DisposableObserver<Double> {

    private IHomeView iHomeView;
    private int hotelId;

    public GetHotelRatingObserver(IHomeView iHomeView, int hotelId) {
        this.iHomeView = iHomeView;
        this.hotelId = hotelId;
    }

    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(Double value) {
        iHomeView.renderRating(value, hotelId);
    }

    //En caso de error, entrará por aqui.
    @Override
    public void onError(Throwable e) {
        iHomeView.hideLoading();
    }

    //En caso de finalizar la tarea, entrará por aqui.
    @Override
    public void onComplete() {
        iHomeView.hideLoading();

    }
}
