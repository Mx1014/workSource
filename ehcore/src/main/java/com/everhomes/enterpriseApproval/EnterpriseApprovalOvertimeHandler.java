package com.everhomes.enterpriseApproval;

import org.springframework.stereotype.Component;

/**
 * 
 * <ul>
 * 用于审批的默认handler
 * </ul>
 */
@Component(EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX + "OVERTIME")
public class EnterpriseApprovalOvertimeHandler extends EnterpriseApprovalPunchDefaultHandler {
    //  use the father's function
}
