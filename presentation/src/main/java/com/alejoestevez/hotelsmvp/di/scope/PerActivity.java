package com.alejoestevez.hotelsmvp.di.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

//Alcance de las dependencias en la aplicación, en este caso será alcance del ciclo de vida de la Activity.
@Scope
@Retention(RUNTIME)
public @interface PerActivity {}
