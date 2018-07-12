package com.everhomes.enterpriseApproval;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseApprovalGroups;
import com.everhomes.util.StringHelper;

public class EnterpriseApprovalGroup extends EhEnterpriseApprovalGroups {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
