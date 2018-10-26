package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.techpark.punch.PunchExceptionRequest;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;

/**
 * <ul>
 * 用于异常申请审批的默认handler
 * </ul>
 */
@Component(EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX + "ABNORMAL_PUNCH")
public class EnterpriseApprovalAbnormalPunchHandler extends EnterpriseApprovalPunchDefaultHandler {

    @Override
    public PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase) {
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
        PunchExceptionRequest request = punchProvider.findPunchExceptionRequestByRequestId(ga.getOrganizationId(), flowCase.getApplyUserId(), flowCase.getId());
        if (request == null) {
            return null;
        }
        request.setStatus(ApprovalStatus.AGREEMENT.getCode());
        punchProvider.updatePunchExceptionRequest(request);
        try {
            Calendar punCalendar = Calendar.getInstance();
            punCalendar.setTime(request.getPunchDate());
            OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(request.getUserId(), request.getEnterpriseId());
            punchService.refreshPunchDayLog(memberDetail, punCalendar);
        } catch (ParseException e) {
            LOGGER.error("refresh punchDayLog ParseException : userid [" + request.getUserId() + "],enterpriseid ["
                    + request.getEnterpriseId() + "] day[" + request.getPunchDate() + "]");
        }
        return request;
    }

    @Override
    public void onFlowCaseDeleted(FlowCase flowCase) {
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
        PunchExceptionRequest request = punchProvider.findPunchExceptionRequestByRequestId(ga.getOrganizationId(), flowCase.getApplyUserId(), flowCase.getId());
        if (request == null) {
            return;
        }
        // 如果流程删除之前是审批通过状态，则删除以后，需要重新校准考勤状态，否则不需要
        boolean showRefreshPunchDayLog = ApprovalStatus.REJECTION != ApprovalStatus.fromCode(request.getStatus());
        punchProvider.deletePunchExceptionRequest(request);

        if (!showRefreshPunchDayLog) {
            return;
        }
        try {
            Calendar punCalendar = Calendar.getInstance();
            punCalendar.setTime(request.getPunchDate());
            OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(request.getUserId(), request.getEnterpriseId());
            punchService.refreshPunchDayLog(memberDetail, punCalendar);
        } catch (Exception e) {
            LOGGER.error("refreshPunchDayLog error user_id = {}", flowCase.getApplyUserId());
        }
    }
}
