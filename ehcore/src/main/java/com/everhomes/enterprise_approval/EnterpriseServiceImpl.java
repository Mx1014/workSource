package com.everhomes.enterprise_approval;

import com.everhomes.rest.enterprise_approval.*;
import org.springframework.stereotype.Component;

@Component
public class EnterpriseServiceImpl implements EnterpriseApprovalService{


    @Override
    public ListApprovalFlowRecordsResponse listApprovalFlowMonitors(ListApprovalFlowRecordsCommand cmd) {
        return null;
    }

    @Override
    public ListApprovalFlowRecordsResponse listApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd) {
        return null;
    }

    @Override
    public ListEnterpriseApprovalsResponse listEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd) {
        return null;
    }
}
