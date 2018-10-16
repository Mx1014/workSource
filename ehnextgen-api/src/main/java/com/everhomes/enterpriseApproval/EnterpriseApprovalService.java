package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.rest.enterpriseApproval.ApprovalFlowIdCommand;
import com.everhomes.rest.enterpriseApproval.ApprovalFlowIdsCommand;
import com.everhomes.rest.enterpriseApproval.CreateApprovalTemplatesCommand;
import com.everhomes.rest.enterpriseApproval.CreateEnterpriseApprovalCommand;
import com.everhomes.rest.enterpriseApproval.DeliverApprovalFlowCommand;
import com.everhomes.rest.enterpriseApproval.DeliverApprovalFlowsCommand;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalDTO;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalGroupDTO;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalIdCommand;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalRecordDTO;
import com.everhomes.rest.enterpriseApproval.ListApprovalFlowRecordsCommand;
import com.everhomes.rest.enterpriseApproval.ListApprovalFlowRecordsResponse;
import com.everhomes.rest.enterpriseApproval.ListEnterpriseApprovalsCommand;
import com.everhomes.rest.enterpriseApproval.ListEnterpriseApprovalsResponse;
import com.everhomes.rest.enterpriseApproval.UpdateEnterpriseApprovalCommand;
import com.everhomes.rest.enterpriseApproval.VerifyApprovalTemplatesCommand;
import com.everhomes.rest.enterpriseApproval.VerifyApprovalTemplatesResponse;
import com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
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

    void deleteApprovalFlow(ApprovalFlowIdCommand cmd);

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
