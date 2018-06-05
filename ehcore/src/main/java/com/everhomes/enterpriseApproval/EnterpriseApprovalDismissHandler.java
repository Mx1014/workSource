package com.everhomes.enterpriseApproval;

import com.everhomes.archives.ArchivesService;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.archives.ArchivesOperationType;
import com.everhomes.rest.enterpriseApproval.ApprovalFlowIdsCommand;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component(EnterpriseApprovalDismissHandler.ENTERPRISE_APPROVAL_DISMISS_HANDLER_NAME)
public class EnterpriseApprovalDismissHandler implements EnterpriseApprovalHandler {

    static final String ENTERPRISE_APPROVAL_DISMISS_HANDLER_NAME = EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX + "DISMISS_APPLICATION";

    @Autowired
    private ArchivesService archivesService;

    @Autowired
    private EnterpriseApprovalService enterpriseApprovalService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Override
    public void onApprovalCreated(FlowCase flowCase) {
        //  1.cancel the archives operate
        OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(flowCase.getApplyUserId(), flowCase.getApplierOrganizationId());
        if(member != null){
            archivesService.cancelArchivesOperation(member.getNamespaceId(), member.getDetailId(), ArchivesOperationType.DISMISS.getCode());
        }
        //  2.cancel the flow
        List<FlowCaseDetail> details = enterpriseApprovalService.listActiveFlowCasesByApprovalId(flowCase.getApplierOrganizationId(), flowCase.getReferId());
        if(details != null){
            details.remove(details.size()-1);   //  ignore the new approval
            List<Long> flowCaseIds = details.stream().map(EhFlowCases::getFlowMainId).collect(Collectors.toList());
            enterpriseApprovalService.stopApprovalFlows(new ApprovalFlowIdsCommand(flowCaseIds));
        }
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState flowCase) {

    }


    @Override
    public GeneralFormReminderDTO getGeneralFormReminder(GetTemplateBySourceIdCommand cmd) {
        Long userId = UserContext.currentUserId();
        return enterpriseApprovalService.checkArchivesApproval(userId, cmd.getOwnerId(), cmd.getSourceId(),
                ArchivesOperationType.DISMISS.getCode());
    }
}
