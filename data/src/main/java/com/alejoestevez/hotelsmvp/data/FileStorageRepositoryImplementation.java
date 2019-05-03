package com.alejoestevez.hotelsmvp.data;

import com.alejoestevez.hotelsmvp.domain.repositories.IFileStorageRepository;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class FileStorageRepositoryImplementation implements IFileStorageRepository {

    @Inject
    public FileStorageRepositoryImplementation() {

    }


    @Override
    public Observable<byte[]> loadImageBitmapFromUrl(String url) {
        return Observable
                .create(emitter -> {
                    InputStream inputStream = new java.net.URL(url).openStream();
                    //ByteArrayOutputStream se usa para tomar tantos bytes como necesitemos leer.
                    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

                    //Por cada iteración, el buffer sera de 1024 bytes
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];

                    //Preparamos el byteBuffer para leer tantos bytes como tengamos
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        byteBuffer.write(buffer, 0, len);
                    }
                    //Retornamos el array de bytes de la imagen obtenida por url.
                    emitter.onNext(byteBuffer.toByteArray());
                    emitter.onComplete();
                });
    }
}

