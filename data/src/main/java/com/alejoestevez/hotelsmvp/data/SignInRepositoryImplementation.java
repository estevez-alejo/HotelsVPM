package com.alejoestevez.hotelsmvp.data;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alejoestevez.hotelsmvp.data.factory.SessionDataStoreFactory;
import com.alejoestevez.hotelsmvp.data.mapper.FirebaseUserToUser;
import com.alejoestevez.hotelsmvp.domain.model.SessionProvider;
import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.domain.repositories.ISignInRepository;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

//Clase que implementa la interfaz del repositorio del diccionario, definida en el dominio.
@Singleton
public class SignInRepositoryImplementation implements ISignInRepository {

    private static final String TAG = "SIGNIN";

    //Factoría del almacén de datos para seleccionar entre Local o Remoto.
    private SessionDataStoreFactory sessionDataStoreFactory;

    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private Context context;

    @Inject
    public SignInRepositoryImplementation(Context context,
                                          SessionDataStoreFactory sessionDataStoreFactory,
                                          GoogleApiClient googleApiClient,
                                          FirebaseAuth firebaseAuth) {
        this.context = context;

        this.sessionDataStoreFactory = sessionDataStoreFactory;
        this.googleApiClient = googleApiClient;

        this.firebaseAuth = firebaseAuth;
    }
    //Comprobar sesion abierta
    @Override
    public Observable<User> isSessionOpen() {
        return Observable
                .create(emitter -> {
                    try {
                        //Si tenemos un usuario, lo construimos apropiadamente y buscaremos su tokenId.
                        if (firebaseAuth.getCurrentUser() == null)
                            emitter.onError(new Exception(context.getString(R.string.there_is_not_active_user)));
                        else
                            getTokenOnSuccessFulSignIn(emitter);

                    } catch (Exception e) {
                        //Indicamos que se ha producido un error.
                        emitter.onError(e);
                    }
                });
    }

    //Login mediante Google o Facebook
    @Override
    public Observable<User> signIn(String provider, String accountIdToken) {

        return Observable
                .create(emitter -> {
                    try {
                        AuthCredential credential = null;
                        //Si el proveedor es Google, obtenemos las credenciales de Google usando su proveedor.
                        if (provider.equals(SessionProvider.GOOGLE))
                            credential = GoogleAuthProvider.getCredential(accountIdToken, null);
                        else if (provider.equals(SessionProvider.FACEBOOK))//En caso de que su proveedor sea facebook, utilizaremos su proveedor correspondiente.
                            credential = FacebookAuthProvider.getCredential(accountIdToken);

                        //Abrimos sesión en firebase, mediante las credenciales obtenidas por una de los dos tipos de proveedor que aceptamos.
                        firebaseAuth.signInWithCredential(credential)
                                .addOnCompleteListener(
                                        task -> {
                                            if (task.isSuccessful())
                                                getTokenOnSuccessFulSignIn(emitter);
                                            else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "signInWithCredential:failure", task.getException());

                                                emitter.onError(task.getException());
                                            }
                                        });


                    } catch (Exception e) {
                        //Indicamos que se ha producido un error.
                        emitter.onError(e);
                    }
                });

    }

    //Login mediante email y contraseña
    @Override
    public Observable<User> logInWithEmail(String email, String password) {
        return Observable
                .create(emitter -> {
                    try {
                        //Iniciamos sesión mediante email y contraseña
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(
                                        task -> {
                                            if (task.isSuccessful())
                                                getTokenOnSuccessFulSignIn(emitter);
                                            else emitter.onError(task.getException());
                                        });
                    } catch (Exception e) {
                        //Indicamos que se ha producido un error.
                        emitter.onError(e);
                    }

                });
    }

    //Registro de usuario mediante email y contraseña
    @Override
    public Observable<User> signUpAccountWithEmail(String email, String password) {
        return Observable
                .create(emitter -> {
                    try {
                        //Creamos usuario nuevo mediante email y contraseña
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(
                                        task -> {
                                            if (task.isSuccessful())
                                                getTokenOnSuccessFulSignIn(emitter);
                                            else emitter.onError(task.getException());
                                        });
                    } catch (Exception e) {
                        //Indicamos que se ha producido un error.
                        emitter.onError(e);
                    }
                });
    }

    //Logout
    @Override
    public Observable signOut() {
        return Observable
                .create(emitter -> {
                    try {
                        //Hacemos logout de firebase
                        firebaseAuth.signOut();
                        //Hacemos Logout de Faceboook
                        LoginManager.getInstance().logOut();
                        //Hacemos Logout de Google
                        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(@Nullable Bundle bundle) {
                                Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                                        status -> {
                                            googleApiClient.disconnect();
                                            googleApiClient.unregisterConnectionCallbacks(this);
                                            emitter.onComplete();
                                        });
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                            }

                        });
                        googleApiClient.connect();

                    } catch (Exception e) {
                        //Indicamos que se ha producido un error.
                        emitter.onError(e);
                    }
                });
    }

    //Retornar el usuario con el token de autenticación.
    private void getTokenOnSuccessFulSignIn(ObservableEmitter<User> emitter) {
        // Sign in success, Actualizar interfaz con información del usuario logueado.
        User user = FirebaseUserToUser.Create(firebaseAuth.getCurrentUser());

        //FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //Obtenemos el usuario de la sesión de firebase y obtenemos su token, sin la necesidad de forzar el refresco de token.
        firebaseAuth.getCurrentUser().getIdToken(false)
                .addOnCompleteListener(result -> {
                    //Una vez obtenido el token de usuario, lo asignamos a la instancia usuario.
                    user.setAuthToken(result.getResult().getToken());

                    //Si se ha guardado correctamente localmente el usuario, lo retornamos.
                    if (sessionDataStoreFactory.Local().saveSession(user)) {
                        emitter.onNext(user);
                        emitter.onComplete();
                    } else {
                        emitter.onError(new Exception(context.getString(R.string.error_save_user_local)));
                    }

                    Log.d(TAG, "Token Firebase User:" + result.getResult().getToken());
                });
    }
}
