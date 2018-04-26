package com.everhomes.enterpriseApproval;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterpriseApproval.*;
import com.everhomes.rest.enterpriseApproval.ListEnterpriseApprovalsResponse;

import javax.validation.Valid;
import java.util.List;

public interface EnterpriseApprovalService {

    ListApprovalFlowRecordsResponse listApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd);

    void exportApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd);

    ListApprovalFlowRecordsResponse listApprovalFlowMonitors(ListApprovalFlowRecordsCommand cmd);

    List<EnterpriseApprovalGroupDTO> listAvailableApprovalGroups();

    EnterpriseApprovalDTO createEnterpriseApproval(CreateEnterpriseApprovalCommand cmd);

    EnterpriseApprovalDTO updateEnterpriseApproval(UpdateEnterpriseApprovalCommand cmd);

    ListEnterpriseApprovalsResponse listEnterpriseApprovalTypes(ListEnterpriseApprovalsCommand cmd);

    ListEnterpriseApprovalsResponse listEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd);

    ListEnterpriseApprovalsResponse listAvailableEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd);

}
