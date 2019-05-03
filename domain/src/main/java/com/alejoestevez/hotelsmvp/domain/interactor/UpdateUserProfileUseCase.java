package com.alejoestevez.hotelsmvp.domain.interactor;

import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.domain.repositories.IUserProfileRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

public class UpdateUserProfileUseCase extends UseCaseBase<User, User> {

    private IUserProfileRepository iUserProfileRepository;
    private Scheduler schedulerThread;

    @Inject
    public UpdateUserProfileUseCase(IUserProfileRepository iUserProfileRepository, Scheduler schedulerThread) {
        this.iUserProfileRepository = iUserProfileRepository;
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<User> implementUseCase(DisposableObserver observer, User user) {

        //Observable para guardar el perfil del usuario
        Observable<User> observable = iUserProfileRepository.save(user);

        //Con este observable, construimos el caso de uso, al que subscribimos el observador recibido.
        this.createUseCase(observable, observer, schedulerThread);

        return observable;
    }


}
