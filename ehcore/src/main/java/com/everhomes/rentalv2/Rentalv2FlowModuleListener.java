package com.everhomes.rentalv2;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.flow.FlowProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.organization.ListUserRelatedOrganizationsCommand;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationSimpleDTO;
import com.everhomes.rest.rentalv2.AmorpmFlag;
import com.everhomes.rest.rentalv2.BillAttachmentDTO;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.rentalv2.SiteItemDTO;
import com.everhomes.rest.rentalv2.admin.AttachmentType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class Rentalv2FlowModuleListener implements FlowModuleListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Rentalv2FlowModuleListener.class);
	@Autowired
	private FlowService flowService;
	@Autowired
	private FlowProvider flowProvider;
	@Autowired
	private ContentServerService contentServerService;
	@Autowired
	private Rentalv2Service rentalv2Service;
	@Autowired
	private Rentalv2Provider rentalv2Provider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	LocaleStringService localeStringService;

	@Override
	public FlowModuleInfo initModule() {
		FlowModuleInfo module = new FlowModuleInfo();
		FlowModuleDTO moduleDTO = flowService.getModuleById(Rentalv2Controller.moduleId);
		module.setModuleName(moduleDTO.getDisplayName());
		module.setModuleId(Rentalv2Controller.moduleId);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {
		// TODO Auto-generated method stub

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
		List<FlowCaseEntity> entities = new ArrayList<>();
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
		
		RentalOrder order = rentalv2Provider.findRentalBillById(flowCase.getReferId());
		FlowCaseEntity e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
				"user", RentalNotificationTemplateCode.locale, ""));
		User user = this.userProvider.findUserById(order.getRentalUid());
		if (null != user)
			e.setValue(user.getNickName());
		else {
			LOGGER.debug("user is null...userId = " + order.getRentalUid());
			e.setValue("用户不在系统中");
		}
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
				"contact", RentalNotificationTemplateCode.locale, ""));
		UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(
				order.getRentalUid(), IdentifierType.MOBILE.getCode());
		if (null == userIdentifier) {
			LOGGER.debug("userIdentifier is null...userId = " + order.getRentalUid());
		} else {
			e.setValue(userIdentifier.getIdentifierToken());
		}
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
				"organization", RentalNotificationTemplateCode.locale, "")); 
		List<OrganizationSimpleDTO>  organizaiotnDTOs =this.organizationService.listUserRelateOrgs(new ListUserRelatedOrganizationsCommand(),user);
		 
		for(OrganizationSimpleDTO org : organizaiotnDTOs ){ 
			if (StringUtils.isNotBlank(e.getValue()))
				e.setValue(e.getValue() + "、" + org.getName());
			else
				e.setValue(org.getName());	 
		}
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
				"resourceName", RentalNotificationTemplateCode.locale, ""));
		e.setValue(order.getResourceName());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
				"useDetail", RentalNotificationTemplateCode.locale, ""));
		e.setValue(order.getUseDetail());
		entities.add(e);

		RentalResource rs = this.rentalv2Provider.getRentalSiteById(order.getRentalResourceId());
		if (rs != null && NormalFlag.NONEED.getCode() == rs.getExclusiveFlag().byteValue()
				&& NormalFlag.NONEED.getCode() == rs.getAutoAssign().byteValue()) {
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
			e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
					"count", RentalNotificationTemplateCode.locale, ""));
			e.setValue(order.getRentalCount() + "");
			entities.add(e);
		}

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
				"price", RentalNotificationTemplateCode.locale, ""));
		e.setValue(order.getPayTotalMoney().toString());
		entities.add(e);

		List<RentalItemsOrder> ribs = rentalv2Provider.findRentalItemsBillBySiteBillId(order
				.getId());
		if (null != ribs) { 
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
			e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
					"item", RentalNotificationTemplateCode.locale, ""));
			for (RentalItemsOrder item : ribs) {
				if (StringUtils.isNotBlank(e.getValue()))
					e.setValue(e.getValue() + "\n" + item.getItemName() + "*" + item.getRentalCount());
				else
					e.setValue(item.getItemName() + "*" + item.getRentalCount());
			}
			entities.add(e);
		}

		List<RentalOrderAttachment> attachments = rentalv2Provider
				.findRentalBillAttachmentByBillId(order.getId());
		for (RentalOrderAttachment attachment : attachments) {
			if (attachment.getAttachmentType().equals(AttachmentType.SHOW_CONTENT.getCode())) {
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
				e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
						"content", RentalNotificationTemplateCode.locale, ""));
				e.setValue(attachment.getContent());
				entities.add(e);
			} else if (attachment.getAttachmentType().equals(AttachmentType.LICENSE_NUMBER.getCode())) {
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
				e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
						"license", RentalNotificationTemplateCode.locale, ""));
				e.setValue(attachment.getContent());
				entities.add(e);
			} else if (attachment.getAttachmentType().equals(AttachmentType.TEXT_REMARK.getCode())) {
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
				e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
						"remark", RentalNotificationTemplateCode.locale, ""));
				e.setValue(attachment.getContent());
				entities.add(e);
			} else if (attachment.getAttachmentType().equals(AttachmentType.ATTACHMENT.getCode())) {
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
				e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
						"attachment", RentalNotificationTemplateCode.locale, ""));
				e.setValue(this.contentServerService.parserUri(attachment.getContent(),
						EntityType.USER.getCode(), UserContext.current().getUser().getId()));
				entities.add(e);
			}

		}

