package com.alejoestevez.hotelsmvp.data.datastore.remote;

import android.content.Context;

import com.alejoestevez.hotelsmvp.data.R;
import com.alejoestevez.hotelsmvp.data.datastore.IOpinionDataStore;
import com.alejoestevez.hotelsmvp.data.service.IOpinionService;
import com.alejoestevez.hotelsmvp.data.utils.Utilities;
import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.domain.model.Rating;
import com.alejoestevez.hotelsmvp.domain.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteOpinionDataStore implements IOpinionDataStore {
    private Retrofit retrofit;
    private IOpinionService iOpinionService;
    private Context context;

    public RemoteOpinionDataStore(final Context context) {

        this.context = context;
        //La factoria de conversión de JSON, establecemos a que formato debe convertir las fechas, así como aceptar serialización de valores nulos.
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .serializeNulls()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.apiBaseURL))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(Utilities.initializeHttpClient(context).build())
                .build();

        iOpinionService = retrofit.create(IOpinionService.class);
    }

    @Override
    public Observable<Map<String, Opinion>> getOpinions(int hotelId) {
        return Observable.create(emitter -> {
            try {
                //Invocamos al método del servicio, encargado de obtener los comentarios en un hotel.
                Call<Map<String, Opinion>> call = iOpinionService.getOpinions(hotelId);
                //Ejecutamos la llamada para obtener la respuesta.
                Response<Map<String, Opinion>> response = call.execute();

                if (response.code() == 200) {
                    Map<String, Opinion> opinions = response.body();

                    emitter.onNext(opinions);
                    emitter.onComplete();
                } else
                    emitter.onError(new Exception(response.errorBody().toString()));

            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Observable<Opinion> createOpinion(User user, int hotelId, Opinion opinion) {
        return Observable.create(emitter -> {
            try {
                //Invocamos al método del servicio, responsable de crear opiniones en un hotel.
                Call<Opinion> call = iOpinionService.createOpinion(hotelId, user.getUserId(), user.getAuthToken(), opinion);
                //Ejecutamos la llamada para obtener la respuesta con la opinion creada.
                Response<Opinion> response = call.execute();

                if (response.code() == 200) {
                    Opinion opinions = response.body();

                    emitter.onNext(opinions);
                    emitter.onComplete();
                } else
                    emitter.onError(new Exception(response.errorBody().toString()));


            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Observable<Rating> createRating(User user, int hotelId, int rating) {
        return Observable.create(emitter -> {
            try {
                //Invocamos al método del servicio, responsable de crear una puntuación en un hotel.
                Call<Rating> call = iOpinionService.createRating(hotelId, user.getUserId(), user.getAuthToken(), rating);
                //Ejecutamos la llamada para obtener la respuesta.
                Response<Rating> response = call.execute();

                if (response.code() == 200) {
                    emitter.onNext(response.body());
                    emitter.onComplete();
                } else
                    emitter.onError(new Exception(response.errorBody().toString()));


            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

}

