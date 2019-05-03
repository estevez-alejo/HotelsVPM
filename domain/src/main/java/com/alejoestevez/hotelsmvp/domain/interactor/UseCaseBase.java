package com.alejoestevez.hotelsmvp.domain.interactor;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

//Clase base que deben cumplir los casos de uso
public abstract class UseCaseBase<Type, Object> implements IObservableUseCase {
    //Listado de observadores subscritos.
    ArrayList<DisposableObserver<Type>> observers = new ArrayList<>();

    //Creamos el caso de uso con un observable, al que subscribimos un observador, escuchando en el hilo principal.
    public void createUseCase(Observable observable, DisposableObserver<Type> observer, Scheduler schedulerThread) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(schedulerThread)
                .subscribeWith(observer);
        subscribe(observer);
    }

    //Método abstracto para implementar el caso de uso segun el tipo de observador y parámetro.
    public abstract Observable<Type> implementUseCase(DisposableObserver observer, Object object);

    //Subscripción de un observer al listado de observadores subscritos
    @Override
    public void subscribe(DisposableObserver observer) {
        if (!observers.contains(observer)) observers.add(observer);
    }

    //Cancelar Subscripción de los observadores subscritos.
    @Override
    public void cancelSubscription() {
        //Clonamos el listado de observers.
        ArrayList<DisposableObserver<Type>> observersAux = (ArrayList<DisposableObserver<Type>>) observers.clone();
        for (DisposableObserver observer : observersAux) {
            if (observer != null && observers.contains(observer)) {
                observer.dispose();
                observers.remove(observer);
            }
        }


    }

}