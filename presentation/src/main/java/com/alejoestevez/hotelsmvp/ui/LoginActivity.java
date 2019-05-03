package com.alejoestevez.hotelsmvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.R;
import com.alejoestevez.hotelsmvp.mvp.presenter.LoginPresenter;
import com.alejoestevez.hotelsmvp.mvp.view.ILoginView;
import com.alejoestevez.hotelsmvp.ui.constants.Constants;
import com.alejoestevez.hotelsmvp.ui.constants.Flags;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class LoginActivity extends AppCompatActivity implements ILoginView {


    private static final String TAG = "LOGIN";
    @BindView(R.id.login_progress)
    ProgressBar login_progress;
    @BindView(R.id.login_signInFacebook_btn)
    Button login_signInFacebook_btn;
    @BindView(R.id.login_signInGoogle_btn)
    Button login_signInGoogle_btn;
    @BindView(R.id.login_email_btn)
    Button login_email_btn;
    @BindView(R.id.login_register_btn)
    Button login_register_btn;
    @BindView(R.id.login_signOut_btn)
    Button login_signOut_btn;

    //Capturamos el click del boton de cierre de sesión
    @OnClick(R.id.login_signOut_btn)
    protected void doSignOut() {
        loginPresenter.signOut();
    }

    //Capturamos el click del boton Login para hacer login usando el método por email y contraseña.
    @OnClick(R.id.login_email_btn)
    protected void doEmailLogin() {
        Intent intent = new Intent(this, EmailLoginActivity.class);
        //Indicamos que la activity de EmailLoginActivity, se represente en modo login.
        intent.putExtra(Constants.IntentEmailLoginMode, Constants.EmailLoginMode.Login);
        startActivityForResult(intent, Flags.EMAIL_LOGIN);
    }

    //Capturamos el click del boton Crear cuenta para hacer registro de nuevo usuario, usando el método por email y contraseña.
    @OnClick(R.id.login_register_btn)
    protected void doEmailRegister(View view) {
        Intent intent = new Intent(this, EmailLoginActivity.class);
        //Indicamos que la activity de EmailLoginActivity, se represente en modo registro de usuario.
        intent.putExtra(Constants.IntentEmailLoginMode, Constants.EmailLoginMode.Register);
        startActivityForResult(intent, Flags.EMAIL_SIGN_IN);
    }

    //Capturamos el click del boton de Login con Google, para hacer login a través de Google.
    @OnClick(R.id.login_signInGoogle_btn)
    protected void doGoogleSignIn() {
        Intent intentGoogle = loginPresenter.googleSignIn();
        startActivityForResult(intentGoogle, Flags.GOOGLE_SIGN_IN);
    }

    //Capturamos el click del boton de Login con Facebook, para hacer login a través de Facebook.
    @OnClick(R.id.login_signInFacebook_btn)
    protected void doFacebookSignIn() {
        //Obtenemos la instancia del LoginManager de Facebook, indicando que queremos recibir información sobre email y perfil público, para que nos de acceso de lectura y escritura.
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        //Asociamos un callbackManeger a la instancia del LoginManager de Facebook, de modo que en caso de éxito, manejaremos la información recibida.
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginPresenter.handleFacebookResult(loginResult);
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, error.toString());
                    }
                });
    }

    //CallbackManager de Facebook
    @Inject
    CallbackManager callbackManager;

    @Inject
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Inyección de la Activity.
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        /********Full Screen****************/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /************************/

        setContentView(R.layout.activity_login);

        //Inicializamos ButterKnife
        ButterKnife.bind(this);

        //Inicializamos el presentador, con el contexto de nuestra activity.
        loginPresenter.initialize(this);

    }

    //Cuando pulsemos el botón de atras de la toolbar, queremos que limpie el historial de activity de la pila y abra la activity Home.
    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.hasExtra(Constants.IntentProfileDenied)) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            //Para limpiar la pila del historial de las activity.
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    //Mostramos información del usuario que ha iniciado sesión.
    @Override
    public void updateUI(User user) {
        Toast.makeText(this, "Bienvenido:" + user.getName() + ".Conectado desde:" + user.getProvider(), Toast.LENGTH_LONG).show();
    }

    //Mostramos los botones de inicio de sesión, cuando la sesión ha sido cerrada.
    @Override
    public void onSignedOut() {

        login_email_btn.setVisibility(View.VISIBLE);
        login_register_btn.setVisibility(View.VISIBLE);
        login_signInFacebook_btn.setVisibility(View.VISIBLE);
        login_signInGoogle_btn.setVisibility(View.VISIBLE);
        login_signOut_btn.setVisibility(View.GONE);

        Toast.makeText(this, "Adios!", Toast.LENGTH_LONG).show();
    }

    //Ocultamos los botones de inicio de sesión, tras iniciar sesión.
    @Override
    public void onSignedIn() {
        login_email_btn.setVisibility(View.GONE);
        login_register_btn.setVisibility(View.GONE);
        login_signInFacebook_btn.setVisibility(View.GONE);
        login_signInGoogle_btn.setVisibility(View.GONE);
        login_signOut_btn.setVisibility(View.VISIBLE);

        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Capturamos el Intent
        if (requestCode == Flags.GOOGLE_SIGN_IN)//Si recibimos el intent de Google, manejaremos la respuesta a través de Google
            loginPresenter.handleGoogleResult(data);
        else if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode())//Si recibimos el intent de Facebook, manejaremos la respuesta a través de Facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        else if (requestCode == Flags.EMAIL_LOGIN || requestCode == Flags.EMAIL_SIGN_IN)//Si recibimos el intent de login con email, miramos si tenemos sesión iniciada.
            loginPresenter.isSignedIn();
    }

    @Override
    public void showLoading() {
        login_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        login_progress.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        loginPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        loginPresenter.destroy();
    }
}
