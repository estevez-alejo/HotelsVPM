package com.alejoestevez.hotelsmvp.ui;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.alejoestevez.hotelsmvp.domain.factories.Factory;
import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.domain.model.Rating;
import com.alejoestevez.hotelsmvp.R;
import com.alejoestevez.hotelsmvp.mvp.presenter.OpinionPresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IOpinionView;
import com.alejoestevez.hotelsmvp.ui.constants.Constants;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class OpinionActivity extends AppCompatActivity implements IOpinionView {

    @BindView(R.id.opinion_rating)
    RatingBar opinion_rating;
    @BindView(R.id.opinion_message)
    EditText opinion_message;
    @BindView(R.id.opinion_progress)
    ProgressBar opinion_progress;

    private int hotelId;

    @Inject
    OpinionPresenter opinionPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Inyección de la Activity.
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion);

        //Inicializamos ButterKnife
        ButterKnife.bind(this);

        //Obtenemos el identificador de hotel que nos pasan de la activity anterior por los extra.
        hotelId = getIntent().getExtras().getInt(Constants.ExtraCurrentHotelId);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    //Obtenemos el elemento del menú seleccionado, en nuestro caso, el icono de borrado.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_save) {//Al guardar una nueva opinión, utilizamos el constructor de opiniones y creamos una nueva instancia.
            Opinion opinion = Factory.OpinionFactory.Create(Math.round(opinion_rating.getRating())
                    , opinion_message.getText().toString()
                    , new Date()
            );
            //Guardamos la opinion remotamente.
            opinionPresenter.createOpinion(hotelId, opinion);

            return true;
        } else if (id == android.R.id.home) {
            //Cuando pulsamos el botón atras de la toolbar, cerramos la activity actual.
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {
        opinion_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        opinion_progress.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //Tras guardar la opinión, volvemos a la activity de opiniones.
    @Override
    public void opinionCreated(Opinion opinion) {
        onBackPressed();
    }

    //Tras guardar la puntuación, volvemos a la activity de opiniones.
    @Override
    public void ratingCreated(Rating value) {
        onBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
        opinionPresenter.destroy();
    }
}
