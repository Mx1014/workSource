package com.everhomes.oauth2;

import com.everhomes.user.UserLogin;

import java.net.URI;

/**
 * <ul>
 *     <li>uri: uri</li>
 *     <li>userLogin: userLogin</li>
 * </ul>
 */
public class ConfirmAuthorizationVO {

    private URI uri;
    private UserLogin userLogin;

    public ConfirmAuthorizationVO() {
    }

    public ConfirmAuthorizationVO(URI uri, UserLogin userLogin) {
        this.uri = uri;
        this.userLogin = userLogin;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }
}
