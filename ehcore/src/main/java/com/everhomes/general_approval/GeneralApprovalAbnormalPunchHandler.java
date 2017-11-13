package com.everhomes.general_approval;

import com.alibaba.fastjson.JSON;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormAbnormalPunchValue;
import com.everhomes.techpark.punch.PunchExceptionRequest;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCase;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;

/**
 * 
 * <ul>
 * 用于审批的默认handler
 * </ul>
 */
@Component(GeneralApprovalHandler.GENERAL_APPROVAL_PREFIX + "ABNORMAL_PUNCH")
public class GeneralApprovalAbnormalPunchHandler extends GeneralApprovalDefaultHandler {
    //
//    @Override
//    public void onApprovalCreated(FlowCase flowCase) {

        //对于打卡
//		//建立一个request
//		PunchExceptionRequest request = new PunchExceptionRequest();
//		GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
//		request.setEnterpriseId(ga.getOrganizationId());
//		//初始状态是等待审批
//		request.setStatus(ApprovalStatus.WAITING_FOR_APPROVING.getCode());
//		request.setUserId(flowCase.getApplyUserId());
//
//		GeneralApprovalVal val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndFeildType(flowCase.getId(),
//				GeneralFormFieldType.ABNORMAL_PUNCH.getCode());
//		PostApprovalFormAbnormalPunchValue valDTO= JSON.parseObject(val.getFieldStr3(), PostApprovalFormAbnormalPunchValue.class);
//
//		request.setPunchDate(java.sql.Date.valueOf(valDTO.getAbnormalDate()));
//		request.setPunchType(valDTO.getPunchType());
//		request.setPunchIntervalNo(valDTO.getPunchIntervalNo());
//		request.setApprovalAttribute(ga.getApprovalAttribute());
//		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		request.setCreatorUid(UserContext.currentUserId());
//		//用工作流的id 作為表示是哪個審批
//		request.setRequestId(flowCase.getId());
//		punchProvider.createPunchExceptionRequest(request);

//    }

//	@Override
//	public void onFlowCaseAbsorted(FlowCase flowCase) {
//		super();
//
//	}
//
	@Override
	public PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase) {
        //把狀態置为审批通过
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
        PunchExceptionRequest request = punchProvider.findPunchExceptionRequestByRequestId(ga.getOrganizationId(), flowCase.getApplyUserId(), flowCase.getId());
        request.setStatus(ApprovalStatus.AGREEMENT.getCode());
        punchService.approveAbnormalPunch(request);
        Calendar punCalendar = Calendar.getInstance();
        punCalendar.setTime(request.getPunchDate());
        try {
            punchService.refreshPunchDayLog(request.getUserId(), request.getEnterpriseId(), punCalendar);
        } catch (ParseException e) {
            LOGGER.error("refresh punchDayLog ParseException : userid ["+request.getUserId()+"],enterpriseid ["
                    +request.getEnterpriseId()+"] day["+request.getPunchDate()+"]");
        }
        return request;

	}
//
}
