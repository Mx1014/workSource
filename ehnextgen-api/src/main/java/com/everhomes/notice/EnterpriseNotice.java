package com.everhomes.notice;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseNotices;
import com.everhomes.util.StringHelper;

public class EnterpriseNotice extends EhEnterpriseNotices {
    private static final long serialVersionUID = 924472757L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
