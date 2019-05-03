package com.alejoestevez.hotelsmvp.di.module;

import com.alejoestevez.hotelsmvp.domain.interactor.CreateOpinionUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.CreateRatingUseCase;
import com.alejoestevez.hotelsmvp.mvp.presenter.OpinionPresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IOpinionView;
import com.alejoestevez.hotelsmvp.ui.OpinionActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class OpinionModule {

    @Provides
    static OpinionPresenter provideOpinionPresenter(IOpinionView iOpinionView
            , CreateOpinionUseCase createOpinionUseCase
            , CreateRatingUseCase createRatingUseCase
    ) {
        return new OpinionPresenter(iOpinionView, createOpinionUseCase, createRatingUseCase);
    }

    @Binds
    abstract IOpinionView provideOpinionView(OpinionActivity opinionActivity);
}
