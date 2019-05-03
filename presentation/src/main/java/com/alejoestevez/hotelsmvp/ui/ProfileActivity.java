package com.alejoestevez.hotelsmvp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alejoestevez.hotelsmvp.domain.model.SessionProvider;
import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.R;
import com.alejoestevez.hotelsmvp.mvp.presenter.ProfilePresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IProfileView;
import com.alejoestevez.hotelsmvp.ui.constants.Constants;
import com.alejoestevez.hotelsmvp.ui.constants.Flags;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

import static com.alejoestevez.hotelsmvp.ui.constants.Flags.REQUESTSESSION;

public class ProfileActivity extends AppCompatActivity implements IProfileView {

    private User currentUser;

    @BindView(R.id.profile_signedwith_email)
    CardView profile_signedwith_email;
    @BindView(R.id.profile_signedwith_facebook)
    CardView profile_signedwith_facebook;
    @BindView(R.id.profile_signedwith_google)
    CardView profile_signedwith_google;
    @BindView(R.id.profile_progress)
    ProgressBar profile_progress;

    @BindView(R.id.profile_close_session)
    Button profile_close_session;
    @BindView(R.id.profile_edit)
    Button profile_edit;
    @BindView(R.id.profile_image)
    ImageView profile_image;
    @BindView(R.id.profile_name)
    TextView profile_name;
    @BindView(R.id.profile_email)
    TextView profile_email;

    @OnClick(R.id.profile_edit)
    public void doEdit() {
        Intent intent = new Intent(this, EditProfileActivity.class);

        Type userType = new TypeToken<User>() {
        }.getType();
        Gson gson = new Gson();
        String currentUserJson = gson.toJson(currentUser, userType);

        intent.putExtra(Constants.ExtraCurrentUser, currentUserJson);

        Bitmap bitmap = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
        //Convert to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        intent.putExtra(Constants.ExtraCurrentProfileImage, byteArray);

        startActivityForResult(intent, Flags.REQUEST_EDIT_PROFILE);
    }

    @OnClick(R.id.profile_close_session)
    public void doCloseSession() {
        profilePresenter.signOut();
    }

    @Inject
    ProfilePresenter profilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Inyección de la Activity.
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Inicializamos ButterKnife
        ButterKnife.bind(this);

        profilePresenter.isSignedIn();
    }

    @Override
    public void showLoading() {
        profile_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        profile_progress.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    //Mostramos por pantalla la información del perfil del usuario.
    @Override
    public void updateUI(User user) {
        currentUser = user;
        profile_email.setText(user.getEmail());

        //Buscamos la imagen del usuario segun su URL y tras obtener la información, lo asignaremos al recurso ImageView indicado.
        profilePresenter.loadImageUrl(user.getPhotoUrl(), profile_image.getId());

        profile_name.setText(user.getName());
        //Ocultamos los tipos de inicio de sesión.
        profile_signedwith_email.setVisibility(View.GONE);
        profile_signedwith_facebook.setVisibility(View.GONE);
        profile_signedwith_google.setVisibility(View.GONE);

        //Mostramos el tipo de sesión que tenemos abierta.
        switch (user.getProvider()) {
            case SessionProvider.EMAIL:
                profile_signedwith_email.setVisibility(View.VISIBLE);
                break;
            case SessionProvider.FACEBOOK:
                profile_signedwith_facebook.setVisibility(View.VISIBLE);
                break;
            case SessionProvider.GOOGLE:
                profile_signedwith_google.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onSignedIn() {

    }
    //Cuando cerramos sesión, abrimos la pantalla de login para que inicie sesión.
    @Override
    public void onSignedOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.IntentProfileDenied, true);
        startActivityForResult(intent, REQUESTSESSION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Capturamos el Intent
        if (requestCode == Flags.REQUESTSESSION) {
            profilePresenter.isSignedIn();
        } else if (requestCode == Flags.REQUEST_EDIT_PROFILE) {
            profilePresenter.isSignedIn();
        }

    }
    //Cuando recibimos los bytes de la imagen, lo pintamos en el recurso.
    @Override
    public void photoLoaded(byte[] data, int resourceId) {
        if (data != null) {
            //Decodificamos el array de bytes de la imagen, obteniendo un Bitmap y se lo asignamos al ImageView.
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            if (bitmap != null)
                ((ImageView) findViewById(resourceId)).setImageBitmap(bitmap);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        profilePresenter.destroy();
    }

}
