package com.everhomes.enterpriseApproval;

import com.everhomes.archives.ArchivesService;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.archives.ArchivesOperationType;
import com.everhomes.rest.archives.EmployArchivesEmployeesCommand;
import com.everhomes.rest.enterpriseApproval.ApprovalFlowIdsCommand;
import com.everhomes.rest.enterpriseApproval.ComponentEmployApplicationValue;
import com.everhomes.rest.general_approval.*;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.techpark.punch.PunchExceptionRequest;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component(EnterpriseApprovalEmployHandler.ENTERPRISE_APPROVAL_EMPLOY_HANDLER_NAME)
public class EnterpriseApprovalEmployHandler implements EnterpriseApprovalHandler {

    static final String ENTERPRISE_APPROVAL_EMPLOY_HANDLER_NAME = EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX + "EMPLOY_APPLICATION";

    @Autowired
    private ArchivesService archivesService;

    @Autowired
    private EnterpriseApprovalService enterpriseApprovalService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private GeneralApprovalValProvider generalApprovalValProvider;

    @Override
    public void processFormFields(List<GeneralFormFieldDTO> fieldDTOs, GetTemplateBySourceIdCommand cmd) {
        Long userId = UserContext.currentUserId();

        for (GeneralFormFieldDTO fieldDTO : fieldDTOs) {
            if (GeneralFormFieldType.fromCode(fieldDTO.getFieldType()).equals(GeneralFormFieldType.EMPLOY_APPLICATION)) {
                ComponentEmployApplicationValue val = new ComponentEmployApplicationValue();
                OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, cmd.getOwnerId());
                if (member != null) {
                    OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(member.getDetailId());
                    val.setApplierName(member.getContactName());
                    val.setApplierDepartment(archivesService.convertToOrgNames(archivesService.getEmployeeDepartment(member.getDetailId())));
                    val.setApplierJobPosition(archivesService.convertToOrgNames(archivesService.getEmployeeJobPosition(member.getDetailId())));
                    val.setCheckInTime(memberDetail.getCheckInTime().toString());
                }
                fieldDTO.setFieldExtra(StringHelper.toJsonString(val));
            }
        }
    }

    @Override
    public void onApprovalCreated(FlowCase flowCase) {
        //  cancel the flow
        List<FlowCaseDetail> details = enterpriseApprovalService.listActiveFlowCasesByApprovalId(
                flowCase.getApplyUserId(), flowCase.getApplierOrganizationId(), flowCase.getReferId());
        if (details != null) {
            details.remove(0);   //  ignore the new approval
            List<Long> flowCaseIds = details.stream().map(EhFlowCases::getId).collect(Collectors.toList());
            enterpriseApprovalService.stopApprovalFlows(new ApprovalFlowIdsCommand(flowCaseIds));
        }
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState flowCase) {

    }

    @Override
    public PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase) {
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(flowCase.getApplyUserId(), flowCase.getApplierOrganizationId());
        if (member != null) {
            //  1.cancel the archives operate
            archivesService.cancelArchivesOperation(member.getNamespaceId(), member.getDetailId(), ArchivesOperationType.EMPLOY.getCode());

            //  2.set the new operate
            GeneralApprovalVal generalApprovalVal = generalApprovalValProvider.getSpecificApprovalValByFlowCaseId(flowCase.getId(), GeneralFormFieldType.EMPLOY_APPLICATION.getCode());
            if (generalApprovalVal == null)
                return null;
            ComponentEmployApplicationValue val = ConvertHelper.convert(generalApprovalVal.getFieldStr3(), ComponentEmployApplicationValue.class);
            if (val.getEmploymentTime() == null)
                return null;
            EmployArchivesEmployeesCommand cmd = new EmployArchivesEmployeesCommand();
            cmd.setDetailIds(Collections.singletonList(member.getDetailId()));
            cmd.setOrganizationId(flowCase.getApplierOrganizationId());
            cmd.setEmploymentTime(val.getEmploymentTime());
            cmd.setEmploymentEvaluation(val.getEmploymentReason());
            cmd.setOperationType(ArchivesOperationType.SELF_EMPLOY.getCode());  // operationType for the archives log, the config'type is still EMPLOY
            archivesService.employArchivesEmployeesConfig(cmd);
        }
        return null;
    }

    @Override
    public GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd) {
        Long userId = UserContext.currentUserId();
        return enterpriseApprovalService.checkArchivesApproval(userId, cmd.getCurrentOrganizationId(), cmd.getSourceId(),
                ArchivesOperationType.EMPLOY.getCode());
    }
}
