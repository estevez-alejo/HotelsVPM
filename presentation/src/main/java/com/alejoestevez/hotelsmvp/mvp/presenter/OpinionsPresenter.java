package com.alejoestevez.hotelsmvp.mvp.presenter;

import com.alejoestevez.hotelsmvp.domain.interactor.CheckIsUserSignedInUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.GetOpinionsUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.GetPublicProfileUseCase;
import com.alejoestevez.hotelsmvp.mvp.view.IOpinionsView;
import com.alejoestevez.hotelsmvp.observer.GetOpinionsObserver;
import com.alejoestevez.hotelsmvp.observer.GetPublicProfileObserver;
import com.alejoestevez.hotelsmvp.observer.SignedInObserver;

import javax.inject.Inject;

public class OpinionsPresenter implements IPresenter {
    IOpinionsView iOpinionsView;

    private CheckIsUserSignedInUseCase checkIsUserSignedInUseCase;
    private GetOpinionsUseCase getOpinionsUseCase;
    private GetPublicProfileUseCase getPublicProfileUseCase;

    @Inject
    public OpinionsPresenter(IOpinionsView iOpinionsView
            , CheckIsUserSignedInUseCase checkIsUserSignedInUseCase
            , GetOpinionsUseCase getOpinionsUseCase
            , GetPublicProfileUseCase getPublicProfileUseCase
    ) {
        this.iOpinionsView = iOpinionsView;
        this.getOpinionsUseCase = getOpinionsUseCase;
        this.checkIsUserSignedInUseCase = checkIsUserSignedInUseCase;
        this.getPublicProfileUseCase = getPublicProfileUseCase;
    }

    //Inicializamos el presentador, mirando si tenemos una sesión abierta y además buscamos cada uno de los comentarios de un hotel determinado.
    public void initialize(int hotelId) {
        isSignedIn();
        getOpinions(hotelId);
    }

    //Comprobar sesión abierta
    public void isSignedIn() {
        checkIsUserSignedInUseCase.implementUseCase(new SignedInObserver(iOpinionsView), null);
    }

    //Obtenemos listado de opiniones de un hotel.
    public void getOpinions(int hotelId) {
        iOpinionsView.showLoading();
        getOpinionsUseCase.implementUseCase(new GetOpinionsObserver(iOpinionsView), hotelId);
    }

    //Obtenemos el perfil público de un usuario. Su nombre público.
    public void getPublicProfile(String uid) {
        getPublicProfileUseCase.implementUseCase(new GetPublicProfileObserver(iOpinionsView, uid), uid);
    }

    @Override
    public void destroy() {
        if (getOpinionsUseCase != null) getOpinionsUseCase.cancelSubscription();
        if (checkIsUserSignedInUseCase != null) checkIsUserSignedInUseCase.cancelSubscription();
        if (getPublicProfileUseCase != null) getPublicProfileUseCase.cancelSubscription();
    }
}

