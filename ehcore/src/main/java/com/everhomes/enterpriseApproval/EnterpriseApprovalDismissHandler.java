package com.everhomes.enterpriseApproval;

import com.everhomes.archives.ArchivesService;
import com.everhomes.archives.ArchivesUtil;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.archives.AddArchivesLogCommand;
import com.everhomes.rest.archives.ArchivesDismissType;
import com.everhomes.rest.archives.ArchivesOperationType;
import com.everhomes.rest.archives.ArchivesParameter;
import com.everhomes.rest.archives.DismissArchivesEmployeesCommand;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.enterpriseApproval.ApprovalFlowIdsCommand;
import com.everhomes.rest.enterpriseApproval.ComponentDismissApplicationValue;
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

@Component(EnterpriseApprovalDismissHandler.ENTERPRISE_APPROVAL_DISMISS_HANDLER_NAME)
public class EnterpriseApprovalDismissHandler implements EnterpriseApprovalHandler {

    static final String ENTERPRISE_APPROVAL_DISMISS_HANDLER_NAME = EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX + "DISMISS_APPLICATION";

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApprovalDismissHandler.class);

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
            if (GeneralFormFieldType.fromCode(fieldDTO.getFieldType()).equals(GeneralFormFieldType.DISMISS_APPLICATION)) {
                ComponentDismissApplicationValue val = new ComponentDismissApplicationValue();
                OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, cmd.getOwnerId());
                if (member != null) {
                    val.setApplierName(member.getContactName());
                    val.setApplierDepartment(archivesService.convertToOrgNames(archivesService.getEmployeeDepartment(member.getDetailId())));
                    val.setApplierJobPosition(archivesService.convertToOrgNames(archivesService.getEmployeeJobPosition(member.getDetailId())));
                }
                fieldDTO.setFieldExtra(StringHelper.toJsonString(val));
            }
        }
    }

    @Override
    public void onApprovalCreated(FlowCase flowCase) {
        //  cancel the flow
        List<FlowCaseDetail> details = enterpriseApprovalService.listActiveFlowCasesByApprovalId(flowCase.getApplyUserId(),
                flowCase.getApplierOrganizationId(), flowCase.getReferId());
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
            GeneralApprovalVal generalApprovalVal = generalApprovalValProvider.getSpecificApprovalValByFlowCaseId(flowCase.getId(), GeneralFormFieldType.DISMISS_APPLICATION.getCode());
            if (generalApprovalVal == null) {
                LOGGER.error("No value");
                return null;
            }
            ComponentDismissApplicationValue val = (ComponentDismissApplicationValue) StringHelper.fromJsonString(generalApprovalVal.getFieldStr3(), ComponentDismissApplicationValue.class);
            if (val.getDismissTime() == null) {
                LOGGER.error("DismissTime is null");
                return null;
            }
            DismissArchivesEmployeesCommand configCom = new DismissArchivesEmployeesCommand();
            configCom.setDetailIds(Collections.singletonList(member.getDetailId()));
            configCom.setOrganizationId(flowCase.getApplierOrganizationId());
            configCom.setDismissTime(val.getDismissTime());
            configCom.setDismissType(ArchivesOperationType.SELF_DISMISS.getCode());
            configCom.setDismissReason(ArchivesUtil.convertToArchivesEnum(val.getDismissReason(), ArchivesParameter.DISMISS_REASON));
            configCom.setDismissRemark(val.getDismissRemark());
            configCom.setLogFlag(TrueOrFalseFlag.FALSE.getCode()); // operationType for the archives log, the config'type is still DISMISS
            archivesService.dismissArchivesEmployeesConfig(configCom);

            //  2.set the new archives log
            AddArchivesLogCommand logCom = new AddArchivesLogCommand();
            logCom.setNamespaceId(member.getNamespaceId());
            logCom.setDetailId(member.getDetailId());
            logCom.setOrganizationId(member.getOrganizationId());
            logCom.setOperationType(ArchivesOperationType.SELF_DISMISS.getCode());
            logCom.setOperationTime(val.getDismissTime());
            logCom.setStr1(ArchivesUtil.resolveArchivesEnum(ArchivesDismissType.QUIT.getCode(), ArchivesParameter.DISMISS_TYPE));
            logCom.setStr2(val.getDismissReason());
            logCom.setStr3(val.getDismissRemark());
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
                ArchivesOperationType.DISMISS.getCode());
    }

    @Override
    public void onFlowCaseDeleted(FlowCase flowCase) {
    }
}
