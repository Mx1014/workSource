package com.everhomes.pay.oauth2;

import com.everhomes.pay.oauth.AuthorizationCommand;
import org.springframework.http.ResponseEntity;

public interface PayOauth2Service {

    String authorizeLogon(AuthorizationCommand cmd);

    ResponseEntity redirectLogon(String code, String state);
}
