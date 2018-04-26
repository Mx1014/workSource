package com.everhomes.enterpriseApproval;

import com.everhomes.rest.enterpriseApproval.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnterpriseServiceImpl implements EnterpriseApprovalService{

    @Override
    public ListApprovalFlowRecordsResponse listApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd) {
        return null;
    }

    @Override
    public ListApprovalFlowRecordsResponse listApprovalFlowMonitors(ListApprovalFlowRecordsCommand cmd) {
        return null;
    }

    @Override
    public List<EnterpriseApprovalGroupDTO> listAvailableApprovalGroups() {
        return null;
    }

    @Override
    public void exportApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd) {

    }

    @Override
    public EnterpriseApprovalDTO createEnterpriseApproval(CreateEnterpriseApprovalCommand cmd) {
        return null;
    }

    @Override
    public EnterpriseApprovalDTO updateEnterpriseApproval(UpdateEnterpriseApprovalCommand cmd) {
        return null;
    }

    @Override
    public ListEnterpriseApprovalsResponse listEnterpriseApprovalTypes(ListEnterpriseApprovalsCommand cmd) {
        return null;
    }

    @Override
    public ListEnterpriseApprovalsResponse listEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd) {
        return null;
    }

    @Override
    public ListEnterpriseApprovalsResponse listAvailableEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd) {
        return null;
    }
}
