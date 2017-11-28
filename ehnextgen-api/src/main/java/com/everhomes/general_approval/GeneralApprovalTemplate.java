package com.everhomes.general_approval;

import com.everhomes.server.schema.tables.pojos.EhGeneralApprovalTemplates;
import com.everhomes.util.StringHelper;

public class GeneralApprovalTemplate extends EhGeneralApprovalTemplates{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
