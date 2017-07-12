package com.everhomes.rentalv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.flow.*;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
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
 
import com.everhomes.util.Tuple;

import javax.annotation.Resource;

@Component
public class Rentalv2FlowModuleListener implements FlowModuleListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Rentalv2FlowModuleListener.class);
	@Autowired
	private FlowUserSelectionProvider flowUserSelectionProvider;
	@Autowired
	private FlowService flowService;
	@Autowired
	private FlowEventLogProvider flowEventLogProvider;
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
	private LocaleStringService localeStringService;
    @Autowired
    private LocaleTemplateService localeTemplateService;
	@Autowired
	private Rentalv2Service rentalService;
	@Autowired
	private SmsProvider smsProvider;
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
		FlowGraphNode graphNode = ctx.getPrefixNode();
		if (null != graphNode) {
			FlowNode preFlowNode = graphNode.getFlowNode();
			FlowGraphNode currentGraphNode = ctx.getCurrentNode();
			FlowNode currNode = currentGraphNode.getFlowNode();

			FlowCase flowCase = ctx.getFlowCase();
			ctx.getCurrentEvent().getFiredButtonId();
			RentalOrder order = null;
			if(null != flowCase.getReferId()){
				order = this.rentalv2Provider.findRentalBillById(flowCase.getReferId());
			}
			String preNodeParam = preFlowNode.getParams();
			String curNodeParam = currNode.getParams();
			String stepType = ctx.getStepType().getCode();

			if (FlowStepType.APPROVE_STEP.getCode().equals(stepType)) {
				if (currentGraphNode instanceof FlowGraphNodeEnd) {
					return;
				}
				if (null != curNodeParam) {
					Byte status = convertFlowStatus(curNodeParam);
					Boolean cancelOtherOrderFlag = false;
					//支付成功之后 cancelOtherOrderFlag设置成true，取消其他竞争状态的订单
					if (null != status && SiteBillStatus.SUCCESS.getCode() == status) {
						cancelOtherOrderFlag = true;
					}
					rentalv2Service.changeRentalOrderStatus(order, status, cancelOtherOrderFlag);
				}
			}else if (FlowStepType.ABSORT_STEP.getCode().equals(stepType)){
				rentalv2Service.changeRentalOrderStatus(order, SiteBillStatus.FAIL.getCode(), false);
			}

		}
	}

	/**
	 * 转换状态，由产品定义
	 * @param nodeType
	 * @return
	 */
	private Byte convertFlowStatus(String nodeType) {

		switch (nodeType) {
			case "agree": return SiteBillStatus.APPROVING.getCode();
			case "unpaid": return SiteBillStatus.PAYINGFINAL.getCode();
			case "paid": return SiteBillStatus.SUCCESS.getCode();
			case "complete": return SiteBillStatus.COMPLETE.getCode();
			default: return null;
		}
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
		if (rs != null && NormalFlag.NONEED.getCode() == rs.getExclusiveFlag()
				&& NormalFlag.NONEED.getCode() == rs.getAutoAssign()) {
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

		List<RentalConfigAttachment> recommendUsers = rentalv2Provider
				.queryRentalConfigAttachmentByOwner(AttachmentType.ORDER_RECOMMEND_USER.name(), order.getId());
		if (null != recommendUsers && recommendUsers.size() != 0) {

			StringBuilder itemStr = new StringBuilder();
			int size = recommendUsers.size();
			for (int i = 0; i < size; i++) {
				if (i == size -1) {
					itemStr.append(recommendUsers.get(i).getUserName()).append("(")
							.append(recommendUsers.get(i).getMobile()).append(")");
				}else {
					itemStr.append(recommendUsers.get(i).getUserName()).append("(")
							.append(recommendUsers.get(i).getMobile()).append(")").append("、");
				}
			}

			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
			e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
					"recommendUser", RentalNotificationTemplateCode.locale, ""));
			e.setValue(itemStr.toString());
			entities.add(e);
		}

		List<RentalConfigAttachment> goodItems = rentalv2Provider
				.queryRentalConfigAttachmentByOwner(AttachmentType.ORDER_GOOD_ITEM.name(), order.getId());
		if (null != goodItems && goodItems.size() != 0) {

			StringBuilder itemStr = new StringBuilder();
			int size = goodItems.size();
			for (int i = 0; i < size; i++) {
				if (i == size -1) {
					itemStr.append(goodItems.get(i).getItemName());
				}else {
					itemStr.append(goodItems.get(i).getItemName()).append("、");
				}
			}

			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
			e.setKey(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.FLOW_SCOPE,
					"goodItem", RentalNotificationTemplateCode.locale, ""));
			e.setValue(itemStr.toString());
			entities.add(e);
		}

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
		Map<String,String> customObject = new HashMap<String,String>();

    	Map<String, String> map = new HashMap<String, String>();

		String contentString = "";
		if(null != order.getOfflinePayeeUid()){
			OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(order.getOfflinePayeeUid(), order.getOrganizationId());
			if(null!=member){
				map.put("offlinePayeeName", member.getContactName());
				map.put("offlinePayeeContact", member.getContactToken());
				map.put("offlineCashierAddress", order.getOfflineCashierAddress());
				contentString = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.FLOW_SCOPE,
						RentalNotificationTemplateCode.RENTAL_FLOW_OFFLINE_INFO, RentalNotificationTemplateCode.locale, map, "");

			}
		}
		customObject.put("offlinePayInfo",contentString);
		customObject.put("orderId", String.valueOf(order.getId()));
		flowCase.setCustomObject(JSON.toJSONString(customObject));
		return entities;
	}

	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onFlowButtonFired(FlowCaseState ctx) {
		// 
		
//		FlowGraphNode currentNode = ctx.getCurrentNode();
//		LOGGER.debug("buttun fire params : " + currentNode.getFlowNode().getParams()+"step type "+ctx.getStepType());
//		//当前节点是同意待支付节点并且事件是催办的时候
//		if(currentNode.getFlowNode().getParams()!=null && currentNode.getFlowNode().getParams()  .equals(RentalFlowNodeParams.PAID.getCode())
//				&& FlowStepType.REMINDER_STEP.getCode().equals(ctx.getStepType().getCode())){
//			FlowLogType logType = FlowLogType.NODE_REMIND;
//			FlowEventLog log = new FlowEventLog();
//			log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
//			log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
//			log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
//			log.setFlowCaseId(ctx.getFlowCase().getId());
//			log.setFlowUserId(ctx.getOperator().getId());
//			log.setLogType(logType.getCode());
//			log.setFlowNodeId(currentNode.getFlowNode().getId());
//			List<FlowEventLog> remindLogs = flowEventLogProvider.findFiredEventsByLog(log);
//			LOGGER.debug("offline pay : cuiban remind : log size : " + remindLogs.size());
//			if(remindLogs.size() == 0){
//				//第一次催办发短信给管理员
//
//				EhFlowCases flowCase =ctx.getFlowCase();
//				RentalOrder order = rentalv2Provider.findRentalBillById(flowCase.getReferId());
//				String templateScope = SmsTemplateCode.SCOPE;
//				String templateLocale = RentalNotificationTemplateCode.locale;
//				RentalResource rs = rentalv2Provider.getRentalSiteById(order.getRentalResourceId());
//				if(null != rs){
//					int templateId = SmsTemplateCode.RENTAL_REMIND_CODE;
//					User user = userProvider.findUserById(ctx.getOperator().getId());
//					List<Tuple<String, Object>> variables = smsProvider.toTupleList("userName", user.getNickName());
//
//					UserIdentifier rentalIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode()) ;
//					smsProvider.addToTupleList(variables, "userPhone", rentalIdentifier.getIdentifierToken());
//					smsProvider.addToTupleList(variables, "resourceName", rs.getResourceName());
//					smsProvider.addToTupleList(variables, "usetime", order.getUseDetail());
//					smsProvider.addToTupleList(variables, "price", order.getPayTotalMoney());
//					Map<String, String> map = new HashMap<>();
//					map.put("userName", user.getNickName());
//					map.put("userPhone", rentalIdentifier.getIdentifierToken());
//					map.put("resourceName", rs.getResourceName());
//					map.put("usetime", order.getUseDetail());
//					map.put("price", ""+order.getPayTotalMoney() );
//
//					List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(ctx.getCurrentNode().getFlowNode().getId()
//							, FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());
//					List<Long> users = flowService.resolvUserSelections(ctx, FlowEntityType.FLOW_NODE, ctx.getCurrentNode().getFlowNode().getId(), selections);
//
//					if(null != users){
//						for(Long userId : users){
//							UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode()) ;
//							rentalService.sendMessageCode(userId,  RentalNotificationTemplateCode.locale, map, RentalNotificationTemplateCode.RENTAL_REMIND_CODE);
//							if(null == userIdentifier){
//								LOGGER.debug("userIdentifier is null...userId = " + order.getRentalUid());
//							}else{
//								smsProvider.sendSms(UserContext.getCurrentNamespaceId(), userIdentifier.getIdentifierToken(), templateScope, templateId, templateLocale, variables);
//							}
//						}
//					}
//
//				}
//			}
//		}
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
		FlowCase flowCase = ctx.getFlowCase();
		RentalOrder order = rentalv2Provider.findRentalBillById(flowCase.getReferId());
		Long resourceTypeId = order.getResourceTypeId();
		RentalResourceType resourceType = rentalv2Provider.getRentalResourceTypeById(resourceTypeId);

		UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getRentalUid(), IdentifierType.MOBILE.getCode()) ;
		User user = this.userProvider.findUserById(order.getRentalUid());
		String userName = user.getNickName();
		String userPhone = userIdentifier.getIdentifierToken();
		String resourceTypeName = resourceType.getName();

		if (SmsTemplateCode.RENTAL_APPLY_SUCCESS_CODE == templateId) {
			//线下模式审批通过 短信
			LOGGER.info("Rental message -----------------------------, templateId={}", templateId);
			smsProvider.addToTupleList(variables, "useTime", order.getUseDetail());
			smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());

			String contactName="";
			String contactToken="";
			if(null != order.getOfflinePayeeUid()){
				OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(order.getOfflinePayeeUid(), order.getOrganizationId());
				if(null != member){
					contactName = member.getContactName();
					contactToken = member.getContactToken();
				}
			}
			smsProvider.addToTupleList(variables, "offlinePayeeName", contactName);
			smsProvider.addToTupleList(variables, "offlinePayeeContact", contactToken);
			smsProvider.addToTupleList(variables, "offlineCashierAddress", order.getOfflineCashierAddress());
			//TODO:以后可能会修改
			//发消息,工作流消息不支持 资源预约需要的变量
			Map<String, String> map = new HashMap<>();
			map.put("useTime", order.getUseDetail());
			map.put("resourceName", order.getResourceName());
			map.put("offlinePayeeName", contactName);
			map.put("offlinePayeeContact", contactToken);
			map.put("offlineCashierAddress", order.getOfflineCashierAddress());
			rentalService.sendMessageCode(order.getRentalUid(),  RentalNotificationTemplateCode.locale, map,
					RentalNotificationTemplateCode.RENTAL_APPLY_SUCCESS_CODE);
		}else if (SmsTemplateCode.RENTAL_APPLY_FAILURE_CODE == templateId) {

			Map<String, String> map = new HashMap<>();
			map.put("useTime", order.getUseDetail());
			map.put("resourceName", order.getResourceName());
			rentalService.sendMessageCode(order.getRentalUid(),  RentalNotificationTemplateCode.locale, map,
					RentalNotificationTemplateCode.RENTAL_APPLY_FAILURE_CODE);
			//审批驳回
			smsProvider.addToTupleList(variables, "useTime", order.getUseDetail());
			smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());
		}else if (SmsTemplateCode.RENTAL_PAY_SUCCESS_CODE == templateId) {

			Map<String, String> map = new HashMap<>();
			map.put("useTime", order.getUseDetail());
			map.put("resourceName", order.getResourceName());
			rentalService.sendMessageCode(order.getRentalUid(),  RentalNotificationTemplateCode.locale, map,
					RentalNotificationTemplateCode.RENTAL_PAY_SUCCESS_CODE);

			//支付成功
			smsProvider.addToTupleList(variables, "useTime", order.getUseDetail());
			smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());

		}else if (SmsTemplateCode.RENTAL_PROCESSOR_SUCCESS_CODE == templateId) {

//			Map<String, String> map = new HashMap<>();
//			map.put("userName", userName);
//			map.put("userPhone", userPhone);
//			map.put("useTime", order.getUseDetail());
//			map.put("resourceName", order.getResourceName());
//			rentalService.sendMessageCode(order.getRentalUid(),  RentalNotificationTemplateCode.locale, map,
//					RentalNotificationTemplateCode.RENTAL_PROCESSOR_SUCCESS_CODE);

			//支付成功
			smsProvider.addToTupleList(variables, "userName", userName);
			smsProvider.addToTupleList(variables, "userPhone", userPhone);
			smsProvider.addToTupleList(variables, "useTime", order.getUseDetail());
			smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());

		} else if (SmsTemplateCode.APPROVE_RENTAL_APPLY_SUCCESS_CODE == templateId) {

			Map<String, String> map = new HashMap<>();
			map.put("useTime", order.getUseDetail());
			map.put("resourceName", order.getResourceName());
			rentalService.sendMessageCode(order.getRentalUid(),  RentalNotificationTemplateCode.locale, map,
					RentalNotificationTemplateCode.APPROVE_RENTAL_APPLY_SUCCESS_CODE);
			//审批线上支付模式 审批通过短信
			smsProvider.addToTupleList(variables, "useTime", order.getUseDetail());
			smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());
		}else if (SmsTemplateCode.RENTAL_CANCEL_CODE == templateId) {

			Map<String, String> map = new HashMap<>();
			map.put("useTime", order.getUseDetail());
			map.put("resourceName", order.getResourceName());
			rentalService.sendMessageCode(order.getRentalUid(),  RentalNotificationTemplateCode.locale, map,
					RentalNotificationTemplateCode.RENTAL_CANCEL_CODE);

			//预约失败
			smsProvider.addToTupleList(variables, "useTime", order.getUseDetail());
			smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());

		}else if (SmsTemplateCode.RENTAL_REMIND_CODE == templateId) {

			Map<String, String> map = new HashMap<>();
			map.put("useTime", order.getUseDetail());
			map.put("resourceName", order.getResourceName());
			rentalService.sendMessageCode(order.getRentalUid(),  RentalNotificationTemplateCode.locale, map,
					RentalNotificationTemplateCode.RENTAL_REMIND_CODE);

			//线下支付客户催办

			smsProvider.addToTupleList(variables, "userName", userName);
			smsProvider.addToTupleList(variables, "userPhone", userPhone);
			smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());
			smsProvider.addToTupleList(variables, "useTime", order.getUseDetail());
			smsProvider.addToTupleList(variables, "price", order.getPayTotalMoney());

		}else if (SmsTemplateCode.RENTAL_PROCESSING_NODE_CODE == templateId) {

			smsProvider.addToTupleList(variables, "userName", userName);
			smsProvider.addToTupleList(variables, "userPhone", userPhone);
			smsProvider.addToTupleList(variables, "resourceTypeName", resourceTypeName);

		}else if (SmsTemplateCode.RENTAL_PROCESSING_NODE_SUPERVISE_CODE == templateId){
			//TODO: 给督办发短信
		}else if (SmsTemplateCode.RENTAL_PROCESSING_BUTTON_APPROVE_CODE == templateId){
			//TODO：给被分配的人发短信

			FlowEventLog flowEventLog = null;
			List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(ctx.getNextNode().getFlowNode().getId()
					, ctx.getFlowCase().getId()
					, ctx.getFlowCase().getStepCount()); ////stepCount 不加 1 的原因是，目标节点处理人是当前 stepCount 计算的 node_enter 的值
			if(logs != null && logs.size() > 0) {
				for(FlowEventLog log : logs) {
					if(log.getFlowUserId() != null && log.getFlowUserId() > 0) {
						flowEventLog = log;
					}
				}
			}

			if (null != flowEventLog) {
				if (null != flowEventLog.getFlowSelectionId()) {
					User entityUser = userProvider.findUserById(flowEventLog.getFlowSelectionId());
					UserIdentifier entityIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(entityUser.getId(), IdentifierType.MOBILE.getCode());
					smsProvider.addToTupleList(variables, "operatorName", entityUser.getNickName());
					smsProvider.addToTupleList(variables, "operatorContact", entityIdentifier.getIdentifierToken());
				}
			}
		}else if (SmsTemplateCode.RENTAL_PROCESSING_BUTTON_ABSORT_CODE == templateId){
			smsProvider.addToTupleList(variables, "resourceName", resourceTypeName);

		}else if (SmsTemplateCode.RENTAL_PROCESSING_BUTTON_REMINDER_CODE == templateId){
			smsProvider.addToTupleList(variables, "userName", userName);
			smsProvider.addToTupleList(variables, "userPhone", userPhone);
			smsProvider.addToTupleList(variables, "resourceName", resourceTypeName);
		}else if (SmsTemplateCode.RENTAL_COMPLETED_CODE == templateId){
			smsProvider.addToTupleList(variables, "resourceName", resourceTypeName);
		}
	}

}
