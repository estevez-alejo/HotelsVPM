package com.alejoestevez.hotelsmvp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.R;
import com.alejoestevez.hotelsmvp.mvp.presenter.EmailLoginPresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IEmailLoginView;
import com.alejoestevez.hotelsmvp.ui.constants.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class EmailLoginActivity extends AppCompatActivity implements IEmailLoginView {

    @BindView(R.id.emailLogin_toolbar)
    android.support.v7.widget.Toolbar emailLogin_toolbar;
    @BindView(R.id.emailLogin_email_error)
    TextView emailLogin_email_error;
    @BindView(R.id.emailLogin_password_error)
    TextView emailLogin_password_error;

    @BindView(R.id.emailLogin_email)
    EditText emailLogin_email;
    @BindView(R.id.emailLogin_password)
    EditText emailLogin_password;
    @BindView(R.id.emailLogin_label_start_session)
    TextView emailLogin_label_start_session;
    @BindView(R.id.emailLogin_label_start_session2)
    TextView emailLogin_label_start_session2;
    @BindView(R.id.emailLogin_login_btn)
    Button emailLogin_login_btn;
    @BindView(R.id.login_progress)
    ProgressBar login_progress;
    @BindView(R.id.emailLogin_register_btn)
    Button emailLogin_register_btn;

    @OnClick(R.id.emailLogin_register_btn)
    public void doRegisterClick(View view) {
        clearFields();
        //Registramos el email y contraseña en Firebase para crear nueva cuenta.
        emailLoginPresenter.registerWithEmailAndPassword(emailLogin_email.getText().toString(), emailLogin_password.getText().toString());
    }

    @OnClick(R.id.emailLogin_login_btn)
    public void doLoginClick(View view) {
        clearFields();
        //Hacemos login con el email y contraseña en Firebase.
        emailLoginPresenter.loginWithEmailAndPassword(emailLogin_email.getText().toString(), emailLogin_password.getText().toString());
    }


    @Inject
    EmailLoginPresenter emailLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Inyección de la Activity.
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        /********Full Screen****************/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /************************/

        setContentView(R.layout.activity_email_login);

        //Inicializamos ButterKnife
        ButterKnife.bind(this);

        //Inicializamos el layout
        initializeLayout();

        emailLoginPresenter.initialize(this);

        setSupportActionBar(emailLogin_toolbar);
        emailLogin_toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_left));
        emailLogin_toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    //Segun el Flag recibido, sabemos que queremos realizar un Login o un registro de usuario nuevo, de modo que preparamos la interfaz para ello.
    private void initializeLayout() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras().getString(Constants.IntentEmailLoginMode) != null) {
            if (intent.getExtras().getString(Constants.IntentEmailLoginMode)
                    .equals(Constants.EmailLoginMode.Login)) {
                emailLogin_register_btn.setVisibility(View.GONE);
                emailLogin_login_btn.setVisibility(View.VISIBLE);
                emailLogin_label_start_session.setVisibility(View.VISIBLE);
                emailLogin_label_start_session2.setVisibility(View.VISIBLE);
                emailLogin_toolbar.setTitle(getString(R.string.start_session));
            } else {
                emailLogin_register_btn.setVisibility(View.VISIBLE);
                emailLogin_login_btn.setVisibility(View.GONE);
                emailLogin_label_start_session.setVisibility(View.GONE);
                emailLogin_label_start_session2.setVisibility(View.GONE);
                emailLogin_toolbar.setTitle(getString(R.string.create_account));
            }
        }
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
    public void updateUI(User user) {
        Toast.makeText(this, "Bienvenido:" + user.getEmail(), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onSignedIn() {
    }

    @Override
    public void onSignedOut() {
    }

    //En caso de error de email, mostramos un error
    @Override
    public void showEmailMessageError(String s) {
        emailLogin_email_error.setVisibility(View.VISIBLE);
        emailLogin_email_error.setText(s);
    }

    //En caso de error de contraseña, mostramos un error
    @Override
    public void showPasswordMessageError(String s) {
        emailLogin_password_error.setVisibility(View.VISIBLE);
        emailLogin_password_error.setText(s);
    }

    @Override
    public void showEmailNoErrors() {
        clearEmail();
    }

    @Override
    public void showPasswordNoErrors() {
        clearPassword();
    }

    protected void clearEmail() {
        emailLogin_email_error.setText("");
        emailLogin_email_error.setVisibility(View.GONE);
    }

    protected void clearPassword() {
        emailLogin_password_error.setText("");
        emailLogin_password_error.setVisibility(View.GONE);
    }

    protected void clearFields() {
        clearEmail();
        clearPassword();
    }

    //Cancelamos las subscripciones de los observadores del presentador.
    @Override
    public void onStop() {
        super.onStop();
        emailLoginPresenter.destroy();
    }
}
