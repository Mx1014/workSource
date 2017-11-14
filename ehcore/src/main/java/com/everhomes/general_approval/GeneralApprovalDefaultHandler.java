package com.everhomes.general_approval;

import com.alibaba.fastjson.JSON;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.techpark.punch.PunchStatus;
import com.everhomes.techpark.punch.PunchExceptionRequest;
import com.everhomes.techpark.punch.PunchLog;
import com.everhomes.techpark.punch.PunchProvider;

import com.everhomes.techpark.punch.PunchService;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCase;

import java.sql.Timestamp;
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
	public void onApprovalCreated(FlowCase flowCase) {
		// 每一个子类自己实现

		//建立一个request
		PunchExceptionRequest request = new PunchExceptionRequest();
		GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
		request.setEnterpriseId(ga.getOrganizationId());
		//初始状态是等待审批
		request.setStatus(ApprovalStatus.WAITING_FOR_APPROVING.getCode());
		request.setUserId(flowCase.getApplyUserId());
		request.setApprovalAttribute(ga.getApprovalAttribute());
		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		request.setCreatorUid(UserContext.currentUserId());
		//用工作流的id 作為表示是哪個審批
		request.setRequestId(flowCase.getId());
		//分别处理
		if (punchService.getTimeIntervalApprovalAttribute().contains(ga.getApprovalAttribute())) {

			GeneralApprovalVal val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndFeildType(flowCase.getId(),
					ga.getApprovalAttribute());
			PostApprovalFormAskForLeaveValue valDTO = JSON.parseObject(val.getFieldStr3(), PostApprovalFormAskForLeaveValue.class);
			request.setBeginTime(Timestamp.valueOf(valDTO.getStartTime() + ":00"));
			request.setEndTime(Timestamp.valueOf(valDTO.getEndTime() + ":00"));
			request.setDuration(valDTO.getDuration());
			request.setCategoryId(valDTO.getRestId());
		}else if(GeneralApprovalAttribute.fromCode(ga.getApprovalAttribute()) == GeneralApprovalAttribute.ABNORMAL_PUNCH){

			GeneralApprovalVal val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndFeildType(flowCase.getId(),
					GeneralFormFieldType.ABNORMAL_PUNCH.getCode());
			PostApprovalFormAbnormalPunchValue valDTO= JSON.parseObject(val.getFieldStr3(), PostApprovalFormAbnormalPunchValue.class);

			request.setPunchDate(java.sql.Date.valueOf(valDTO.getAbnormalDate()));
			request.setPunchType(valDTO.getPunchType());
			request.setPunchIntervalNo(valDTO.getPunchIntervalNo());
			//对于异常申请,如果没有打卡,需要增加一条打卡记录
			PunchLog pl = punchProvider.findPunchLog(ga.getOrganizationId(), flowCase.getApplyUserId(), request.getPunchDate(), request.getPunchType(), request.getPunchIntervalNo());
			if (null == pl) {
				pl = punchService.getAbnormalPunchLog(request);
				punchProvider.createPunchLog(pl);
			}
		}

		punchProvider.createPunchExceptionRequest(request);
	}

	@Override
	public void onFlowCaseAbsorted(FlowCase flowCase) {

		GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
		PunchExceptionRequest request = punchProvider.findPunchExceptionRequestByRequestId(ga.getOrganizationId(), flowCase.getApplyUserId(), flowCase.getId());
		if (null == request) {
			return;
		}
		if (UserContext.current().getUser().getId().equals(request.getUserId())) {
			//如果是自己取消的,删除request
			punchProvider.deletePunchExceptionRequest(request);
		}else {
			//否则request变成reject
			request.setStatus(ApprovalStatus.REJECTION.getCode());
			punchProvider.updatePunchExceptionRequest(request);
		}
	}

	@Override
	public PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase) {
		//通过就把状态置为已审批
		GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
		PunchExceptionRequest request = punchProvider.findPunchExceptionRequestByRequestId(ga.getOrganizationId(), flowCase.getApplyUserId(), flowCase.getId());
		if (null == request) {
			return null;
		}
		request.setStatus(ApprovalStatus.AGREEMENT.getCode());
		punchProvider.updatePunchExceptionRequest(request);
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
