package com.alejoestevez.hotelsmvp.data.datastore.remote;

import android.content.Context;

import com.alejoestevez.hotelsmvp.data.R;
import com.alejoestevez.hotelsmvp.data.datastore.IHotelDataStore;
import com.alejoestevez.hotelsmvp.data.service.IHotelService;
import com.alejoestevez.hotelsmvp.data.utils.Utilities;
import com.alejoestevez.hotelsmvp.domain.model.Hotel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteHotelDataStore implements IHotelDataStore {
    private Retrofit retrofit;
    private IHotelService iHotelService;
    private Context context;

    public RemoteHotelDataStore(final Context context) {

        this.context = context;

        retrofit = new Retrofit.Builder()//Preparamos el constructor de Retrofit
                .baseUrl(context.getResources().getString(R.string.apiBaseURL))//Establecemos la  baseUrl de la api remota.
                .addConverterFactory(GsonConverterFactory.create())//Añadimos una factoría de conversión de JSON.
                .client(Utilities.initializeHttpClient(context).build())//Inicializamos el cliente Http
                .build();

        iHotelService = retrofit.create(IHotelService.class);//Los servicios que utilizará retrofit estan definidos en la interfaz IHotelService. Aqui estan todas las llamadas.
    }

    @Override
    public Observable<List<Hotel>> getHotels() {
        return Observable.create(emitter -> {
            try {
                //Invocamos la llamada al servicio, responsable de obtener los hoteles.
                Call<List<Hotel>> call = iHotelService.getHotels();
                //Ejecutamos la llamada mediante execute
                Response<List<Hotel>> response = call.execute();

                //Si la respuesta tiene el codigo http 200, significa que ha ido bien.
                if (response.code() == 200) {
                    //Por cada hotel recibido, construimos su URL de la imagen,
                    // dado que hemos configurado que únicamente nos descargue información de parte de la ruta
                    // pero la baseURL que se repite, no la queremos descargar y asi ahorramos datos.
                    List<Hotel> hotels = response.body();
                    List<Hotel> hotelsResponse = new ArrayList<>();
                    for (Hotel hotel : hotels) {
                        if (hotel != null) {
                            Hotel hotelRes = hotel;
                            hotelRes.setPhotoUrl(context.getString(R.string.dataStorageBaseURL) + hotel.getPhotoUrl());
                            hotelsResponse.add(hotelRes);
                        }
                    }
                    emitter.onNext(hotelsResponse);
                    emitter.onComplete();
                } else
                    emitter.onError(new Exception(response.errorBody().toString()));

            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Observable<Double> getRating(int hotelId) {
        return Observable.create(emitter -> {
            try {
                //Invocamos el método del servicio, responsable de obtener la puntuación según el identificador de hotel asignado.
                Call call = iHotelService.getRating(hotelId);
                //Ejecutamos la llamada
                Response<Map<String, Integer>> response = call.execute();
                //Si la respuesta es un HTTP 200, es que ha ido bien y por tanto calcularemos la media de las puntuaciones de cada usuario que haya valorado.
                double ratingValue = 0;
                if (response.code() == 200) {
                    Map<String, Integer> ratings = response.body();

                    if (ratings != null) {
                        for (String key : ratings.keySet())
                            ratingValue += ratings.get(key).intValue();
                        ratingValue = ratingValue / ratings.size();
                    }

                    emitter.onNext(ratingValue);
                    emitter.onComplete();
                } else
                    emitter.onError(new Exception(response.errorBody().toString()));


            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }


}
