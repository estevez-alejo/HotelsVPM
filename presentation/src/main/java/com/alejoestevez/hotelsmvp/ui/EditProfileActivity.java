package com.alejoestevez.hotelsmvp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.R;
import com.alejoestevez.hotelsmvp.mvp.presenter.EditProfilePresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IEditProfileView;
import com.alejoestevez.hotelsmvp.ui.constants.Constants;
import com.alejoestevez.hotelsmvp.ui.constants.Flags;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class EditProfileActivity extends AppCompatActivity implements IEditProfileView {

    private User currentUser;

    @BindView(R.id.editprofile_progress)
    ProgressBar editprofile_progress;
    @BindView(R.id.editprofile_image_button)
    TextView editprofile_image_button;
    @BindView(R.id.editprofile_email)
    TextView editprofile_email;
    @BindView(R.id.editprofile_image)
    ImageView editprofile_image;
    @BindView(R.id.editprofile_name)
    EditText editprofile_name;

    @OnClick(R.id.editprofile_image_button)
    public void doUploadProfileImage() {
        //Lanzamos un Intent para seleccionar la imagen a subir.
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, Flags.FILE_SELECTOR);
    }

    @Inject
    EditProfilePresenter editProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Inicializamos ButterKnife
        ButterKnife.bind(this);

        //Pintamos la información del usuario.
        renderUser();
    }

    private void renderUser() {
        //Obtenemos el intent que nos ha enviado la activity del perfil.
        Intent intent = getIntent();

        //Deserializamos el JSON recibido con el objeto usuario serializado.
        Gson gson = new Gson();
        String currentUserJson = intent.getExtras().getString(Constants.ExtraCurrentUser);
        byte[] data = intent.getExtras().getByteArray(Constants.ExtraCurrentProfileImage);

        currentUser = gson.fromJson(currentUserJson, User.class);

        //Rellenamos la información recibida.
        editprofile_email.setText(currentUser.getEmail());
        editprofile_name.setText(currentUser.getName());

        //Segun los bytes recibidos, los decodificamos y obtenemos un bitmap que se lo asignaremos al ImageView de la vista.
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        if (bitmap != null) editprofile_image.setImageBitmap(bitmap);
    }

    //Cargamos el menú con el icono de guardado.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    //Obtenemos el elemento del menú seleccionado, en nuestro caso, el icono de guardar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_save) {

            currentUser.setName(editprofile_name.getText().toString());
            currentUser.setEmail(editprofile_email.getText().toString());

            //Invocamos al método de guardar del presentador para actualizar el perfil remótamente.
            editProfilePresenter.saveProfile(currentUser);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Capturamos el Intent
        if (requestCode == Flags.FILE_SELECTOR && resultCode == RESULT_OK) {
            Uri selectedimage = data.getData();
            try {
                //Tras seleccionar la imagen de la biblioteca, obtenemos la Uri y se lo asignamos al ImageView.
                editprofile_image.setImageBitmap(
                        MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimage)
                );

                //Obtenemos los datos del ImageView, como bytes
                editprofile_image.setDrawingCacheEnabled(true);
                editprofile_image.buildDrawingCache();

                //Ahora vamos a obtener el array de bytes de la imagen y vamos a subirla al Firebase Storage.
                Bitmap bitmap = editprofile_image.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //Comprimimos el bitmap a la máxima calidad va de 0 - 100, así que seleccionamos 100.
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                //Obtenemos los bytes de la imagen
                byte[] data2 = baos.toByteArray();

                //La subimos al repositorio.
                editProfilePresenter.uploadProfilePhoto(data2);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void showLoading() {
        editprofile_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        editprofile_progress.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    //Tras subir la foto actualizamos la url de la imagen remota, en la clase usuario.
    @Override
    public void photoUploaded(String value) {
        currentUser.setPhotoUrl(value);
    }

    //Tras actualizar el perfil del usuario, cerramos la activity.
    @Override
    public void updatedUser(User value) {
        finish();
    }
    //Cancelamos las subscripciones de los observadores del presentador.
    @Override
    public void onStop() {
        super.onStop();
        editProfilePresenter.destroy();
    }
}
