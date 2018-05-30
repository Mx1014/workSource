package com.everhomes.general_approval;

import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;

public interface ApprovalPostItemHandler {

    String POST_APPROVAL_PREFIX = "PostApprovalHandler_";

    GetTemplateByApprovalIdResponse postApprovalForm();

}
