package com.alejoestevez.hotelsmvp.data;

import com.alejoestevez.hotelsmvp.data.factory.OpinionDataStoreFactory;
import com.alejoestevez.hotelsmvp.data.factory.SessionDataStoreFactory;
import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.domain.model.Rating;
import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.domain.repositories.IOpinionRepository;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class OpinionRepositoryImplementation implements IOpinionRepository {

    //Factoría del almacén de datos para seleccionar entre Local o Remoto.
    private OpinionDataStoreFactory opinionDataStoreFactory;
    private SessionDataStoreFactory sessionDataStoreFactory;

    @Inject
    public OpinionRepositoryImplementation(OpinionDataStoreFactory opinionDataStoreFactory,
                                           SessionDataStoreFactory sessionDataStoreFactory) {
        this.opinionDataStoreFactory = opinionDataStoreFactory;
        this.sessionDataStoreFactory = sessionDataStoreFactory;
    }

    //Obtenemos las opiniones de un hotel, de forma remota.
    @Override
    public Observable<Map<String, Opinion>> getOpinions(int hotelId) {
        return opinionDataStoreFactory.Remote().getOpinions(hotelId);
    }

    //Creamos una opinión en un hotel, de forma remota. Para ello necesitamos obtener el tokenId activo del usuario conectado. Lo buscamos localmente.
    @Override
    public Observable<Opinion> createOpinion(int hotelId, Opinion opinion) {
        User user = sessionDataStoreFactory.Local().getSession();
        return opinionDataStoreFactory.Remote().createOpinion(user, hotelId, opinion);
    }

    //Creamos una puntuación en un hotel, de forma remota. Para ello necesitamos obtener el tokenId activo del usuario conectado. Lo buscamos localmente.
    @Override
    public Observable<Rating> createRating(int hotelId, int rating) {
        User user = sessionDataStoreFactory.Local().getSession();
        return opinionDataStoreFactory.Remote().createRating(user, hotelId, rating);
    }
}

