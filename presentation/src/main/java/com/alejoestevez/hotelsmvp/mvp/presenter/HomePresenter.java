package com.alejoestevez.hotelsmvp.mvp.presenter;

import com.alejoestevez.hotelsmvp.domain.interactor.CheckIsUserSignedInUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.GetHotelRatingUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.GetHotelsUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.parameters.HotelParameters;
import com.alejoestevez.hotelsmvp.mvp.view.IHomeView;
import com.alejoestevez.hotelsmvp.observer.GetHotelRatingObserver;
import com.alejoestevez.hotelsmvp.observer.GetHotelsObserver;
import com.alejoestevez.hotelsmvp.observer.SignedInObserver;

import javax.inject.Inject;

public class HomePresenter implements IPresenter {

    IHomeView iHomeView;
    private GetHotelsUseCase getHotelsUseCase;
    private GetHotelRatingUseCase getHotelRatingUseCase;

    private CheckIsUserSignedInUseCase checkIsUserSignedInUseCase;

    @Inject
    public HomePresenter(IHomeView iHomeView
            , GetHotelsUseCase getHotelsUseCase
            , GetHotelRatingUseCase getHotelRatingUseCase
            , CheckIsUserSignedInUseCase checkIsUserSignedInUseCase
    ) {
        this.iHomeView = iHomeView;
        this.getHotelsUseCase = getHotelsUseCase;
        this.getHotelRatingUseCase = getHotelRatingUseCase;
        this.checkIsUserSignedInUseCase = checkIsUserSignedInUseCase;
    }

    //Inicializamos el presentador, mirando si estamos con una sesión abierta y además listaremos los hoteles.
    public void initialize() {
        isSignedIn();
        getHotels();
    }

    //Miramos si hay abierta una sesión, para indicarlo en el NavigationDrawer
    public void isSignedIn() {
        iHomeView.showLoading();
        checkIsUserSignedInUseCase.implementUseCase(new SignedInObserver(iHomeView), null);
    }
    //Buscamos los hoteles
    public void getHotels() {
        iHomeView.showLoading();
        getHotelsUseCase.implementUseCase(new GetHotelsObserver(iHomeView), null);
    }
    //Obtenemos la puntuación de cada hotel
    public void getRating(int hotelId) {
        iHomeView.showLoading();
        getHotelRatingUseCase.implementUseCase(new GetHotelRatingObserver(iHomeView, hotelId), HotelParameters.Parameters.Create(hotelId));
    }
    //Refrescamos los elementos
    public void refreshItems() {
        getHotels();
    }

    @Override
    public void destroy() {
        if (getHotelsUseCase != null) getHotelsUseCase.cancelSubscription();
        if (getHotelRatingUseCase != null) getHotelRatingUseCase.cancelSubscription();
        if (checkIsUserSignedInUseCase != null) checkIsUserSignedInUseCase.cancelSubscription();
    }
}
