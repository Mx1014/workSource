package com.everhomes.general_approval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.approval.ApprovalRequestDefaultHandler;
import com.everhomes.approval.ApprovalRequestHandler;

/**
 * 
 * <ul>
 * 用于审批的默认handler
 * </ul>
 */
@Component(GeneralApprovalDefaultHandler.GENERAL_APPROVAL_DEFAULT_HANDLER_NAME)
public class GeneralApprovalDefaultHandler implements GeneralApprovalHandler {
	
	static final String GENERAL_APPROVAL_DEFAULT_HANDLER_NAME = GeneralApprovalHandler.GENERAL_APPROVAL_PREFIX + "Default";

	protected static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalDefaultHandler.class);
	
}
