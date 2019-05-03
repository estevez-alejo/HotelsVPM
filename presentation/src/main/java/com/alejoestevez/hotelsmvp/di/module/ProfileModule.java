package com.alejoestevez.hotelsmvp.di.module;

import com.alejoestevez.hotelsmvp.domain.interactor.CheckIsUserSignedInUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.LoadImageUrlUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.SignOutUseCase;
import com.alejoestevez.hotelsmvp.mvp.presenter.ProfilePresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IProfileView;
import com.alejoestevez.hotelsmvp.ui.ProfileActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ProfileModule {
    @Provides
    static ProfilePresenter provideProfilePresenter(IProfileView iProfileView, CheckIsUserSignedInUseCase checkIsUserSignedInUseCase, LoadImageUrlUseCase loadImageUrlUseCase, SignOutUseCase signOutUseCase) {
        return new ProfilePresenter(iProfileView, checkIsUserSignedInUseCase, loadImageUrlUseCase, signOutUseCase);
    }

    @Binds
    abstract IProfileView provideProfileView(ProfileActivity profileActivity);
}
