package com.alejoestevez.hotelsmvp.observer;

import com.alejoestevez.hotelsmvp.mvp.view.IEditProfileView;

import io.reactivex.observers.DisposableObserver;

//Observador para obtener la URL de una imagen subida.
public class PhotoObserver extends DisposableObserver<String> {

    private IEditProfileView iEditProfileView;

    public PhotoObserver(IEditProfileView iEditProfileView) {
        this.iEditProfileView = iEditProfileView;
    }

    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(String value) {
        iEditProfileView.photoUploaded(value);
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

