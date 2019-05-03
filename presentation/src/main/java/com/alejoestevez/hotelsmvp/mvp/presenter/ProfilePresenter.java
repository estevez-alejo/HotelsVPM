package com.alejoestevez.hotelsmvp.mvp.presenter;

import com.alejoestevez.hotelsmvp.domain.interactor.CheckIsUserSignedInUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.LoadImageUrlUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.SignOutUseCase;
import com.alejoestevez.hotelsmvp.mvp.view.IProfileView;
import com.alejoestevez.hotelsmvp.observer.LoadImageObserver;
import com.alejoestevez.hotelsmvp.observer.SignOutObserver;
import com.alejoestevez.hotelsmvp.observer.SignedInObserver;

import javax.inject.Inject;

public class ProfilePresenter implements IPresenter {

    private IProfileView iProfileView;

    private CheckIsUserSignedInUseCase checkIsUserSignedInUseCase;
    private SignOutUseCase signOutUseCase;
    private LoadImageUrlUseCase loadImageUrlUseCase;

    @Inject
    public ProfilePresenter(IProfileView iProfileView,
                            CheckIsUserSignedInUseCase checkIsUserSignedInUseCase,
                            LoadImageUrlUseCase loadImageUrlUseCase,
                            SignOutUseCase signOutUseCase) {
        this.iProfileView = iProfileView;
        this.checkIsUserSignedInUseCase = checkIsUserSignedInUseCase;
        this.loadImageUrlUseCase = loadImageUrlUseCase;
        this.signOutUseCase = signOutUseCase;
    }

    //Comprobamos si tenemos sesión abierta
    public void isSignedIn() {
        iProfileView.showLoading();
        checkIsUserSignedInUseCase.implementUseCase(new SignedInObserver(iProfileView), null);
    }

    //Obtenemos una imagen en base a una Url, y se lo asignamos a un recurso
    public void loadImageUrl(String url, int resourceId) {
        iProfileView.showLoading();
        loadImageUrlUseCase.implementUseCase(new LoadImageObserver(iProfileView, resourceId), url);
    }

    //Cierre de sesión
    public void signOut() {
        signOutUseCase.implementUseCase(new SignOutObserver(iProfileView), null);
    }

    @Override
    public void destroy() {
        if (checkIsUserSignedInUseCase != null) checkIsUserSignedInUseCase.cancelSubscription();
        if (loadImageUrlUseCase != null) loadImageUrlUseCase.cancelSubscription();
        if (signOutUseCase != null) signOutUseCase.cancelSubscription();
    }

}
