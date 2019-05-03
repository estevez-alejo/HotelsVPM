package com.alejoestevez.hotelsmvp.di.module;

import com.alejoestevez.hotelsmvp.domain.interactor.CheckIsUserSignedInUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.GetHotelRatingUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.GetHotelsUseCase;
import com.alejoestevez.hotelsmvp.mvp.presenter.HomePresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IHomeView;
import com.alejoestevez.hotelsmvp.ui.HomeActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

//Módulo del Home, encargado de proveer las dependencias de la activity del Home.
@Module
public abstract class HomeModule {

    @Provides
    static HomePresenter provideHomePresenter(IHomeView iHomeView
            , GetHotelsUseCase getHotelsUseCase
            , GetHotelRatingUseCase getHotelRatingUseCase
            , CheckIsUserSignedInUseCase checkIsUserSignedInUseCase
    ) {
        return new HomePresenter(iHomeView, getHotelsUseCase, getHotelRatingUseCase, checkIsUserSignedInUseCase);
    }

    @Binds
    abstract IHomeView provideHomeView(HomeActivity homeActivity);
}
