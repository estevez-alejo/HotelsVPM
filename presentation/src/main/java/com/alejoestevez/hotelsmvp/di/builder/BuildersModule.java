package com.alejoestevez.hotelsmvp.di.builder;

import com.alejoestevez.hotelsmvp.di.module.EditProfileModule;
import com.alejoestevez.hotelsmvp.di.module.EmailLoginModule;
import com.alejoestevez.hotelsmvp.di.module.HomeModule;
import com.alejoestevez.hotelsmvp.di.module.LoginModule;
import com.alejoestevez.hotelsmvp.di.module.OpinionModule;
import com.alejoestevez.hotelsmvp.di.module.OpinionsModule;
import com.alejoestevez.hotelsmvp.di.module.ProfileModule;
import com.alejoestevez.hotelsmvp.di.scope.PerActivity;
import com.alejoestevez.hotelsmvp.ui.EditProfileActivity;
import com.alejoestevez.hotelsmvp.ui.EmailLoginActivity;
import com.alejoestevez.hotelsmvp.ui.HomeActivity;
import com.alejoestevez.hotelsmvp.ui.LoginActivity;
import com.alejoestevez.hotelsmvp.ui.OpinionActivity;
import com.alejoestevez.hotelsmvp.ui.OpinionsActivity;
import com.alejoestevez.hotelsmvp.ui.ProfileActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuildersModule {

    @PerActivity
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeActivity contributeHomeActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity contributeLoginActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = EmailLoginModule.class)
    abstract EmailLoginActivity contributeEmailLoginActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ProfileModule.class)
    abstract ProfileActivity contributeProfileActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = EditProfileModule.class)
    abstract EditProfileActivity contributeEditProfileActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = OpinionsModule.class)
    abstract OpinionsActivity contributeOpinionsActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = OpinionModule.class)
    abstract OpinionActivity contributeOpinionActivity();

}
