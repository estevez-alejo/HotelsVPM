package com.alejoestevez.hotelsmvp.mvp.presenter;

import com.alejoestevez.hotelsmvp.domain.interactor.UpdateUserProfileUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.UploadFileUseCase;
import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.mvp.view.IEditProfileView;
import com.alejoestevez.hotelsmvp.observer.PhotoObserver;
import com.alejoestevez.hotelsmvp.observer.UpdateUserProfileObserver;

import javax.inject.Inject;

//Presentador de la edición del perfil.
public class EditProfilePresenter implements IPresenter {
    IEditProfileView iEditProfileView;

    private UpdateUserProfileUseCase updateUserProfileUseCase;
    private UploadFileUseCase uploadFileUseCase;

    @Inject
    public EditProfilePresenter(IEditProfileView iEditProfileView,
                                UpdateUserProfileUseCase updateUserProfileUseCase,
                                UploadFileUseCase uploadFileUseCase) {
        this.iEditProfileView = iEditProfileView;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.uploadFileUseCase = uploadFileUseCase;
    }

    //Guardamos el perfil del usuario
    public void saveProfile(User user) {
        updateUserProfileUseCase.implementUseCase(new UpdateUserProfileObserver(iEditProfileView), user);
    }

    //Subimos la imagen al repositorio.
    public void uploadProfilePhoto(byte[] data) {
        uploadFileUseCase.implementUseCase(new PhotoObserver(iEditProfileView), data);
    }

    @Override
    public void destroy() {
        if (updateUserProfileUseCase != null) updateUserProfileUseCase.cancelSubscription();
        if (uploadFileUseCase != null) uploadFileUseCase.cancelSubscription();
    }
}

