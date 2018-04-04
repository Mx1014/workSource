package com.everhomes.general_approval;

import com.everhomes.server.schema.tables.pojos.EhGeneralApprovalScopeMap;
import com.everhomes.util.StringHelper;

public class GeneralApprovalScopeMap extends EhGeneralApprovalScopeMap{

    private static final long serialVersionUID = 3225057618382428966L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
