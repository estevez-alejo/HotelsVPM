package com.alejoestevez.hotelsmvp.observer;

import android.content.Context;

import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.R;
import com.alejoestevez.hotelsmvp.mvp.view.ISessionView;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import io.reactivex.observers.DisposableObserver;

//Observador para controlar que un usuario ha iniciado sesión en el sistema
public class SignInObserver extends DisposableObserver<User> {

    private ISessionView iSessionView;
    private Context context;

    public SignInObserver(ISessionView iSessionView, Context context)
    {
        this.iSessionView=iSessionView;
        this.context=context;
    }

    //En caso de retornar un valor, entrará por aqui.
    @Override
    public void onNext(User value) {
        iSessionView.updateUI(value);
        iSessionView.onSignedIn();
    }

    //En caso de error, entrará por aqui.
    @Override
    public void onError(Throwable e) {
        iSessionView.hideLoading();

        if(e instanceof FirebaseAuthInvalidCredentialsException)
            iSessionView.showMessage(context.getString(R.string.error_invalid_credentials));
        else if(e instanceof FirebaseAuthUserCollisionException)
            iSessionView.showMessage(context.getString(R.string.error_user_already_exists));
        else if(e instanceof FirebaseAuthRecentLoginRequiredException)
            iSessionView.showMessage(context.getString(R.string.error_need_authenticate_again));
        else if(e instanceof FirebaseAuthInvalidUserException)
            iSessionView.showMessage(context.getString(R.string.error_user_not_found));
        else
            iSessionView.showMessage(e.getMessage());

    }

    //En caso de finalizar la tarea, entrará por aqui.
    @Override
    public void onComplete() {
        iSessionView.hideLoading();
    }
}
