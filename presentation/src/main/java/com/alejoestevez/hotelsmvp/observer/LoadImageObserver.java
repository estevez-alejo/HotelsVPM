package com.alejoestevez.hotelsmvp.observer;

import com.alejoestevez.hotelsmvp.mvp.view.IProfileView;

import io.reactivex.observers.DisposableObserver;

//Observador para cargar los bytes recogidos de una imagen
public class LoadImageObserver extends DisposableObserver<byte[]> {

    private IProfileView iProfileView;
    private int resourceId;

    public LoadImageObserver(IProfileView iProfileView, int resourceId) {
        this.iProfileView = iProfileView;
        this.resourceId = resourceId;
    }

    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(byte[] data) {

        iProfileView.photoLoaded(data, resourceId);
    }

    //En caso de error, entrará por aqui.
    @Override
    public void onError(Throwable e) {
        iProfileView.hideLoading();
    }

    //En caso de finalizar la tarea, entrará por aqui.
    @Override
    public void onComplete() {
        iProfileView.hideLoading();

    }
}

