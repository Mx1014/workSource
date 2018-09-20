package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.techpark.punch.PunchProvider;
import com.everhomes.techpark.punch.PunchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * <ul>
 * 用于审批的默认handler
 * </ul>
 */
@Component(EnterpriseApprovalDefaultHandler.ENTERPRISE_APPROVAL_DEFAULT_HANDLER_NAME)
public class EnterpriseApprovalDefaultHandler implements EnterpriseApprovalHandler {

	static final String ENTERPRISE_APPROVAL_DEFAULT_HANDLER_NAME = EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX + "Default";
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

	protected static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApprovalDefaultHandler.class);

	@Override
	public void onApprovalCreated(FlowCase flowCase) {
	}

	@Override
	public void onFlowCaseAbsorted(FlowCaseState flowCase) {

	}

	@Override
	public void onFlowCaseDeleted(FlowCase flowCase) {
	}
}
