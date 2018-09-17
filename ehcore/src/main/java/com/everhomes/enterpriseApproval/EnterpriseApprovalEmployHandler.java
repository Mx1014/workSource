package com.everhomes.enterpriseApproval;

import com.everhomes.archives.ArchivesService;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.archives.AddArchivesLogCommand;
import com.everhomes.rest.archives.ArchivesOperationType;
import com.everhomes.rest.archives.EmployArchivesEmployeesCommand;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.enterpriseApproval.ApprovalFlowIdsCommand;
import com.everhomes.rest.enterpriseApproval.ComponentEmployApplicationValue;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormReminderCommand;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.techpark.punch.PunchExceptionRequest;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component(EnterpriseApprovalEmployHandler.ENTERPRISE_APPROVAL_EMPLOY_HANDLER_NAME)
public class EnterpriseApprovalEmployHandler implements EnterpriseApprovalHandler {

    static final String ENTERPRISE_APPROVAL_EMPLOY_HANDLER_NAME = EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX + "EMPLOY_APPLICATION";

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApprovalEmployHandler.class);


    @Autowired
    private ArchivesService archivesService;

    @Autowired
    private EnterpriseApprovalService enterpriseApprovalService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private GeneralApprovalValProvider generalApprovalValProvider;

    @Autowired
    private DbProvider dbProvider;

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
        if (member == null)
            return null;
        dbProvider.execute((TransactionStatus status) -> {
            //  1.set the new operate, the old operate will be canceled automatically
            GeneralApprovalVal generalApprovalVal = generalApprovalValProvider.getSpecificApprovalValByFlowCaseId(flowCase.getId(), GeneralFormFieldType.EMPLOY_APPLICATION.getCode());
            if (generalApprovalVal == null) {
                LOGGER.error("No value");
                return null;
            }
            ComponentEmployApplicationValue val = (ComponentEmployApplicationValue) StringHelper.fromJsonString(generalApprovalVal.getFieldStr3(), ComponentEmployApplicationValue.class);
            if (val.getEmploymentTime() == null) {
                LOGGER.error("EmploymentTime is null");
                return null;
            }
            EmployArchivesEmployeesCommand configCom = new EmployArchivesEmployeesCommand();
            configCom.setDetailIds(Collections.singletonList(member.getDetailId()));
            configCom.setOrganizationId(member.getOrganizationId()); // the organizationId must be flowCase's applierOrganizationId
            configCom.setEmploymentTime(val.getEmploymentTime());
            configCom.setEmploymentEvaluation(val.getEmploymentReason());
            configCom.setLogFlag(TrueOrFalseFlag.FALSE.getCode());  // set the new log here.
            archivesService.employArchivesEmployeesConfig(configCom);

            //  2.set the new archives log
            AddArchivesLogCommand logCom = new AddArchivesLogCommand();
            logCom.setNamespaceId(member.getNamespaceId());
            logCom.setDetailId(member.getDetailId());
            logCom.setOrganizationId(member.getOrganizationId());
            logCom.setOperationType(ArchivesOperationType.SELF_EMPLOY.getCode());
            logCom.setOperationTime(val.getEmploymentTime());
            logCom.setStr1(val.getEmploymentReason());
            logCom.setOperatorUid(member.getTargetId());
            logCom.setOperatorName(member.getContactName());
            archivesService.addArchivesLog(logCom);
            return null;
        });
        return null;
    }

    @Override
    public GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd) {
        Long userId = UserContext.currentUserId();
        return enterpriseApprovalService.checkArchivesApproval(userId, cmd.getCurrentOrganizationId(), cmd.getSourceId(),
                ArchivesOperationType.EMPLOY.getCode());
    }

    @Override
    public void onFlowCaseDeleted(FlowCase flowCase) {
    }
}
