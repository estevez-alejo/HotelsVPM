package com.alejoestevez.hotelsmvp.observer;

import com.alejoestevez.hotelsmvp.mvp.view.ISessionView;

import io.reactivex.observers.DisposableObserver;

//Observador responsable de cerrar sesión de usuario.
public class SignOutObserver extends DisposableObserver<Void> {

    private ISessionView iSessionView;

    public SignOutObserver(ISessionView iSessionView) {
        this.iSessionView = iSessionView;
    }

    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(Void value) {

    }

    //En caso de error, entrará por aqui.
    @Override
    public void onError(Throwable e) {
        iSessionView.hideLoading();
    }

    //En caso de finalizar la tarea, entrará por aqui.
    @Override
    public void onComplete() {
        iSessionView.hideLoading();
        iSessionView.onSignedOut();
    }
}
