package com.everhomes.techpark.punch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.approval.ApprovalRequest;
import com.everhomes.approval.ApprovalRequestHandler;
import com.everhomes.approval.ApprovalRequestProvider;
import com.everhomes.approval.ApprovalService;
import com.everhomes.contentserver.ResourceType;

import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowGraphNode;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.flow.FlowNode;
import com.everhomes.flow.FlowProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowUserSelection;
import com.everhomes.flow.FlowUserSelectionProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.approval.ApproveApprovalRequestCommand;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.organization.ListUserRelatedOrganizationsCommand;
import com.everhomes.rest.organization.OrganizationSimpleDTO;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.rentalv2.RentalFlowNodeParams;
import com.everhomes.rest.rentalv2.admin.AttachmentType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
 
import com.everhomes.util.ConvertHelper; 
import com.everhomes.util.Tuple;

@Component
public class PunchFlowModuleListener implements FlowModuleListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(PunchFlowModuleListener.class);
	@Autowired
	private FlowUserSelectionProvider flowUserSelectionProvider;
	@Autowired
	private FlowService flowService;
	@Autowired
	private FlowEventLogProvider flowEventLogProvider;
	@Autowired
	private FlowProvider flowProvider;
	@Autowired
	private ContentServerService contentServerService; 
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	LocaleStringService localeStringService;
    @Autowired
    private LocaleTemplateService localeTemplateService; 
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private ApprovalRequestProvider  approvalRequestProvider;
	@Autowired
	private SmsProvider smsProvider;
	@Override
	public FlowModuleInfo initModule() {
		FlowModuleInfo module = new FlowModuleInfo();
		FlowModuleDTO moduleDTO = flowService.getModuleById(PunchConstants.PUNCH_MODULE_ID);
		module.setModuleName(moduleDTO.getDisplayName());
		module.setModuleId(moduleDTO.getModuleId());
		return module;
	}

	@Override
	public void onFlowCaseStart(FlowCaseState ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFlowCaseStateChanged(FlowCaseState ctx) { 
	 
	}
	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {
 
		if(ctx.getFlowCase().getStatus().equals(FlowCaseStatus.FINISHED.getCode())){
			//正常结束,就是审批完成

			ApprovalRequest approvalRequest = approvalRequestProvider.findApprovalRequestById(ctx.getFlowCase().getReferId());
			approvalService.finishApproveApprovalRequest(approvalRequest);
			
		}
	}

	@Override
	public void onFlowCaseActionFired(FlowCaseState ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public String onFlowCaseBriefRender(FlowCase flowCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		//
		// e = new FlowCaseEntity();
		// e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		// e.setKey("test-multi-key3");
		// e.setValue("test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2");
		// entities.add(e);
		//
		// e = new FlowCaseEntity();
		// e.setEntityType(FlowCaseEntityType.TEXT.getCode());
		// e.setKey("test-text-key2");
		// e.setValue("test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2");
		// entities.add(e);
		ApprovalRequest approvalRequest = approvalRequestProvider.findApprovalRequestById(flowCase.getReferId());
		ApprovalRequestHandler handler = approvalService.getApprovalRequestHandler(approvalRequest.getApprovalType());
		List<FlowCaseEntity> entities = handler.getFlowCaseEntities(approvalRequest);

		return entities;
	}

	private String getResourceUrlByUir(String uri, String ownerType, Long ownerId) {
		String url = null;
		if (uri != null && uri.length() > 0) {
			try {
				url = contentServerService.parserUri(uri, ownerType, ownerId);
			} catch (Exception e) {
				LOGGER.error("Failed to parse uri, uri=, ownerType=, ownerId=", uri, ownerType,
						ownerId, e);
			}
		}

		return url;
	}

	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onFlowButtonFired(FlowCaseState ctx) {
		//  
	}

	@Override
	public void onFlowCreating(Flow flow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
			List<Tuple<String, Object>> variables) {
		 
	}

}
