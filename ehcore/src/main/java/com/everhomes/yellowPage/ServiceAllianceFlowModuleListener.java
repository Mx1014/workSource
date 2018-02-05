package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.common.FlowCaseDetailActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.messaging.*;

import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.user.FieldContentType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.RequestFieldDTO;
import com.everhomes.rest.yellowPage.GetRequestInfoResponse;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.rest.yellowPage.ServiceAllianceWorkFlowStatus;
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
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Tuple;
@Component
public class ServiceAllianceFlowModuleListener extends GeneralApprovalFlowModuleListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAllianceFlowModuleListener.class);
	@Autowired
	private ServiceAllianceRequestInfoSearcher serviceAllianceRequestInfoSearcher;
	public static final long MODULE_ID = 40500;
			
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
		List<ServiceAllianceNotifyTargets> emails = yellowPageProvider.listNotifyTargets(category.getNamespaceId(), ContactType.EMAIL.getCode(),
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
		PostApprovalFormItem sourceVal = getFormFieldDTO(GeneralFormDataSourceType.SOURCE_ID.getCode(),values);
		Long yellowPageId = 0l;
		ServiceAlliances  yellowPage = null;
		if(null != sourceVal){
			if(contentBuffer.length()>1)
				contentBuffer.append("\n");
			yellowPageId = Long.valueOf(JSON.parseObject(sourceVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
			yellowPage = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null); 
			ServiceAllianceCategories  parentPage = yellowPageProvider.findCategoryById(yellowPage.getParentId());
			
			contentBuffer.append("服务名称 : ");
			contentBuffer.append(yellowPage.getName());
			flowCase.setTitle(parentPage.getName());
			request.setServiceAllianceId(yellowPageId);
			request.setType(yellowPage.getParentId());
			
		}
		flowCase.setContent(contentBuffer.toString());
		
		Byte status = ServiceAllianceWorkFlowStatus.PROCESSING.getCode();
		if(FlowCaseType.fromCode(flowCase.getCaseType()) == FlowCaseType.DUMB){
			status = ServiceAllianceWorkFlowStatus.NONE.getCode();
		}
		//同步申请到es
		syncRequest(values, request, flowCase,yellowPage,status);
		//发送消息给相关人员
		sendMessage(yellowPageId,flowCase);
	}

	private void syncRequest(List<PostApprovalFormItem> values,ServiceAllianceRequestInfo request,FlowCase flowCase,ServiceAlliances  sa, Byte status) {
		PostApprovalFormItem organizationVal = getFormFieldDTO(GeneralFormDataSourceType.ORGANIZATION_ID.getCode(),values);
		User user = this.userProvider.findUserById(flowCase.getApplyUserId());
		UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		request.setJumpType(2L);
		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		request.setCreatorName(user.getNickName());
		request.setCreatorOrganizationId(Long.valueOf(JSON.parseObject(organizationVal.getFieldValue(), PostApprovalFormTextValue.class).getText()));
		request.setCreatorMobile(identifier.getIdentifierToken());
		if (EntityType.COMMUNITY.getCode().equals(flowCase.getProjectType()) || "community".equals(flowCase.getProjectType())){
	       	request.setOwnerType(ServiceAllianceBelongType.COMMUNITY.getCode());
	       	request.setOwnerId(flowCase.getProjectId());
		}else{
			OrganizationCommunityRequest ocr =organizationProvider.getOrganizationCommunityRequestByOrganizationId(flowCase.getProjectId());
        	if(ocr != null){
        		request.setOwnerType(ServiceAllianceBelongType.COMMUNITY.getCode());
            	request.setOwnerId(ocr.getCommunityId());
        	}else{
        		request.setOwnerType(flowCase.getProjectType());
            	request.setOwnerId(flowCase.getProjectId());
        	}
		}
		request.setFlowCaseId(flowCase.getId());
		request.setCreatorUid(UserContext.current().getUser().getId());
		request.setSecondCategoryId(sa.getCategoryId());
		request.setSecondCategoryName(sa.getServiceType());
		request.setSecondCategoryId(sa.getCategoryId());
		request.setWorkflowStatus(status);
		request.setId(flowCase.getId());
		request.setTemplateType("flowCase");
		serviceAllianceRequestInfoSearcher.feedDoc(request);
	}

	private void sendMessage(Long yellowPageId, FlowCase flowCase) {
		//推送消息
		//给服务公司留的手机号推消息
		if (yellowPageId!=0) {
			ServiceAlliances serviceOrg = yellowPageProvider.findServiceAllianceById(yellowPageId, null, null);
			ServiceAllianceCategories category = yellowPageProvider.findCategoryById(serviceOrg.getParentId());
			//发消息给服务联盟机构管理员
			CrossShardListingLocator locator = new CrossShardListingLocator();
			List<ServiceAllianceNotifyTargets> targets = yellowPageProvider.listNotifyTargets(category.getNamespaceId(), ContactType.MOBILE.getCode(), serviceOrg.getParentId(),locator, Integer.MAX_VALUE);

			if ((serviceOrg.getContactMemid()!=null && serviceOrg.getContactMemid()!=0) || (targets != null && targets.size() > 0)) {
				String body = new StringBuffer().append("收到一条").append(category.getName()).append("的申请,服务名称：").append(serviceOrg.getName()).toString();
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

				if(serviceOrg.getContactMemid()!=null && serviceOrg.getContactMemid()!=0) {
					OrganizationMember member = organizationProvider.findOrganizationMemberById(serviceOrg.getContactMemid());

					MessageDTO messageDto = createMessageDto(body, meta, member.getTargetId().toString());
					messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
							member.getTargetId().toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
				}

				if (targets != null && targets.size() > 0) {
					for (ServiceAllianceNotifyTargets target : targets) {
						if (target.getStatus().byteValue() == 1) {
							UserIdentifier contact = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(), target.getContactToken());
							if (contact != null) {
								MessageDTO message = createMessageDto(body, meta, contact.getOwnerUid().toString());
								messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
										contact.getOwnerUid().toString(), message, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
							}
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
	public void onFlowCaseEnd(FlowCaseState ctx) {
		syncRequest(ctx);
	}
	
	private void syncRequest(FlowCaseState ctx) {
		 // 更新状态
       FlowCase flowCase = ctx.getFlowCase();
       Byte status = flowCase.getStatus();

       if (status != null) {
   		ServiceAllianceRequestInfo request = new ServiceAllianceRequestInfo();
	   	List<GeneralApprovalVal> lists = generalApprovalValProvider.queryGeneralApprovalValsByFlowCaseId(flowCase.getId());
	    List<PostApprovalFormItem> values = lists.stream().map(r -> {
	         PostApprovalFormItem value = new PostApprovalFormItem();
	         value.setFieldName(r.getFieldName());
	         value.setFieldType(r.getFieldType());
	         value.setFieldValue(r.getFieldStr3());
	         return value;
	    }).collect(Collectors.toList());
	    PostApprovalFormItem sourceVal = getFormFieldDTO(GeneralFormDataSourceType.SOURCE_ID.getCode(),values);
   		Long yellowPageId = 0L;
   		ServiceAlliances  yellowPage = null;
   		if(null != sourceVal){
   			yellowPageId = Long.valueOf(JSON.parseObject(sourceVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
   			yellowPage = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null); 
   			request.setServiceAllianceId(yellowPageId);
   			request.setType(yellowPage.getParentId());
   			
   		}
   		syncRequest(values, request, flowCase,yellowPage,status);
       }
	
	}
	
	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {
//		syncRequest(ctx);
	}

	private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		if (flowCase.getOwnerType() != null && !FlowOwnerType.GENERAL_APPROVAL.getCode().equals(flowCase.getOwnerType()))
			return this.processCustomRequest(flowCase);


		List<FlowCaseEntity> entities = new ArrayList<>();
		if (flowCase.getCreateTime() != null) {
			entities.add(new FlowCaseEntity("申请时间", flowCase.getCreateTime().toLocalDateTime().format(fmt), FlowCaseEntityType.MULTI_LINE.getCode()));
		}

		GeneralApprovalVal val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.SOURCE_ID.getCode());
		if (val != null) {
			Long yellowPageId = Long.valueOf(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
			ServiceAlliances yellowPage = yellowPageProvider.findServiceAllianceById(yellowPageId, null, null);
			entities.add(new FlowCaseEntity("服务名称", yellowPage.getName(), FlowCaseEntityType.MULTI_LINE.getCode()));
			entities.add(new FlowCaseEntity("服务类型", yellowPage.getServiceType(), FlowCaseEntityType.MULTI_LINE.getCode()));

			//前面写服务联盟特有的默认字段-姓名-电话-企业-申请类型-申请来源-服务机构
			//姓名
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
					val.getFormOriginId(), val.getFormVersion());
			List<GeneralFormFieldDTO> fieldDTOs;
			GeneralFormFieldDTO dto;
			if (form != null) {
				fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
				val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
						GeneralFormDataSourceType.USER_NAME.getCode());
				dto = getFieldDTO(val.getFieldName(), fieldDTOs);
				entities.add(new FlowCaseEntity(dto.getFieldDisplayName(), JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText(), FlowCaseEntityType.MULTI_LINE.getCode()));

				//电话
				val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
						GeneralFormDataSourceType.USER_PHONE.getCode());
				if (val != null) {
					dto = getFieldDTO(val.getFieldName(), fieldDTOs);
					entities.add(new FlowCaseEntity(dto.getFieldDisplayName(), JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText(), FlowCaseEntityType.MULTI_LINE.getCode()));
				}

				//企业
				val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
						GeneralFormDataSourceType.USER_COMPANY.getCode());
				if (val != null) {
					dto = getFieldDTO(val.getFieldName(), fieldDTOs);
					entities.add(new FlowCaseEntity(dto.getFieldDisplayName(), JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText(), FlowCaseEntityType.MULTI_LINE.getCode()));
				}

				//楼栋门牌
				FlowCaseEntity e = new FlowCaseEntity();
				val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
						GeneralFormDataSourceType.USER_ADDRESS.getCode());
				if (val != null) {
					dto = getFieldDTO(val.getFieldName(), fieldDTOs);
					e.setKey(dto.getFieldDisplayName());
					e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
					if (JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class) != null) {
						e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
						entities.add(e);
					}
				}


//		ServiceAllianceCategories  parentPage = null;
//		////申请来源
//		if(null != yellowPage){
//			parentPage = yellowPageProvider.findCategoryById(yellowPage.getParentId());
//			flowCase.setModuleName(parentPage.getName());
//		}
//		e = new FlowCaseEntity();
//		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
//		e.setKey("申请来源");
//		e.setValue(parentPage==null?"未知":parentPage.getName());
//		entities.add(e);


				//后面跟自定义模块--
				entities.addAll(onFlowCaseCustomDetailRender(flowCase, flowUserType));
				return entities;
			}
		}
		return null;
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

	@Override
	public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
		List<ServiceAllianceCategories> serviceAllianceCategories = yellowPageProvider.listChildCategories(null, null, namespaceId, 0L, null, null);
		if(serviceAllianceCategories == null || serviceAllianceCategories.size() == 0 ){
			return new ArrayList<>();
		}
		return serviceAllianceCategories.stream().map(r->{
			FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
			dto.setNamespaceId(namespaceId);
			dto.setServiceName(r.getName());
			return dto;
		}).collect(Collectors.toList());
	}
}
