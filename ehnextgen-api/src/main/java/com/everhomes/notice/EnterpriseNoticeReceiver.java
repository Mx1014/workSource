package com.everhomes.notice;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseNoticeReceivers;
import com.everhomes.util.StringHelper;

public class EnterpriseNoticeReceiver extends EhEnterpriseNoticeReceivers {
    private static final long serialVersionUID = 1701270952L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
