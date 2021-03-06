package com.example.pikalti.lib.grelib;

public interface GestureRecognitionClient {
    void setReferenceMode(ClientReferenceMode referenceMode);
    void setGestureRecognitionResponseListener(GestureRecognitionResponseListener gestureRecognitionResponseListener);
    void connect(String uri);
    void disconnect();

    void pause();
    void resume();

}
