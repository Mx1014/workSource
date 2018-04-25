package com.everhomes.enterprise_approval;

import com.everhomes.rest.enterprise_approval.*;
import com.everhomes.rest.enterprise_approval.ListEnterpriseApprovalsResponse;

public interface EnterpriseApprovalService {

    ListApprovalFlowRecordsResponse listApprovalFlowMonitors(ListApprovalFlowRecordsCommand cmd);

    ListApprovalFlowRecordsResponse listApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd);

    ListEnterpriseApprovalsResponse listEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd);

}
