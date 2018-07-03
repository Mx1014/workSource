package com.everhomes.enterpriseApproval;

import org.springframework.stereotype.Component;

/**
 * 
 * <ul>
 * 用于审批的默认handler
 * </ul>
 */
@Component(EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX + "ASK_FOR_LEAVE")
public class EnterpriseApprovalAskForLeaveHandler extends EnterpriseApprovalPunchDefaultHandler {
    //  use the father's function
}
