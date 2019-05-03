package com.alejoestevez.hotelsmvp.data.datastore.remote;

import android.content.Context;

import com.alejoestevez.hotelsmvp.data.R;
import com.alejoestevez.hotelsmvp.data.datastore.ISessionDataStore;
import com.alejoestevez.hotelsmvp.data.service.ISessionService;
import com.alejoestevez.hotelsmvp.data.utils.Utilities;
import com.alejoestevez.hotelsmvp.domain.model.User;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteSessionDataStore implements ISessionDataStore {

    private Retrofit retrofit;
    private ISessionService iSessionService;
    private Context context;

    public RemoteSessionDataStore(final Context context) {

        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.apiBaseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .client(Utilities.initializeHttpClient(context).build())
                .build();

        iSessionService = retrofit.create(ISessionService.class);
    }

    @Override
    public boolean saveSession(User user) {
        return false;
    }

    @Override
    public User getSession() {
        return null;
    }


    @Override
    public Observable<String> getPublicProfile(String uid) {
        return Observable.create(emitter -> {
            try {
                //Invocamos al método del servicio, responsable de obtener el perfil público del usuario.
                Call call = iSessionService.getPublicProfile(uid);
                //Ejecutamos la llamada para obtener el nombre del perfil público de un usuario.
                Response<String> response = call.execute();

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

    @Override
    public Observable<User> savePublicProfile(User user) {
        return Observable.
                create(emitter -> {
                    try {
                        //Invocamos al método del servicio, responsable de crear el perfil público de un usuario.
                        Call<String> call = iSessionService.createPublicProfile(user.getUserId(),
                                user.getAuthToken(),
                                user.getName());
                        //Ejecutamos la llamada para obtener por respuesta, el nombre del perfil público del usuario.
                        Response<String> response = call.execute();

                        if (response.code() == 200) {

                            emitter.onNext(user);
                            emitter.onComplete();
                        } else
                            emitter.onError(new Exception(response.errorBody().toString()));

                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                });
    }
}

