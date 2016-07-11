package com.everhomes.oauth2;

import java.util.Date;

import com.everhomes.server.schema.tables.pojos.EhOauth2Codes;
import com.everhomes.util.StringHelper;

public class AuthorizationCode extends EhOauth2Codes {
    private static final long serialVersionUID = -2913362074508809971L;

    public AuthorizationCode() {
    }

    public boolean isExpired(Date cutoffTime) {
        assert(cutoffTime != null);

        return cutoffTime.getTime() >= this.getExpirationTime().getTime();
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
