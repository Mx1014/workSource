package com.everhomes.general_approval;

import com.alibaba.fastjson.JSON;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormGoOutValue;
import com.everhomes.techpark.punch.PunchExceptionRequest;
import com.everhomes.techpark.punch.PunchProvider;

import com.everhomes.techpark.punch.PunchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * <ul>
 * 用于审批的默认handler
 * </ul>
 */
@Component(GeneralApprovalDefaultHandler.GENERAL_APPROVAL_DEFAULT_HANDLER_NAME)
public class GeneralApprovalDefaultHandler implements GeneralApprovalHandler {
	
	static final String GENERAL_APPROVAL_DEFAULT_HANDLER_NAME = GeneralApprovalHandler.GENERAL_APPROVAL_PREFIX + "Default";
	@Autowired
	protected PunchProvider punchProvider;
	@Autowired
	protected PunchService punchService;
	@Autowired
	protected GeneralApprovalProvider generalApprovalProvider;
	@Autowired
	protected GeneralApprovalValProvider generalApprovalValProvider;
	@Autowired
	protected GeneralFormProvider generalFormProvider;

	protected static ThreadLocal<SimpleDateFormat> datetimeSF = new ThreadLocal<SimpleDateFormat>(){
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	protected static ThreadLocal<SimpleDateFormat> dateSF = new ThreadLocal<SimpleDateFormat>(){
    	protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
	protected static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalDefaultHandler.class);

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		// 每一个子类自己实现
		
	}

	@Override
	public void onFlowCaseAbsorted(FlowCase flowCase) {

		GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
		PunchExceptionRequest request = punchProvider.findPunchExceptionRequestByRequestId(ga.getOrganizationId(), flowCase.getApplyUserId(), flowCase.getId());
		request.setStatus(ApprovalStatus.REJECTION.getCode());

	}

	@Override
	public PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase) {
		//通过就把状态置为已审批
		GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
		PunchExceptionRequest request = punchProvider.findPunchExceptionRequestByRequestId(ga.getOrganizationId(), flowCase.getApplyUserId(), flowCase.getId());
		request.setStatus(ApprovalStatus.AGREEMENT.getCode());
		//如果审批类型是-加班请假等,重刷影响日期的pdl
		if (punchService.getTimeIntervalApprovalAttribute().contains(ga.getApprovalAttribute())) {
			Calendar punCalendar = Calendar.getInstance();
			punCalendar.setTime(request.getBeginTime());
			Date startDay = punchService.calculatePunchDate(punCalendar, request.getEnterpriseId(), request.getUserId());
			punCalendar.setTime(request.getEndTime());
			Date endDay = punchService.calculatePunchDate(punCalendar, request.getEnterpriseId(), request.getUserId());
			punchService.refreshPunchDayLog(request.getUserId(), request.getEnterpriseId(),startDay,endDay);
		}
		return request;
	}

}
