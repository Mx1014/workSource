package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhSocialSecurityInoutLog;
import com.everhomes.util.StringHelper;

public class SocialSecurityInoutLog extends EhSocialSecurityInoutLog{

    private static final long serialVersionUID = 8787230089874438900L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
