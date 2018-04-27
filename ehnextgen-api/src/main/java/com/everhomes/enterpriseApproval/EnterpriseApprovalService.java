package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterpriseApproval.*;
import com.everhomes.rest.enterpriseApproval.ListEnterpriseApprovalsResponse;

import javax.validation.Valid;
import java.io.OutputStream;
import java.util.List;

public interface EnterpriseApprovalService {

    ListApprovalFlowRecordsResponse listApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd);

    EnterpriseApprovalRecordDTO convertEnterpriseApprovalRecordDTO(FlowCase r);

    void exportApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd);

    OutputStream getEnterpriseApprovalOutputStream(ListApprovalFlowRecordsCommand cmd, Long taskId);

    ListApprovalFlowRecordsResponse listApprovalFlowMonitors(ListApprovalFlowRecordsCommand cmd);

    List<EnterpriseApprovalGroupDTO> listAvailableApprovalGroups();

    EnterpriseApprovalDTO createEnterpriseApproval(CreateEnterpriseApprovalCommand cmd);

    EnterpriseApprovalDTO updateEnterpriseApproval(UpdateEnterpriseApprovalCommand cmd);

    ListEnterpriseApprovalsResponse listEnterpriseApprovalTypes(ListEnterpriseApprovalsCommand cmd);

    ListEnterpriseApprovalsResponse listEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd);

    ListEnterpriseApprovalsResponse listAvailableEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd);

}
