package com.alejoestevez.hotelsmvp.domain.repositories;

import io.reactivex.Observable;

public interface IFileStorageRepository {

    Observable<byte[]> loadImageBitmapFromUrl(String url);
}