//		flowCase.setCustomObject(JSONObject.toJSONString(dto));
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
		// FlowGraphNode currentNode = ctx.getCurrentNode();
		// FlowNode flowNode = currentNode.getFlowNode();
		// FlowCase flowCase = ctx.getFlowCase();
		//
		// String stepType = ctx.getStepType().getCode();
		// String param = flowNode.getParams();
		//
		// Long flowId = flowNode.getFlowMainId();
		// ParkingCardRequest parkingCardRequest =
		// parkingProvider.findParkingCardRequestById(flowCase.getReferId());
		// Flow flow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(),
		// flowCase.getFlowVersion());
		// String tag1 = flow.getStringTag1();
		//
		// long now = System.currentTimeMillis();
		// LOGGER.debug("update parking request, stepType={}, tag1={}, param={}",
		// stepType, tag1, param);
		// if(FlowStepType.APPROVE_STEP.getCode().equals(stepType)) {
		// if("AUDITING".equals(param)) {
		// parkingCardRequest.setStatus(ParkingCardRequestStatus.QUEUEING.getCode());
		// parkingCardRequest.setAuditSucceedTime(new Timestamp(now));
		// parkingProvider.updateParkingCardRequest(parkingCardRequest);
		// }
		// else if("QUEUEING".equals(param)) {
		//
		// ParkingFlow parkingFlow =
		// parkingProvider.getParkingRequestCardConfig(parkingCardRequest.getOwnerType(),
		// parkingCardRequest.getOwnerId(),
		// parkingCardRequest.getParkingLotId(), flowId);
		// Integer requestedCount =
		// parkingProvider.countParkingCardRequest(parkingCardRequest.getOwnerType(),
		// parkingCardRequest.getOwnerId(),
		// parkingCardRequest.getParkingLotId(), flowId,
		// ParkingCardRequestStatus.SUCCEED.getCode(), null);
		//
		// Integer totalCount = parkingFlow.getMaxIssueNum();
		// Integer surplusCount = totalCount - requestedCount;
		// if(surplusCount <= 0) {
		// LOGGER.error("surplusCount is 0.");
		// throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE,
		// ParkingErrorCode.ERROR_ISSUE_CARD,
		// "surplusCount is 0.");
		// }
		// if(ParkingRequestFlowType.QUEQUE.getCode() == Integer.valueOf(tag1))
		// {
		// parkingCardRequest.setStatus(ParkingCardRequestStatus.PROCESSING.getCode());
		// parkingCardRequest.setIssueTime(new Timestamp(now));
		// parkingProvider.updateParkingCardRequest(parkingCardRequest);
		// }else {
		// LOGGER.debug("update parking request, stepType={}, tag1={}",
		// stepType, tag1);
		// parkingCardRequest.setStatus(ParkingCardRequestStatus.SUCCEED.getCode());
		// parkingCardRequest.setProcessSucceedTime(new Timestamp(now));
		// parkingProvider.updateParkingCardRequest(parkingCardRequest);
		// }
		// }else if("PROCESSING".equals(param)) {
		// if(ParkingRequestFlowType.QUEQUE.getCode() == Integer.valueOf(tag1))
		// {
		// parkingCardRequest.setStatus(ParkingCardRequestStatus.SUCCEED.getCode());
		// parkingCardRequest.setProcessSucceedTime(new Timestamp(now));
		// parkingProvider.updateParkingCardRequest(parkingCardRequest);
		// }
		// }
		// }else if(FlowStepType.ABSORT_STEP.getCode().equals(stepType)) {
		// if("SUCCEED".equals(param)) {
		// parkingCardRequest.setStatus(ParkingCardRequestStatus.OPENED.getCode());
		// parkingCardRequest.setOpenCardTime(new Timestamp(now));
		// parkingProvider.updateParkingCardRequest(parkingCardRequest);
		// }else {
		// parkingCardRequest.setStatus(ParkingCardRequestStatus.INACTIVE.getCode());
		// parkingCardRequest.setCancelTime(new Timestamp(now));
		// parkingProvider.updateParkingCardRequest(parkingCardRequest);
		// }
		//
		// }
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

	// @Override
	// public void issueParkingCards(IssueParkingCardsCommand cmd) {
	//
	// if(cmd.getCount() == null) {
	// LOGGER.error("Count cannot be null.");
	// throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
	// ErrorCodes.ERROR_INVALID_PARAMETER,
	// "Count cannot be null.");
	// }
	// ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(),
	// cmd.getOwnerId(), cmd.getParkingLotId());
	//
	// StringBuilder strBuilder = new StringBuilder();
	// // 补上ownerType、ownerId、parkingLotId参数，以区分清楚是哪个小区哪个停车场的车牌，否则会发放其它园区的车牌 by
	// lqs 20161103
	// List<ParkingCardRequest> list =
	// parkingProvider.listParkingCardRequests(null, cmd.getOwnerType(),
	// cmd.getOwnerId(), cmd.getParkingLotId(), null,
	// ParkingCardRequestStatus.QUEUEING.getCode(),
	// null, null, cmd.getCount())
	// .stream().map(r -> {
	// r.setStatus(ParkingCardRequestStatus.NOTIFIED.getCode());
	// if(strBuilder.length() > 0) {
	// strBuilder.append(", ");
	// }
	// strBuilder.append(r.getId());
	// return r;
	// }).collect(Collectors.toList());
	//
	// parkingProvider.updateParkingCardRequest(list);
	// // 添加日志，方便定位哪个车牌被修改状态了（即发放了） by lqs 20161103
	// if(LOGGER.isDebugEnabled()) {
	// LOGGER.debug("Issue parking cards, requestIds=[{}]",
	// strBuilder.toString());
	// }
	// Integer namespaceId = UserContext.getCurrentNamespaceId();
	// Map<String, Object> map = new HashMap<String, Object>();
	// String deadline = deadline(parkingLot.getCardReserveDays());
	// map.put("deadline", deadline);
	// String scope = ParkingNotificationTemplateCode.SCOPE;
	// int code = ParkingNotificationTemplateCode.USER_APPLY_CARD;
	// String locale = "zh_CN";
	// String notifyTextForApplicant =
	// localeTemplateService.getLocaleTemplateString(namespaceId, scope, code,
	// locale, map, "");
	// list.forEach(applier -> {
	// sendMessageToUser(applier.getRequestorUid(), notifyTextForApplicant);
	// });
	//
	//
	// }

	// private void setParkingCardIssueFlag(SetParkingCardIssueFlagCommand cmd){
	//
	// checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(),
	// cmd.getParkingLotId());
	// if(cmd.getId() == null){
	// LOGGER.error("Id cannot be null.");
	// throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
	// ErrorCodes.ERROR_INVALID_PARAMETER,
	// "Id cannot be null.");
	// }
	//
	// ParkingCardRequest parkingCardRequest =
	// parkingProvider.findParkingCardRequestById(cmd.getId());
	// if(parkingCardRequest == null){
	// LOGGER.error("ParkingCardRequest not found, cmd={}", cmd);
	// throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
	// ErrorCodes.ERROR_GENERAL_EXCEPTION,
	// "ParkingCardRequest not found");
	// }
	// if(parkingCardRequest.getStatus() !=
	// ParkingCardRequestStatus.NOTIFIED.getCode()){
	// LOGGER.error("ParkingCardRequest status is not notified, cmd={}", cmd);
	// throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
	// ErrorCodes.ERROR_GENERAL_EXCEPTION,
	// "ParkingCardRequest status is not notified.");
	// }
	// //设置已领取状态和 领取时间
	// parkingCardRequest.setStatus(ParkingCardRequestStatus.ISSUED.getCode());
	// parkingCardRequest.setIssueFlag(ParkingCardIssueFlag.ISSUED.getCode());
	// parkingCardRequest.setIssueTime(new
	// Timestamp(System.currentTimeMillis()));
	// parkingProvider.updateParkingCardRequest(Collections.singletonList(parkingCardRequest));
	// }
}
