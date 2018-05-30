package com.everhomes.general_approval;

import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;

public interface ApprovalPostValHandler {

    String APPROVAL_POST_PREFIX = "ApprovalPostValHandler_";

    PostGeneralFormDTO postApprovalForm(PostApprovalFormCommand cmd);

}
