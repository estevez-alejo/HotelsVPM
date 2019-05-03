package com.alejoestevez.hotelsmvp.mvp.presenter;

import android.content.Context;

import com.alejoestevez.hotelsmvp.domain.interactor.LogInEmailUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.SignUpEmailAccountUseCase;
import com.alejoestevez.hotelsmvp.domain.interactor.parameters.EmailParameters;
import com.alejoestevez.hotelsmvp.R;
import com.alejoestevez.hotelsmvp.mvp.view.IEmailLoginView;
import com.alejoestevez.hotelsmvp.observer.SignInObserver;

import javax.inject.Inject;

//Presentador para la activity de login con email.
public class EmailLoginPresenter implements IPresenter {

    IEmailLoginView iEmailLoginView;
    private Context context;

    private LogInEmailUseCase logInEmailUseCase;
    private SignUpEmailAccountUseCase signUpEmailAccountUseCase;

    @Inject
    public EmailLoginPresenter(IEmailLoginView iEmailLoginView,
                               LogInEmailUseCase logInEmailUseCase,
                               SignUpEmailAccountUseCase signUpEmailAccountUseCase
    ) {
        this.iEmailLoginView = iEmailLoginView;
        this.logInEmailUseCase = logInEmailUseCase;
        this.signUpEmailAccountUseCase = signUpEmailAccountUseCase;
    }

    public void initialize(Context context) {
        this.context = context;
    }

    //Hacemos login con el email y contraseña proporcionado
    public void loginWithEmailAndPassword(String email, String password) {
        if (isValidEmail(email) && isValidPassword(password)) {
            logInEmailUseCase.implementUseCase(
                    new SignInObserver(iEmailLoginView, context),
                    EmailParameters.Parameters.Create(email, password));
        }
    }

    //Registro de nueva cuenta de email y contraseña
    public void registerWithEmailAndPassword(String email, String password) {
        if (isValidEmail(email) && isValidPassword(password)) {
            signUpEmailAccountUseCase.implementUseCase(
                    new SignInObserver(iEmailLoginView, context),
                    EmailParameters.Parameters.Create(email, password));
        }
    }

    //Función para determinar si el email es válido o no
    private boolean isValidEmail(String email) {
        if (email == null)
            iEmailLoginView.showEmailMessageError(context.getString(R.string.must_fill_email));
        else if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            iEmailLoginView.showEmailNoErrors();
            return true;
        } else
            iEmailLoginView.showEmailMessageError(context.getString(R.string.not_valid_email));
        return false;
    }

    //Función para determinar si la contrasñea es válida o no
    private boolean isValidPassword(String password) {
        if (password == null)
            iEmailLoginView.showPasswordMessageError(context.getString(R.string.must_fill_password));
        else if (password.length() < 6)
            iEmailLoginView.showPasswordMessageError(context.getString(R.string.password_wrong_length));
        else {
            iEmailLoginView.showPasswordNoErrors();
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {
        if (logInEmailUseCase != null) logInEmailUseCase.cancelSubscription();
        if (signUpEmailAccountUseCase != null) signUpEmailAccountUseCase.cancelSubscription();
    }

}

