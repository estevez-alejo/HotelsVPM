package com.alejoestevez.hotelsmvp.data.mapper;

import com.alejoestevez.hotelsmvp.domain.model.SessionProvider;
import com.alejoestevez.hotelsmvp.domain.model.User;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUserToUser {
    public static User Create(FirebaseUser firebaseUser) {
        if (firebaseUser == null) return null;

        User user = new User();

        if (firebaseUser.getProviders().size() > 0)
            user.setProvider(
                    (firebaseUser.getProviders().get(0).equals(SessionProvider.FACEBOOK) ? SessionProvider.FACEBOOK
                            :
                            (firebaseUser.getProviders().get(0).equals(SessionProvider.GOOGLE) ? SessionProvider.GOOGLE
                                    :
                                    (firebaseUser.getProviders().get(0).equals(SessionProvider.EMAIL) ? SessionProvider.EMAIL :
                                            SessionProvider.NONE))));

        user.setUserId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(firebaseUser.getDisplayName());
        user.setPhotoUrl(firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null);

        return user;
    }
}
