package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.rest.enterpriseApproval.*;
import com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.organization.OrganizationMemberDTO;

import java.io.OutputStream;
import java.util.List;

public interface EnterpriseApprovalService {

    ListApprovalFlowRecordsResponse listApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd);

    EnterpriseApprovalRecordDTO convertEnterpriseApprovalRecordDTO(FlowCase r);

    void exportApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd);

    OutputStream getEnterpriseApprovalOutputStream(ListApprovalFlowRecordsCommand cmd, Long taskId);

    ListApprovalFlowRecordsResponse listActiveApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd);

    void stopApprovalFlows(ApprovalFlowIdsCommand cmd);

    List<OrganizationMemberDTO> listApprovalProcessors(ApprovalFlowIdCommand cmd);

    void deliverApprovalFlow(DeliverApprovalFlowCommand cmd);

    void deliverApprovalFlows(DeliverApprovalFlowsCommand cmd);

    List<EnterpriseApprovalGroupDTO> listEnterpriseApprovalGroups();

    List<EnterpriseApprovalGroupDTO> listAvailableApprovalGroups();

    VerifyApprovalTemplatesResponse verifyApprovalTemplates(VerifyApprovalTemplatesCommand cmd);

    void createApprovalTemplates(CreateApprovalTemplatesCommand cmd);

    EnterpriseApprovalDTO createEnterpriseApproval(CreateEnterpriseApprovalCommand cmd);

    void deleteEnterpriseApproval(EnterpriseApprovalIdCommand cmd);

    EnterpriseApprovalDTO updateEnterpriseApproval(UpdateEnterpriseApprovalCommand cmd);

    void updateGeneralApprovalScope(Integer namespaceId, Long approvalId, List<GeneralApprovalScopeMapDTO> dtos);

    ListEnterpriseApprovalsResponse listEnterpriseApprovalTypes(ListEnterpriseApprovalsCommand cmd);

    void enableEnterpriseApproval(EnterpriseApprovalIdCommand cmd);

    void disableEnterpriseApproval(EnterpriseApprovalIdCommand cmd);

    ListEnterpriseApprovalsResponse listEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd);

    GeneralFormReminderDTO checkArchivesApproval(Long userId, Long organizationId, Long approvalId, Byte operationType);

    ListEnterpriseApprovalsResponse listAvailableEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd);

    List<FlowCaseDetail> listActiveFlowCasesByApprovalId(Long userId, Long ownerId, Long approvalId);

}
