package com.everhomes.enterprise_approval;

import com.everhomes.rest.enterprise_approval.*;

public interface EnterpriseApprovalService {

    ListEnterpriseApprovalsResponse listEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd);
}
