package com.everhomes.oauth2client;

import com.everhomes.server.schema.tables.pojos.EhOauth2Servers;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2017/3/7.
 */
public class OAuth2Server extends EhOauth2Servers {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
