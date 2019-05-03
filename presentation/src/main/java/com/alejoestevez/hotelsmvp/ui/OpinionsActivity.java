package com.alejoestevez.hotelsmvp.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alejoestevez.hotelsmvp.domain.model.Hotel;
import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.R;
import com.alejoestevez.hotelsmvp.mvp.presenter.OpinionsPresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IOpinionsView;
import com.alejoestevez.hotelsmvp.ui.adapter.OpinionsMapAdapter;
import com.alejoestevez.hotelsmvp.ui.constants.Constants;
import com.alejoestevez.hotelsmvp.ui.constants.Flags;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class OpinionsActivity extends AppCompatActivity implements IOpinionsView {

    @BindView(R.id.opinions_fab_new)
    FloatingActionButton opinions_fab_new;
    @BindView(R.id.opinions_web)
    TextView opinions_web;
    @BindView(R.id.opinions_rating)
    RatingBar opinions_rating;
    @BindView(R.id.opinions_progress)
    ProgressBar opinions_progress;

    @BindView(R.id.opinions_recycler)
    RecyclerView opinions_recycler;
    @BindView(R.id.opinions_toolbar)
    Toolbar opinions_toolbar;
    @BindView(R.id.opinions_image)
    ImageView opinions_image;

    @OnClick(R.id.opinions_fab_new)
    public void createNewOpinion() {
        Intent intent = new Intent(OpinionsActivity.this, OpinionActivity.class);
        intent.putExtra(Constants.ExtraCurrentHotelId, currentHotel.getId());
        startActivityForResult(intent, Flags.RequestOpinion);
    }

    private Hotel currentHotel;
    private OpinionsMapAdapter opinionsMapAdapter;

    @Inject
    OpinionsPresenter opinionsPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Inyección de la Activity.
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        /********Full Screen****************/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /************************/

        setContentView(R.layout.activity_opinions);

        //Inicializamos ButterKnife
        ButterKnife.bind(this);

        String hotelJson = getIntent().getExtras().getString(Constants.ExtraCurrentHotel);
        Gson gson = new Gson();
        currentHotel = gson.fromJson(hotelJson, Hotel.class);
        opinions_rating.setRating(currentHotel.getRating().floatValue());
        opinions_web.setText(currentHotel.getWeb());

        opinions_toolbar.setTitle(currentHotel.getName());
        //Asignamos como action bar, nuestra Toolbar de soporte
        setSupportActionBar(opinions_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        opinionsPresenter.initialize(currentHotel.getId());

        Picasso.with(this).load(currentHotel.getPhotoUrl()).into(opinions_image);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Capturamos el Intent
        if (requestCode == Flags.RequestOpinion)//Si volvemos de la pantalla de crear opiniones, buscamos opiniones
            opinionsPresenter.getOpinions(currentHotel.getId());
    }

    @Override
    public void showLoading() {
        opinions_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        opinions_progress.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateUI(User user) {

    }

    @Override
    public void onSignedIn() {
        opinions_fab_new.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSignedOut() {
        opinions_fab_new.setVisibility(View.GONE);
    }

    //Asignamos al adaptador, la lista de opiniones recibidas, así como ir a buscar cada uno de los nombres del perfil público de cada comentario.
    @Override
    public void renderData(Map<String, Opinion> data) {
        //Por cada comentario, buscamos la información del perfil público, es decir el nombre.
        for (String uid : data.keySet())
            opinionsPresenter.getPublicProfile(uid);
        //Asignamos los datos al adaptador
        opinionsMapAdapter = new OpinionsMapAdapter(data, this);

        //Layout a aplicar al listado de hoteles. Será lineal.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //Aplicamos el layout al recycler
        opinions_recycler.setLayoutManager(linearLayoutManager);
        //Asignamos el adaptador al recycler
        opinions_recycler.setAdapter(opinionsMapAdapter);
    }

    //Al obtener la iformación del perfil público, buscamos por uid en el adaptador de opiniones y asignamos el nombre de la persona que escribio el comentario.
    @Override
    public void renderPublicProfile(String value, String uid) {
        opinionsMapAdapter.getItemByUID(uid).setName(value);
        opinionsMapAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        opinionsPresenter.destroy();
    }
}
