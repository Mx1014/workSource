package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.messaging.admin.MessagingAdminController;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.common.FlowCaseDetailActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.common.ThirdPartActionData;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.messaging.*;

import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.user.FieldContentType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.RequestFieldDTO;
import com.everhomes.rest.yellowPage.GetRequestInfoResponse;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDisplayDestination;
import com.everhomes.rest.yellowPage.ServiceAllianceRequestNotificationTemplateCode;
import com.everhomes.user.*;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalFlowModuleListener;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.module.ServiceModule;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Tuple;
@Component
public class ServiceAllianceFlowModuleListener extends GeneralApprovalFlowModuleListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAllianceFlowModuleListener.class);
	@Autowired
	private ServiceAllianceRequestInfoSearcher serviceAllianceRequestInfoSearcher;
	private static final long MODULE_ID = 40500;
			
    @Autowired
    private YellowPageProvider yellowPageProvider;
    
    @Autowired
    private UserProvider userProvider;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private LocaleTemplateService localeTemplateService;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Override
	public FlowModuleInfo initModule() {
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = serviceModuleProvider.findServiceModuleById(MODULE_ID);
        moduleInfo.setModuleName(module.getName());
        moduleInfo.setModuleId(MODULE_ID);
        return moduleInfo;
	}

	private void changeContentsToObject(String contents,Long userId) {
		GeneralApproval ga;
		GeneralForm form;
		List<PostApprovalFormItem> values;
		List<GeneralFormFieldDTO> fieldDTOs;
		ServiceAlliances serviceOrg;
		ServiceAllianceCategories category = null;
		User user;
		PostApprovalFormItem userCompanyItem;

		user = this.userProvider.findUserById(userId);
		PostApprovalFormCommand cmd = JSONObject.parseObject(contents, PostApprovalFormCommand.class);
		ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		form = this.generalFormProvider.getActiveGeneralFormByOriginId(ga
				.getFormOriginId());
		values = cmd.getValues();
		fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		PostApprovalFormItem sourceVal = getFormFieldDTO(GeneralFormDataSourceType.SOURCE_ID.getCode(),values);
		if(null != sourceVal){
			Long yellowPageId = Long.valueOf(JSON.parseObject(sourceVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
			serviceOrg = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null);
			category = yellowPageProvider.findCategoryById(serviceOrg.getParentId());
		}
		userCompanyItem = getFormFieldDTO(GeneralFormDataSourceType.USER_COMPANY.getCode(),values);

		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<ServiceAllianceNotifyTargets> emails = yellowPageProvider.listNotifyTargets(category.getOwnerType(), category.getOwnerId(), ContactType.EMAIL.getCode(),
				category.getId(), locator, Integer.MAX_VALUE);
	}

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		//旧表单直接退出
		if(flowCase.getOwnerType()!=null && !FlowOwnerType.GENERAL_APPROVAL.getCode().equals(flowCase.getOwnerType()))
			return;

		// 服务联盟的审批拼接工作流 content字符串
		
		ServiceAllianceRequestInfo request = new ServiceAllianceRequestInfo();
		
		PostApprovalFormCommand cmd = JSONObject.parseObject(flowCase.getContent(), PostApprovalFormCommand.class);
		//and by dengs 20170427 异步发送邮件
		sendEmailAsynchronizedTask(flowCase.getContent(),flowCase.getApplyUserId());
		//changeContentsToObject(flowCase.getContent(),flowCase.getApplyUserId());

		StringBuffer contentBuffer = new StringBuffer();
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(ga
				.getFormOriginId());
		List<PostApprovalFormItem> values = cmd.getValues();
		List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
//		PostApprovalFormItem nameVal = getFormFieldDTO(GeneralFormDataSourceType.USER_NAME.getCode(),values);
//		if(null != nameVal){
//			GeneralFormFieldDTO dto = getFieldDTO(nameVal.getFieldName(),fieldDTOs); 
//			contentBuffer.append(dto.getFieldDisplayName());
//			contentBuffer.append(" : ");
//			contentBuffer.append(JSON.parseObject(nameVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
//		}
//		PostApprovalFormItem contactVal = getFormFieldDTO(GeneralFormDataSourceType.USER_PHONE.getCode(),values);
//		if(null != contactVal){
//			if(contentBuffer.length()>1)
//				contentBuffer.append("\n");
//			GeneralFormFieldDTO dto = getFieldDTO(contactVal.getFieldName(),fieldDTOs); 
//			contentBuffer.append(dto.getFieldDisplayName());
//			contentBuffer.append(" : ");
//			contentBuffer.append(JSON.parseObject(contactVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
//		}
//		PostApprovalFormItem entpriseVal = getFormFieldDTO(GeneralFormDataSourceType.USER_COMPANY.getCode(),values);
//		if(null != entpriseVal){
//			if(contentBuffer.length()>1)
//				contentBuffer.append("\n");
//			GeneralFormFieldDTO dto = getFieldDTO(entpriseVal.getFieldName(),fieldDTOs); 
//			contentBuffer.append(dto.getFieldDisplayName());
//			contentBuffer.append(" : ");
//			contentBuffer.append(JSON.parseObject(entpriseVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
//		}
		contentBuffer.append("申请类型");
		contentBuffer.append(" : ");
		contentBuffer.append(ga.getApprovalName());
		PostApprovalFormItem sourceVal = getFormFieldDTO(GeneralFormDataSourceType.SOURCE_ID.getCode(),values);
		Long yellowPageId = 0l;
		if(null != sourceVal){
			if(contentBuffer.length()>1)
				contentBuffer.append("\n");
			yellowPageId = Long.valueOf(JSON.parseObject(sourceVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
			ServiceAlliances  yellowPage = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null); 
			ServiceAllianceCategories  parentPage = yellowPageProvider.findCategoryById(yellowPage.getParentId());

			contentBuffer.append("申请来源");
			contentBuffer.append(" : ");
			contentBuffer.append(parentPage.getName());
			flowCase.setTitle(parentPage.getName());
			request.setServiceAllianceId(yellowPageId);
			request.setType(yellowPage.getParentId());
			
		}
//		flowCase.setModuleName(ga.getApprovalName());
		flowCase.setContent(contentBuffer.toString());
		
		//服务联盟加一个申请
		PostApprovalFormItem organizationVal = getFormFieldDTO(GeneralFormDataSourceType.ORGANIZATION_ID.getCode(),values);
		
		User user = this.userProvider.findUserById(flowCase.getApplyUserId());
		UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		request.setJumpType(2L);
		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		request.setCreatorName(user.getNickName());
		request.setCreatorOrganizationId(Long.valueOf(JSON.parseObject(organizationVal.getFieldValue(), PostApprovalFormTextValue.class).getText()));
		request.setCreatorMobile(identifier.getIdentifierToken());
		if (OwnerType.COMMUNITY.getCode().equals(flowCase.getProjectType())){
			request.setOwnerType(EntityType.ORGANIZATIONS.getCode());
			List<Organization> communityList = organizationProvider.findOrganizationByCommunityId(flowCase.getProjectId());
			request.setOwnerId(communityList.get(0).getId());
		}else{
			request.setOwnerType(flowCase.getProjectType());
			request.setOwnerId(flowCase.getProjectId());
		}
		request.setFlowCaseId(flowCase.getId());
		request.setId(flowCase.getId());
		request.setCreatorUid(UserContext.current().getUser().getId());
		request.setTemplateType("flowCase");
	//	serviceAllianceRequestInfoSearcher.feedDoc(request);

		//推送消息
		//给服务公司留的手机号推消息
		if (yellowPageId!=0) {
			ServiceAlliances serviceOrg = yellowPageProvider.findServiceAllianceById(yellowPageId, null, null);
			if (serviceOrg.getContactMemid()==null || serviceOrg.getContactMemid()==0)
				return;

			String body = "";
			ServiceAllianceCategories category = yellowPageProvider.findCategoryById(serviceOrg.getParentId());
			body += "收到一条"+serviceOrg.getName()+"的申请";

			FlowCaseDetailActionData actionData = new FlowCaseDetailActionData();
			actionData.setFlowCaseId(flowCase.getId());
			actionData.setFlowUserType(FlowUserType.PROCESSOR.getCode());
			actionData.setModuleId(flowCase.getModuleId());
			String url = RouterBuilder.build(Router.WORKFLOW_DETAIL, actionData);
			RouterMetaObject metaObject = new RouterMetaObject();
            metaObject.setUrl(url);
			Map<String, String> meta = new HashMap<>();
			meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
			meta.put(MessageMetaConstant.MESSAGE_SUBJECT, category.getName());
      	    meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));

			OrganizationMember member = organizationProvider.findOrganizationMemberById(serviceOrg.getContactMemid());


			MessageDTO messageDto = createMessageDto(body,meta,member.getTargetId().toString());
			messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                    member.getTargetId().toString(),messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());

			//发消息给服务联盟机构管理员
			CrossShardListingLocator locator = new CrossShardListingLocator();


			List<ServiceAllianceNotifyTargets> targets = yellowPageProvider.listNotifyTargets(category.getOwnerType(),
					category.getOwnerId(), ContactType.MOBILE.getCode(), serviceOrg.getParentId(),locator, Integer.MAX_VALUE);
			if(targets != null && targets.size() > 0) {
				for(ServiceAllianceNotifyTargets target : targets) {
					if(target.getStatus().byteValue() == 1) {
						UserIdentifier contact = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(), target.getContactToken());
						if(contact != null) {
							MessageDTO message = createMessageDto(body,meta,contact.getOwnerUid().toString());
							messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
									contact.getOwnerUid().toString(), message, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
						}
					}

				}
			}




		}
	}

	private MessageDTO createMessageDto(String body,Map<String, String> meta,String uid){
		MessageDTO messageDto = new MessageDTO();
		messageDto.setAppId(AppConstants.APPID_MESSAGING);
		messageDto.setSenderUid(User.SYSTEM_UID);
		messageDto.setBodyType(MessageBodyType.TEXT.getCode());
		messageDto.setBody(body);
		messageDto.setMeta(meta);
		messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
		messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid));
		return messageDto;
	}



	private List<FlowCaseEntity> processCustomRequest(FlowCase flowCase){
		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e ;
		CustomRequestHandler handler = getCustomRequestHandler(flowCase.getReferType());
		GetRequestInfoResponse response = handler.getCustomRequestInfo(flowCase.getReferId());
		List<RequestFieldDTO> dtos = response.getDtos();
		for (RequestFieldDTO dto:dtos){
			switch (FieldContentType.fromCode(dto.getFieldContentType())){
				case TEXT:
					e = new FlowCaseEntity();
					e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
					e.setKey(dto.getFieldName());
					e.setValue(dto.getFieldValue());
					entities.add(e);
					break;
				case IMAGE:
					e = new FlowCaseEntity();
					e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
					e.setKey(dto.getFieldName());
					e.setValue(dto.getFieldValue());
					entities.add(e);
					break;
				case FILE:
					e = new FlowCaseEntity();
					e.setEntityType(FlowCaseEntityType.FILE.getCode());
					e.setKey(dto.getFieldName());
					e.setValue(dto.getFieldValue());
					entities.add(e);
					break;
				default:
					break;
			}
		}
		return entities;
	}

	private CustomRequestHandler getCustomRequestHandler(String templateType) {
		CustomRequestHandler handler = null;

		if(!StringUtils.isEmpty(templateType)) {
			String handlerPrefix = CustomRequestHandler.CUSTOM_REQUEST_OBJ_RESOLVER_PREFIX;
//            if(templateType.length() > 7 && CustomRequestConstants.RESERVE_REQUEST_CUSTOM.equals(templateType.substring(0, 7))) {
//            	templateType = CustomRequestConstants.RESERVE_REQUEST_CUSTOM;
//            }
			if(templateType.startsWith(CustomRequestConstants.RESERVE_REQUEST_CUSTOM)) {
				templateType = CustomRequestConstants.RESERVE_REQUEST_CUSTOM;
			} else if(templateType.startsWith(CustomRequestConstants.APARTMENT_REQUEST_CUSTOM)) {
				templateType = CustomRequestConstants.APARTMENT_REQUEST_CUSTOM;
			} else if(templateType.startsWith(CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM)) {
				templateType = CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM;
			} else if(templateType.startsWith(CustomRequestConstants.SETTLE_REQUEST_CUSTOM)) {
				templateType = CustomRequestConstants.SETTLE_REQUEST_CUSTOM;
			} else if(templateType.startsWith(CustomRequestConstants.INVEST_REQUEST_CUSTOM)) {
				templateType = CustomRequestConstants.INVEST_REQUEST_CUSTOM;
			} else if(templateType.startsWith(CustomRequestConstants.GOLF_REQUEST_CUSTOM)) {
				templateType = CustomRequestConstants.GOLF_REQUEST_CUSTOM;
			} else if(templateType.startsWith(CustomRequestConstants.GYM_REQUEST_CUSTOM)) {
				templateType = CustomRequestConstants.GYM_REQUEST_CUSTOM;
			} else if(templateType.startsWith(CustomRequestConstants.SERVER_REQUEST_CUSTOM)) {
				templateType = CustomRequestConstants.SERVER_REQUEST_CUSTOM;
			}
			handler = PlatformContext.getComponent(handlerPrefix + templateType);
		}

		return handler;
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		if (flowCase.getOwnerType()!=null && !FlowOwnerType.GENERAL_APPROVAL.getCode().equals(flowCase.getOwnerType()))
			return this.processCustomRequest(flowCase);


		List<FlowCaseEntity> entities = new ArrayList<>(); 
		//前面写服务联盟特有的默认字段-姓名-电话-企业-申请类型-申请来源-服务机构
		//姓名
		FlowCaseEntity e = new FlowCaseEntity();
		GeneralApprovalVal val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.USER_NAME.getCode()); 
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
				val.getFormOriginId(), val.getFormVersion());
		List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
		e.setKey(dto.getFieldDisplayName());
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
		entities.add(e);
		
		//电话
		e = new FlowCaseEntity();
		val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.USER_PHONE.getCode()); 
		if(val != null){
			dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
			e.setKey(dto.getFieldDisplayName());
			e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
			e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
			entities.add(e);
		}
		
		//企业
		e = new FlowCaseEntity();
		val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.USER_COMPANY.getCode()); 
		if(val != null){
			dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
			e.setKey(dto.getFieldDisplayName());
			e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
			e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
			entities.add(e);
		}
		
		//楼栋门牌
		e = new FlowCaseEntity();
		val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.USER_ADDRESS.getCode()); 
		if(val != null){
			dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
			e.setKey(dto.getFieldDisplayName());
			e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
			if(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class) != null){
				e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
				entities.add(e);	
			}
		}
		
		//username是存在的防止空指针异常
		if(val ==null){
			val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
					GeneralFormDataSourceType.USER_NAME.getCode()); 
		}


		val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.SOURCE_ID.getCode());
		Long yellowPageId = Long.valueOf(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
		ServiceAlliances  yellowPage = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null);
		ServiceAllianceCategories  parentPage = null;
		if(null != yellowPage)
			parentPage = yellowPageProvider.findCategoryById(yellowPage.getParentId());

		//申请类型
		e = new FlowCaseEntity(); 
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(val.getApprovalId());

		e.setKey("申请类型");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(yellowPage.getName());
		entities.add(e);


		
