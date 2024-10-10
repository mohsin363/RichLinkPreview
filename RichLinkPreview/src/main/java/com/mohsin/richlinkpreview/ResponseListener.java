package com.mohsin.richlinkpreview;

public interface ResponseListener {

    void onData(MetaData metaData);

    void onError(Exception e);
}
