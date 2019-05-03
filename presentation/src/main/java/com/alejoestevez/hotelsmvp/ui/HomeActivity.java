package com.alejoestevez.hotelsmvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.alejoestevez.hotelsmvp.domain.model.Hotel;
import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.R;
import com.alejoestevez.hotelsmvp.mvp.presenter.HomePresenter;
import com.alejoestevez.hotelsmvp.mvp.view.IHomeView;
import com.alejoestevez.hotelsmvp.ui.adapter.HotelsListAdapter;
import com.alejoestevez.hotelsmvp.ui.adapter.RecyclerViewClickListener;
import com.alejoestevez.hotelsmvp.ui.constants.Constants;
import com.alejoestevez.hotelsmvp.ui.constants.Flags;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class HomeActivity extends AppCompatActivity implements IHomeView, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.home_drawer_layout)
    DrawerLayout home_drawer_layout;
    @BindView(R.id.home_toolbar)
    Toolbar home_toolbar;
    @BindView(R.id.home_toolbar_title)
    TextView home_toolbar_title;
    @BindView(R.id.home_swipeRefreshLayout)
    SwipeRefreshLayout home_swipeRefreshLayout;
    @BindView(R.id.home_hotels_recycler)
    RecyclerView home_hotels_recycler;
    @BindView(R.id.home_navigation_view)
    NavigationView home_navigation_view;

    @Inject
    HomePresenter homePresenter;

    HotelsListAdapter hotelsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Inyección de la Activity.
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Inicializamos ButterKnife
        ButterKnife.bind(this);

        //Asignamos como action bar, nuestra Toolbar de soporte
        setSupportActionBar(home_toolbar);

        homePresenter.initialize();

        //Inicializamos el NavigationDrawer
        initializeNavigationDrawer();
    }

    @Override
    public void showLoading() {
        home_swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        home_swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initializeNavigationDrawer() {
        //Navigation Drawer********************************************/
        //Para mostrar un icono en la Toolbar que muestre el Navigation Drawer, debemos  utilizar la clase ActionBarDrawerToggle, de esta forma asociamos
        // que el evento click del icono de la ToolBar, se asocie con el Navigation Drawer para mostrarlo. Ademas requiere dos titulos uno para apertura y otro para cierre.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                home_drawer_layout,
                home_toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        //Asociamos al DrawerLayout, el evento Toggle para que esté a la escucha de los eventos que hemos declarado anteriormente y sean capturados.
        home_drawer_layout.addDrawerListener(toggle);

        //Sincronizamos el estado actual del Navigation Drawer
        toggle.syncState();

        //Buscamos el NavigationView que es que contiene el Menu.
        //Asignamos al NavigationView estar a la escucha de la seleccion de opciones.
        home_navigation_view.setNavigationItemSelectedListener(this);
    }

    @Override
    public void renderData(List<Hotel> data) {
        //Buscamos la puntuación media de cada hotel
        for (Hotel hotel : data) homePresenter.getRating(hotel.getId());

        //Cargamos el Adaptador del listado de hoteles, con los hoteles obtenidos.
        hotelsListAdapter = new HotelsListAdapter(data, this);

        //Layout a aplicar al listado de hoteles. Será lineal.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //Aplicamos el layout al recycler
        home_hotels_recycler.setLayoutManager(linearLayoutManager);
        //Asignamos el adaptador al recycler
        home_hotels_recycler.setAdapter(hotelsListAdapter);

        //Al realizar el gesto de arrastrar hacia abajo con el dedo, debemos refrescar listado.
        home_swipeRefreshLayout.setOnRefreshListener(() -> homePresenter.refreshItems());

        //Capturar evento click sobre los elementos del Recycler
        home_hotels_recycler.addOnItemTouchListener(new RecyclerViewClickListener(this,
                (view, position) -> {
                    //Obtenemos el elemento de la lista del recyclerview que hemos seleccionado,
                    //y por su posición obtendremos del modelo de Hotel, al que corresponde
                    Hotel hotel = ((HotelsListAdapter) home_hotels_recycler.getAdapter())
                            .getItemAtPosition(position);

                    //Abrimos la activity de Opiniones al que le pasamos como parámetro el código del hotel a mostrar.
                    Intent intent = new Intent(getApplicationContext(), OpinionsActivity.class);

                    //Serializamos el hotel, para enviarselo a la activity de listado de opiniones y asi no tener que ir a buscar la información.
                    Gson gson = new Gson();
                    intent.putExtra(Constants.ExtraCurrentHotel, gson.toJson(hotel));
                    startActivity(intent);

                }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Capturamos el Intent
        if (requestCode == Flags.RequestLogin || requestCode == Flags.RequestProfile)
            homePresenter.isSignedIn();


    }

    //Pintamos la puntuación de cada hotel, refrescando el adaptador con la información.
    @Override
    public void renderRating(Double value, int hotelId) {
        hotelsListAdapter.getItemAtPosition(hotelId).setRating(value);
        hotelsListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Obtenemos el Id de la opción Seleccionada
        switch (item.getItemId()) {
            case R.id.nav_login://Si pulsamos realizar login, abriremos pantalla de login.
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivityForResult(intentLogin, Flags.RequestLogin);
                break;
            case R.id.nav_profile://Si pulsamos abrir perfil, abriremos pantalla del perfil.
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivityForResult(intentProfile, Flags.RequestProfile);
                break;
            case R.id.nav_session://Si pulsamos realizar logout, abriremos pantalla de perfil y cerraremos sesión.
                Intent intentCloseProfile = new Intent(this, ProfileActivity.class);
                startActivityForResult(intentCloseProfile, Flags.RequestProfile);
                break;
        }

        //Cerramos el Navigation Drawer tras la selección y lo movemos a la izquierda.
        home_drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        //Para que el menu realice tareas de filtrado, debemos de invocar al filtro que tiene declarado el adaptador y realizar filtros.
        MenuItem myActionMenuItem = menu.findItem(R.id.menuSearch);

        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hotelsListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                hotelsListAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //Una vez obtenida la información de la sesión del usuario, mostramos su nombre en el navigationDrawer
    @Override
    public void updateUI(User user) {
        MenuItem myActionMenuItemProfile = home_navigation_view.getMenu().findItem(R.id.nav_profile);
        myActionMenuItemProfile.setTitle(user.getName());
    }

    //Tras iniciar sesión, ocultamos el elemento de iniciar sesión y mostramos el de ir a perfil y cerrar sesión.
    @Override
    public void onSignedIn() {
        Menu menu = home_navigation_view.getMenu();
        MenuItem myActionMenuItemLogin = menu.findItem(R.id.nav_login);
        MenuItem myActionMenuItemProfile = menu.findItem(R.id.nav_profile);
        MenuItem myActionMenuItemSession = menu.findItem(R.id.nav_session);
        myActionMenuItemLogin.setVisible(false);
        myActionMenuItemProfile.setVisible(true);
        myActionMenuItemSession.setVisible(true);
    }

    @Override
    public void onSignedOut() {

    }

    //Cancelamos las subscripciones de los observadores del presentador.
    @Override
    public void onStop() {
        super.onStop();
        homePresenter.destroy();
    }
}
