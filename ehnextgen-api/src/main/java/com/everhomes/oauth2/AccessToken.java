package com.everhomes.oauth2;

import java.util.Date;

import com.everhomes.server.schema.tables.pojos.EhOauth2Tokens;
import com.everhomes.util.StringHelper;

public class AccessToken extends EhOauth2Tokens {
    private static final long serialVersionUID = -7518708661829285655L;

    public AccessToken() {
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
