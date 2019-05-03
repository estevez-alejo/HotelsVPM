package com.alejoestevez.hotelsmvp.mvp.presenter;

import com.alejoestevez.hotelsmvp.domain.interactor.CreateOpinionUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.CreateRatingUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.parameters.OpinionParameters;
import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.mvp.view.IOpinionView;
import com.alejoestevez.hotelsmvp.observer.CreateOpinionObserver;
import com.alejoestevez.hotelsmvp.observer.CreateRatingObserver;

import javax.inject.Inject;

public class OpinionPresenter implements IPresenter {
    IOpinionView iOpinionView;

    private CreateOpinionUseCase createOpinionUseCase;
    private CreateRatingUseCase createRatingUseCase;

    @Inject
    public OpinionPresenter(IOpinionView iOpinionView
            , CreateOpinionUseCase createOpinionUseCase
            , CreateRatingUseCase createRatingUseCase
    ) {
        this.iOpinionView = iOpinionView;
        this.createOpinionUseCase = createOpinionUseCase;
        this.createRatingUseCase = createRatingUseCase;
    }

    //Crear Opinion y puntuación.
    public void createOpinion(int hotelId, Opinion opinion) {
        createRatingUseCase.implementUseCase(new CreateRatingObserver(iOpinionView), OpinionParameters.Parameters.Create(hotelId, opinion));
        createOpinionUseCase.implementUseCase(new CreateOpinionObserver(iOpinionView), OpinionParameters.Parameters.Create(hotelId, opinion));
    }

    @Override
    public void destroy() {
        if (createOpinionUseCase != null) createOpinionUseCase.cancelSubscription();
        if (createRatingUseCase != null) createRatingUseCase.cancelSubscription();
    }

}
