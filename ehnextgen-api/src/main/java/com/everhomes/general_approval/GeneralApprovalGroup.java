package com.everhomes.general_approval;

import com.everhomes.server.schema.tables.pojos.EhGeneralApprovalGroups;
import com.everhomes.util.StringHelper;

public class GeneralApprovalGroup extends EhGeneralApprovalGroups{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
