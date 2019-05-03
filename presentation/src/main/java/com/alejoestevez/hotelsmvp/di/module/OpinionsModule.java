package com.alejoestevez.hotelsmvp.di.module;

import com.alejoestevez.hotelsmvp.domain.interactor.CheckIsUserSignedInUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.GetOpinionsUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.GetPublicProfileUseCase;
import com.alejoestevez.hotelsmvp.mvp.presenter.OpinionsPresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IOpinionsView;
import com.alejoestevez.hotelsmvp.ui.OpinionsActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class OpinionsModule {

    @Provides
    static OpinionsPresenter provideOpinionsPresenter(IOpinionsView iOpinionsView
            , CheckIsUserSignedInUseCase checkIsUserSignedInUseCase
            , GetOpinionsUseCase getOpinionsUseCase
            , GetPublicProfileUseCase getPublicProfileUseCase
    ) {
        return new OpinionsPresenter(iOpinionsView, checkIsUserSignedInUseCase, getOpinionsUseCase, getPublicProfileUseCase);
    }

    @Binds
    abstract IOpinionsView provideOpinionsView(OpinionsActivity opinionsActivity);
}