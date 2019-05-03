package com.alejoestevez.hotelsmvp.di.module;

import com.alejoestevez.hotelsmvp.domain.interactor.UpdateUserProfileUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.UploadFileUseCase;
import com.alejoestevez.hotelsmvp.mvp.presenter.EditProfilePresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IEditProfileView;
import com.alejoestevez.hotelsmvp.ui.EditProfileActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class EditProfileModule {
    @Provides
    static EditProfilePresenter provideEditProfilePresenter(IEditProfileView iEditProfileView, UpdateUserProfileUseCase updateUserProfileUseCase, UploadFileUseCase uploadFileUseCase) {
        return new EditProfilePresenter(iEditProfileView, updateUserProfileUseCase, uploadFileUseCase);
    }

    @Binds
    abstract IEditProfileView provideEditProfileView(EditProfileActivity editProfileActivity);
}
