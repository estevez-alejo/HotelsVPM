package com.alejoestevez.hotelsmvp.observer;

import com.alejoestevez.hotelsmvp.mvp.view.IOpinionsView;

import io.reactivex.observers.DisposableObserver;

//Observador para obtener el perfil público de un usuario.
public class GetPublicProfileObserver extends DisposableObserver<String> {

    private IOpinionsView iOpinionsView;
    private String uid;

    public GetPublicProfileObserver(IOpinionsView iOpinionsView, String uid) {
        this.iOpinionsView = iOpinionsView;
        this.uid = uid;
    }

    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(String value) {
        iOpinionsView.renderPublicProfile(value, uid);
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
