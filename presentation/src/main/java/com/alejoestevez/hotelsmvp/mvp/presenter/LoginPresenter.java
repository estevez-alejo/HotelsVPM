package com.alejoestevez.hotelsmvp.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alejoestevez.hotelsmvp.domain.interactor.CheckIsUserSignedInUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.SignInUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.SignOutUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.parameters.SignInParameters;
import com.alejoestevez.hotelsmvp.domain.model.SessionProvider;
import com.alejoestevez.hotelsmvp.mvp.view.ILoginView;
import com.alejoestevez.hotelsmvp.observer.SignInObserver;
import com.alejoestevez.hotelsmvp.observer.SignOutObserver;
import com.alejoestevez.hotelsmvp.observer.SignedInObserver;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

public class LoginPresenter implements IPresenter {
    private static final String TAG = "LOGIN";

    ILoginView iLoginView;
    private GoogleApiClient googleApiClient;

    private Context context;
    private SignInUseCase signInUseCase;
    private SignOutUseCase signOutUseCase;
    private CheckIsUserSignedInUseCase checkIsUserSignedInUseCase;

    @Inject
    public LoginPresenter(ILoginView iLoginView,
                          SignInUseCase signInUseCase,
                          SignOutUseCase signOutUseCase,
                          CheckIsUserSignedInUseCase checkIsUserSignedInUseCase,
                          GoogleApiClient googleApiClient) {
        this.iLoginView = iLoginView;
        this.signInUseCase = signInUseCase;
        this.signOutUseCase = signOutUseCase;
        this.checkIsUserSignedInUseCase = checkIsUserSignedInUseCase;
        this.googleApiClient = googleApiClient;
    }

    //Inicializamos el presentador y buscamos si hay alguna sesión abierta.
    public void initialize(Context context) {
        this.context = context;
        isSignedIn();
    }

    //Miramos si hay abierta una sesión
    public void isSignedIn() {
        checkIsUserSignedInUseCase.implementUseCase(new SignedInObserver(iLoginView), null);
    }

    //Hacer login con Google, obteniendo el intent con la api del cliente de google.
    public Intent googleSignIn() {
        iLoginView.showLoading();
        return Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    }

    //Manejar el resultado del intent de google para obtener los credenciales de acceso.
    public void handleGoogleResult(Intent data) {

        GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        //Si hemos tenido éxito al loguearnos en Google
        if (googleSignInResult.isSuccess()) {
            //Obtenemos la cuenta de google.
            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

            //Autenticar usuario en Firebase.
            Log.d(TAG, "FirebaseAuthWithGoogle Id:" + googleSignInAccount.getId());
            Log.d(TAG, "IdToken:" + googleSignInAccount.getIdToken());

            //Obtenemos el IdToken de Google que nos servirá para el SignIn de Firebase.
            String accountIdToken = googleSignInAccount.getIdToken();
            signInUseCase.implementUseCase(
                    new SignInObserver(iLoginView, context),
                    SignInParameters.Parameters.Create(SessionProvider.GOOGLE, accountIdToken));
        }
    }

    //Manejamos el resultado del callbackManager de Facebook.
    public void handleFacebookResult(LoginResult loginResult) {
        iLoginView.showLoading();
        //Obtenemos el Token de acceso de Facebook y lo usaremos en firebase en capas superiores.
        String accessToken = loginResult.getAccessToken().getToken();
        signInUseCase.implementUseCase(
                new SignInObserver(iLoginView, context),
                SignInParameters.Parameters.Create(SessionProvider.FACEBOOK, accessToken));
    }

    //En caso de que la api de google, esté desconectada, la reconectamos de esta forma.
    public void start() {
        if (googleApiClient != null && !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    //Cerrar sesión.
    public void signOut() {
        signOutUseCase.implementUseCase(new SignOutObserver(iLoginView), null);
    }

    @Override
    public void destroy() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        if (signInUseCase != null) signInUseCase.cancelSubscription();
        if (signOutUseCase != null) signOutUseCase.cancelSubscription();
        if (checkIsUserSignedInUseCase != null) checkIsUserSignedInUseCase.cancelSubscription();
    }


}
