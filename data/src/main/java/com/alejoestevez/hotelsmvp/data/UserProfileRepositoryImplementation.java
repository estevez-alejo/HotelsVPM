package com.alejoestevez.hotelsmvp.data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alejoestevez.hotelsmvp.data.factory.SessionDataStoreFactory;
import com.alejoestevez.hotelsmvp.domain.model.User;
import com.alejoestevez.hotelsmvp.domain.repositories.IUserProfileRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

@Singleton
public class UserProfileRepositoryImplementation implements IUserProfileRepository {

    private static final String TAG = "SIGNIN";

    //Factoría del almacén de datos para seleccionar entre Local o Remoto.
    private SessionDataStoreFactory sessionDataStoreFactory;

    private FirebaseAuth firebaseAuth;

    private Context context;

    private StorageReference storageRef;
    private FirebaseStorage storage;
    private Scheduler schedulerThread;

    @Inject
    public UserProfileRepositoryImplementation(Context context,
                                               SessionDataStoreFactory sessionDataStoreFactory,
                                               FirebaseAuth firebaseAuth
            , Scheduler schedulerThread) {
        this.context = context;
        this.sessionDataStoreFactory = sessionDataStoreFactory;
        this.firebaseAuth = firebaseAuth;

        storage = FirebaseStorage.getInstance();
        this.schedulerThread = schedulerThread;
    }

    @Override
    public Observable<User> save(User user) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getName())
                .setPhotoUri(Uri.parse(user.getPhotoUrl()))
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            sessionDataStoreFactory.Local().saveSession(user);
                        }
                    }
                });

        return sessionDataStoreFactory.Remote().savePublicProfile(user);
    }

    @Override
    public Observable<String> uploadFile(byte[] data) {
        return Observable
                .create(emitter -> {

                    String path = "profiles/" + firebaseAuth.getCurrentUser().getUid() + ".png";

                    // Create a storage reference from our app
                    storageRef = storage.getReference(path);

                    UploadTask uploadTask = storageRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            emitter.onError(exception);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            emitter.onNext(downloadUrl.toString());
                            emitter.onComplete();
                        }
                    });

                });
    }

    @Override
    public Observable<String> getPublicProfile(String uid) {
        return sessionDataStoreFactory.Remote().getPublicProfile(uid);
    }
}
