package com.everhomes.enterpriseApproval;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseApprovalTemplates;
import com.everhomes.util.StringHelper;

public class EnterpriseApprovalTemplate extends EhEnterpriseApprovalTemplates {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
