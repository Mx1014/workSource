package com.everhomes.oauth2client;

import com.everhomes.server.schema.tables.pojos.EhOauth2ClientTokens;
import com.everhomes.util.StringHelper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by xq.tian on 2017/3/7.
 */
public class OAuth2ClientToken extends EhOauth2ClientTokens {

    public boolean expire() {
        Instant expTime = this.getExpirationTime().toInstant();
        return LocalDateTime.now().isAfter(LocalDateTime.ofInstant(expTime, ZoneId.systemDefault()));
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
