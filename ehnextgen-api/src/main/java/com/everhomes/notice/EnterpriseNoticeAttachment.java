package com.everhomes.notice;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseNoticeAttachments;
import com.everhomes.util.StringHelper;

public class EnterpriseNoticeAttachment extends EhEnterpriseNoticeAttachments {
    private static final long serialVersionUID = -1682045886L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
