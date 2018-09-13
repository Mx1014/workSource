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

    //  use the father's function
    @Override
    public PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase) {
        //把狀態置为审批通过
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
        PunchExceptionRequest request = punchProvider.findPunchExceptionRequestByRequestId(ga.getOrganizationId(), flowCase.getApplyUserId(), flowCase.getId());
        if (null == request) {
            return null;
        }
        request.setStatus(ApprovalStatus.AGREEMENT.getCode());
        punchService.approveAbnormalPunch(request);
        Calendar punCalendar = Calendar.getInstance();
        punCalendar.setTime(request.getPunchDate());
        punchProvider.updatePunchExceptionRequest(request);
        try {
            OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(request.getUserId(), request.getEnterpriseId());
            punchService.refreshPunchDayLog(memberDetail, punCalendar);
        } catch (ParseException e) {
            LOGGER.error("refresh punchDayLog ParseException : userid [" + request.getUserId() + "],enterpriseid ["
                    + request.getEnterpriseId() + "] day[" + request.getPunchDate() + "]");
        }
        return request;
    }
}
