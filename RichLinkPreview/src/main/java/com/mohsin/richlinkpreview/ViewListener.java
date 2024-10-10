package com.mohsin.richlinkpreview;

public interface ViewListener {

    void onSuccess(boolean status);

    void onError(Exception e);
}
