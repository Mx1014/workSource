package com.everhomes.workReport;

import com.everhomes.server.schema.tables.pojos.EhWorkReportValCommentAttachments;
import com.everhomes.util.StringHelper;

public class WorkReportValCommentAttachment extends EhWorkReportValCommentAttachments {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
