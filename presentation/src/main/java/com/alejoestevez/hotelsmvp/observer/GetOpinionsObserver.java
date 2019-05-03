package com.alejoestevez.hotelsmvp.observer;

import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.mvp.view.IOpinionsView;

import java.util.Map;

import io.reactivex.observers.DisposableObserver;

//Observador para obtener opiniones de un hotel.
public class GetOpinionsObserver extends DisposableObserver<Map<String,Opinion>> {

    private IOpinionsView iOpinionsView;

    public GetOpinionsObserver(IOpinionsView iOpinionsView)
    {
        this.iOpinionsView = iOpinionsView;
    }

    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(Map<String,Opinion> value) {
        iOpinionsView.renderData(value);
    }

    //En caso de error, entrará por aqui.
    @Override
    public void onError(Throwable e) {
        iOpinionsView.hideLoading();
    }

    //En caso de finalizar la tarea, entrará por aqui.
    @Override
    public void onComplete() {
        iOpinionsView.hideLoading();

    }
}


