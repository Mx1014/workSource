package com.everhomes.oauth2;

import com.everhomes.app.App;

public class OAuth2UserContext {
    private static ThreadLocal<OAuth2UserContext> s_oauth2UserContexts = new ThreadLocal<OAuth2UserContext>();

    private OAuth2AuthenticationType authenticationType;
    private AccessToken accessToken;
    private App clientApp;

    public OAuth2UserContext() {
        authenticationType = OAuth2AuthenticationType.NO_AUTHENTICATION;
    }

    public static OAuth2UserContext current() {
        OAuth2UserContext context = s_oauth2UserContexts.get();
        if(context == null) {
            context = new OAuth2UserContext();
            s_oauth2UserContexts.set(context);
        }
        return context;
    }

    public static void clear() {
        s_oauth2UserContexts.set(null);
    }

    public OAuth2AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(OAuth2AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public App getClientApp() {
        return clientApp;
    }

    public void setClientApp(App clientApp) {
        this.clientApp = clientApp;
    }
}
