package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberProfileLogs;
import com.everhomes.util.StringHelper;

public class OrganizationMemberProfileLogs extends EhOrganizationMemberProfileLogs {
    public OrganizationMemberProfileLogs() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
