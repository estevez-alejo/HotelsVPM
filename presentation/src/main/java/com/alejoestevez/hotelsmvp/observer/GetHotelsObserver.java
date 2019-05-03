package com.alejoestevez.hotelsmvp.observer;

import com.alejoestevez.hotelsmvp.domain.model.Hotel;
import com.alejoestevez.hotelsmvp.mvp.view.IHomeView;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

//Observador para obtener hoteles
public class GetHotelsObserver extends DisposableObserver<List<Hotel>> {

    private IHomeView iHomeView;

    public GetHotelsObserver(IHomeView iHomeView) {
        this.iHomeView = iHomeView;
    }

    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(List<Hotel> value) {
        iHomeView.renderData(value);
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

