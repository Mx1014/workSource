package com.everhomes.general_approval;

import com.everhomes.rest.general_approval.PostApprovalFormCommand;

public interface GeneralApprovalFormDataVerifyHandler {

    String GENERAL_APPROVAL_FORM_DATA_VERIFY_PREFIX = "GeneralApprovalFormDataVerifyHandler_";

    void verify(PostApprovalFormCommand cmd);

}
