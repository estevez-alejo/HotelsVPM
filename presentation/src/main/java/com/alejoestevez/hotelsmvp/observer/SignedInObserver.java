package com.alejoestevez.hotelsmvp.observer;

import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.mvp.view.ISessionView;

import io.reactivex.observers.DisposableObserver;

//Observador para controlar que un usuario ha iniciado sesión en el sistema
public class SignedInObserver extends DisposableObserver<User> {

    private ISessionView iSessionView;

    public SignedInObserver(ISessionView iSessionView) {
        this.iSessionView = iSessionView;
    }

    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(User value) {
        iSessionView.updateUI(value);
        iSessionView.onSignedIn();

    }

    //En caso de error, entrará por aqui.
    @Override
    public void onError(Throwable e) {
        iSessionView.hideLoading();
        iSessionView.onSignedOut();
    }

    //En caso de finalizar la tarea, entrará por aqui.
    @Override
    public void onComplete() {
        iSessionView.hideLoading();

    }
}
