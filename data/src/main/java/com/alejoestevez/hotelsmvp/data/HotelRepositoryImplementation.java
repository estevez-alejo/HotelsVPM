package com.alejoestevez.hotelsmvp.data;

import android.content.Context;

import com.alejoestevez.hotelsmvp.data.factory.HotelDataStoreFactory;
import com.alejoestevez.hotelsmvp.domain.model.Hotel;
import com.alejoestevez.hotelsmvp.domain.repositories.IHotelRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class HotelRepositoryImplementation implements IHotelRepository {

    //Factoría del almacén de datos para seleccionar entre Local o Remoto.
    private HotelDataStoreFactory hotelDataStoreFactory;

    private Context context;


    @Inject
    public HotelRepositoryImplementation(Context context, HotelDataStoreFactory hotelDataStoreFactory) {
        this.context = context;
        this.hotelDataStoreFactory = hotelDataStoreFactory;
    }

    //Obtenemos remotamente el listado de hoteles.
    @Override
    public Observable<List<Hotel>> getHotels() {
        return hotelDataStoreFactory.Remote().getHotels();

    }
    //Obtenemos remotamente las puntuaciones de un hotel.
    @Override
    public Observable<Double> getRating(int hotelId) {
        return hotelDataStoreFactory.Remote().getRating(hotelId);
    }
}
