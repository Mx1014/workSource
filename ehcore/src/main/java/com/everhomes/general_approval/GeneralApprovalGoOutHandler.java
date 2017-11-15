package com.everhomes.general_approval;

import com.alibaba.fastjson.JSON;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormAskForLeaveValue;
import com.everhomes.rest.general_approval.PostApprovalFormBussinessTripValue;
import com.everhomes.rest.general_approval.PostApprovalFormGoOutValue;
import com.everhomes.rest.general_approval.PostApprovalFormOverTimeValue;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.techpark.punch.PunchExceptionRequest;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.approval.ApprovalRequestDefaultHandler;
import com.everhomes.approval.ApprovalRequestHandler;
import com.everhomes.flow.FlowCase;
import com.everhomes.rest.general_approval.GeneralApprovalAttribute;

import java.sql.Timestamp;
import java.text.ParseException;

/**
 * 
 * <ul>
 * 用于审批的默认handler
 * </ul>
 */
@Component(GeneralApprovalHandler.GENERAL_APPROVAL_PREFIX + "GO_OUT")
public class GeneralApprovalGoOutHandler extends GeneralApprovalPunchDefaultHandler {
	  
//	@Override
//	public void onApprovalCreated(FlowCase flowCase) {
//		//建立一个request
//		PunchExceptionRequest request = new PunchExceptionRequest();
//		GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
//		request.setEnterpriseId(ga.getOrganizationId());
//		//初始状态是等待审批
//		request.setStatus(ApprovalStatus.WAITING_FOR_APPROVING.getCode());
//		request.setUserId(flowCase.getApplyUserId());
//
//		GeneralApprovalVal val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndFeildType(flowCase.getId(),
//				GeneralFormFieldType.GO_OUT.getCode());
//		PostApprovalFormGoOutValue valDTO= JSON.parseObject(val.getFieldStr3(), PostApprovalFormGoOutValue.class);
//
//		request.setBeginTime(Timestamp.valueOf(valDTO.getStartTime()+":00"));
//		request.setEndTime(Timestamp.valueOf(valDTO.getEndTime()+":00"));
//		request.setApprovalAttribute(ga.getApprovalAttribute());
//		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		request.setCreatorUid(UserContext.currentUserId());
//		//用工作流的id 作為表示是哪個審批
//		request.setRequestId(flowCase.getId());
//		punchProvider.createPunchExceptionRequest(request);
//
//	}

//	@Override
//	public void onFlowCaseAbsorted(FlowCase flowCase) {
//		super();
//
//	}
//
//	@Override
//	public void onFlowCaseEnd(FlowCase flowCase) {
//		// TODO Auto-generated method stub
//		super();
//	}
//
}