//		//申请来源
//		e = new FlowCaseEntity();
//		e.setKey("申请来源");
//		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
//		if(null == parentPage)
//			e.setValue("已删除");
//		else
//			e.setValue(parentPage.getName());
//		entities.add(e);
//		//服务机构
//
//		e = new FlowCaseEntity();
//		e.setKey("服务机构");
//		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
//		if(null == yellowPage)
//			e.setValue("已删除");
//		else
//			e.setValue(yellowPage.getName());
//		entities.add(e);
		
		//后面跟自定义模块-- 
		entities.addAll(onFlowCaseCustomDetailRender(flowCase, flowUserType));
		return entities;
	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {
		
	}

	@Override
	public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
			List<Tuple<String, Object>> variables) {
		// TODO Auto-generated method stub
		
	}
	
	private void sendEmailAsynchronizedTask(String contents,Long userId) {
		ServiceAllianceAsynchronizedServiceImpl handler = PlatformContext.getComponent("serviceAllianceAsynchronizedServiceImpl");
		handler.pushToQueque(contents,userId);
	}
	
	@Override
	public void onFlowMessageSend(FlowCaseState ctx, MessageDTO messageDto) {
		Map<String, String> metaMap = messageDto.getMeta();
		
		FlowCase flowCase = ctx.getFlowCase();
		//服务联盟的消息提示，标题搞成了服务联盟大分类的名称-也就是功能入口的名称。
		if(flowCase == null){
			LOGGER.info("onFlowMessageSend flowCase = {}", flowCase);
			return ;
		}
		LOGGER.info("onFlowMessageSend title = {}",flowCase.getTitle());
		metaMap.put(MessageMetaConstant.MESSAGE_SUBJECT, flowCase.getTitle());
	}
}
