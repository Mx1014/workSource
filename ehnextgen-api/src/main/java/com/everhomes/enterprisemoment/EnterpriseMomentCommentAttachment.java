package com.everhomes.enterprisemoment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentCommentAttachments;
import com.everhomes.util.StringHelper;

public class EnterpriseMomentCommentAttachment extends EhEnterpriseMomentCommentAttachments {
    private static final long serialVersionUID = 1711868432L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
