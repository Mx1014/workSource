// @formatter:off
package com.everhomes.organization;

import com.everhomes.server.schema.tables.EhUserAuthenticationOrganizations;
import com.everhomes.util.StringHelper;

public class UserAuthenticationOrganization extends EhUserAuthenticationOrganizations{
    private static final long serialVersionUID = -2032626298573746336L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
