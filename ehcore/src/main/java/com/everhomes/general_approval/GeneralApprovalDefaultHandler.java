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

	protected static ThreadLocal<SimpleDateFormat> datetimeSF = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	protected static ThreadLocal<SimpleDateFormat> dateSF = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	protected static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalDefaultHandler.class);

	@Override
	public void onApprovalCreated(FlowCase flowCase) {
		//// TODO: 2017/11/15  子类自己实现
	}

	@Override
	public void onFlowCaseAbsorted(FlowCase flowCase) {
		//// TODO: 2017/11/15  子类自己实现

	}

	@Override
	public PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase) {
		return null;
	}
}
