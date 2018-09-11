package com.everhomes.approval;

import com.everhomes.server.schema.tables.pojos.EhApprovalCategoryInitLogs;
import com.everhomes.util.StringHelper;

public class ApprovalCategoryInitLog extends EhApprovalCategoryInitLogs {
    private static final long serialVersionUID = -27994036L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
