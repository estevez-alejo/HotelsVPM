package com.alejoestevez.hotelsmvp.data.datastore.local;

import android.content.Context;
import android.util.Log;

import com.alejoestevez.hotelsmvp.data.datastore.ISessionDataStore;
import com.alejoestevez.hotelsmvp.data.mapper.UserToUserSession;
import com.alejoestevez.hotelsmvp.data.realm.DTO.UserSession;
import com.alejoestevez.hotelsmvp.domain.model.User;

import java.util.Date;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;

//Implementación de la clase responsable del acceso local.
public class LocalSessionDataStore implements ISessionDataStore {

    private static final String TAG = "LocalSessionDataStore";
    Context context;

    private Realm myUserRealm;

    public LocalSessionDataStore(Context context) {
        this.context = context;
        RealmConfiguration myConfig = new RealmConfiguration.Builder()
                .name("UserSession.realm")//Asignamos a nuestra BD un nombre
                .schemaVersion(3)//Asignamos a nuestra BD una versión
                .deleteRealmIfMigrationNeeded()//En caso de que modifiquemos la clase, para no perder datos tendriamos que realizar una migracion, pero con este método recreamos el esquema del objeto realm.Sin obtener una excepción
                .build();

        Realm.setDefaultConfiguration(myConfig);
        myUserRealm = Realm.getInstance(myConfig);
    }

    //Almacenamos localmente el usuario obtenido en la sesión abierta.
    @Override
    public boolean saveSession(User user) {

        UserSession userSession = UserToUserSession.Create(user);

        try {
            //Para guardar en base de datos debemos llamar a:
          /*  myUserRealm.beginTransaction();

            UserSession userSession1 = myUserRealm
                    .createObject(UserSession.class, userSession.getUserId());
            userSession1.setProvider(userSession.getProvider());
            userSession1.setPhotoUrl(userSession.getPhotoUrl());
            userSession1.setName(userSession.getName());
            userSession1.setEmail(userSession.getEmail());
            userSession1.setAuthToken(userSession.getAuthToken());

            //Aplicamos los cambios en la BD
            myUserRealm.commitTransaction();
            */
            //Esta instruccion permite realizar un BeginTransaction,Commit de la transacción y en caso de error cancelar la transacción.
            myUserRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //CopyToRealm crearía un nuevo objeto en Realm,
                    // sin embargo en caso de que ya exista el objeto por su clave privada, lanzaría una excepción.
                    // realm.copyToRealm(obj);

                    userSession.setLastAccess(new Date());
                    //Para solucionarlo, usamos esta instrucción que en caso de que la clave primaria exista,
                    // actualizará dicho objeto y si no existe lo creará.
                    realm.copyToRealmOrUpdate(userSession);
                }
            });

        } catch (Exception error) {
            Log.d(TAG, error.getMessage());
            return false;
        } finally {
            //cerramos la instancia Realm
            myUserRealm.close();
        }
        //Ya tendriamos el objeto creado en la BD

        return true;
    }

    //Obtenemos localmente, la ultima sesión que accedió.
    @Override
    public User getSession() {
        UserSession userSession = null;
        try {

            //findAll->Encuentra todos los objetos que cumplan la condición de la consulta.
            //findFirst->Encuentra el primer objeto que cumpla la condición de la consulta.
            //RealmResults<UserSession> results=myUserRealm.where(UserSession.class).findAll();
            //userSession=myUserRealm.where(UserSession.class).findFirst()
            userSession = myUserRealm
                    .where(UserSession.class)
                    .findAllSorted("LastAccess", Sort.DESCENDING)
                    .first(null);

            return UserToUserSession.Create(userSession);
        } catch (Exception error) {
            return null;
        } finally {
            //cerramos la instancia Realm
            myUserRealm.close();
        }

        //UserSession[] userSessions = results.toArray(new UserSession[results.size()]);

        //UserSession firstUserSession = (userSessions.length>0)?userSessions[0]:null;


    }


    @Override
    public Observable<String> getPublicProfile(String uid) {
        return null;
    }

    @Override
    public Observable<User> savePublicProfile(User user) {
        return null;
    }


}