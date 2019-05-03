package com.alejoestevez.hotelsmvp.domain.interactor;

import io.reactivex.observers.DisposableObserver;

//Interfaz que los observables deben cumplir.Subscribir y Cancelar Subscripción.
public interface IObservableUseCase {
    void subscribe(DisposableObserver observer);

    void cancelSubscription();
}