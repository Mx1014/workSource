package com.everhomes.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.client.ClientImpl;
import com.everhomes.client.ClientListener;

/**
 * 
 * For testing purpose only. Client production application should have its own
 * main class to work with the client API implemented in Client class from this package
 * 
 * @author Kelven Yang
 *
 */
public class ClientApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientImpl.class);
    
    public static void main(String[] args) {
        final ClientImpl client = new ClientImpl();
        
        client.init("http://localhost:8080/", new ClientListener() {
            public void onConnectionOpen() {
                LOGGER.info("Client connection is open");
            }
            
            public void onConnectInProgress(int currentAttempt) {
                LOGGER.info("Attempting to establish persistent connection, attempt count: " + currentAttempt);
            }

            public void onConnectionClose() {
                LOGGER.info("Client connection is closed");
            }

            public void onLoginInProgress() {
                LOGGER.info("Logging in is in progress");
            }
            
            public void onLoginError(String errorScope, int errorCode, String errorDescription) {
                LOGGER.info("Logging failed");
            }

            public void onLoggedIn() {
                LOGGER.info("Client is logged in");
            }

            public void onOffline() {
                LOGGER.info("Client is now offline");
            }
        });
        client.logon("root", "password", "test-client");
        
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
