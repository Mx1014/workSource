package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.rest.enterpriseApproval.*;

import java.io.OutputStream;
import java.util.List;

public interface EnterpriseApprovalService {

    ListApprovalFlowRecordsResponse listApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd);

    EnterpriseApprovalRecordDTO convertEnterpriseApprovalRecordDTO(FlowCase r);

    void exportApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd);

    OutputStream getEnterpriseApprovalOutputStream(ListApprovalFlowRecordsCommand cmd, Long taskId);

    ListApprovalFlowRecordsResponse listApprovalFlowMonitors(ListApprovalFlowRecordsCommand cmd);

    List<EnterpriseApprovalGroupDTO> listEnterpriseApprovalGroups();

    List<EnterpriseApprovalGroupDTO> listAvailableApprovalGroups();

    VerifyApprovalTemplatesResponse verifyApprovalTemplates(VerifyApprovalTemplatesCommand cmd);

    void createApprovalTemplates(CreateApprovalTemplatesCommand cmd);

    EnterpriseApprovalDTO createEnterpriseApproval(CreateEnterpriseApprovalCommand cmd);

    EnterpriseApprovalDTO updateEnterpriseApproval(UpdateEnterpriseApprovalCommand cmd);

    ListEnterpriseApprovalsResponse listEnterpriseApprovalTypes(ListEnterpriseApprovalsCommand cmd);

    ListEnterpriseApprovalsResponse listEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd);

    ListEnterpriseApprovalsResponse listAvailableEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd);

}
