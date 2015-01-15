package com.everhomes.client;

public interface ClientListener {
    void onLoginInProgress();
    void onLoggedIn();
    void onLoginError(String errorScope, int errorCode, String errorDescription);
    void onOffline();
    void onConnectionOpen();
    void onConnectInProgress(int currentAttempt);
    void onConnectionClose();
}
