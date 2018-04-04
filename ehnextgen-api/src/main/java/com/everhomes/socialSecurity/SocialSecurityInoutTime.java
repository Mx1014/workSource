package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhSocialSecurityInoutTime;
import com.everhomes.util.StringHelper;

public class SocialSecurityInoutTime extends EhSocialSecurityInoutTime{

    private static final long serialVersionUID = -8297335654138811423L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
