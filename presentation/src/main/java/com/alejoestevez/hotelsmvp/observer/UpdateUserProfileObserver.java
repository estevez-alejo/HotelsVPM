package com.alejoestevez.hotelsmvp.observer;

import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.mvp.view.IEditProfileView;

import io.reactivex.observers.DisposableObserver;

//Observador responsable de actualizar la información del perfil del usuario.
public class UpdateUserProfileObserver extends DisposableObserver<User> {

    private IEditProfileView iEditProfileView;

    public UpdateUserProfileObserver(IEditProfileView iEditProfileView) {
        this.iEditProfileView = iEditProfileView;
    }

    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(User value) {
        iEditProfileView.updatedUser(value);
    }

    //En caso de error, entrará por aqui.
    @Override
    public void onError(Throwable e) {
        iEditProfileView.hideLoading();
    }

    //En caso de finalizar la tarea, entrará por aqui.
    @Override
    public void onComplete() {
        iEditProfileView.hideLoading();
    }
}
