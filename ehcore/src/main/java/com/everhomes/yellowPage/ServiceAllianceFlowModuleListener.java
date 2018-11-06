package com.everhomes.yellowPage;

import static com.everhomes.yellowPage.YellowPageUtils.throwError;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.asset.TargetDTO;
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
import com.everhomes.rest.yellowPage.ServiceAllianceWorkFlowStatus;
import com.everhomes.rest.yellowPage.YellowPageServiceErrorCode;
import com.everhomes.rest.yellowPage.standard.ConfigCommand;
import com.everhomes.rest.yellowPage.stat.StatClickOrSortType;
import com.everhomes.user.*;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEvaluate;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalFieldProcessor;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Tuple;
import com.everhomes.util.file.FileUtils;
import com.everhomes.util.pdf.PdfUtils;
import com.everhomes.yellowPage.standard.ServiceCategoryMatch;
import com.everhomes.yellowPage.stat.ClickStatDetail;
import com.everhomes.yellowPage.stat.ClickStatDetailProvider;
@Component
public class ServiceAllianceFlowModuleListener implements FlowModuleListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAllianceFlowModuleListener.class);
	
	private final String ALLIANCE_TEMPLATE_TYPE = "flowCase";
	private final String ALLIANCE_WORK_FLOW_STATUS_FIELD_NAME = "workflowStatus";
	private final String FORM_VAL_SOURCE_TYPE_NAME = EhFlowCases.class.getSimpleName();
	private static List<String> DEFAULT_FIELDS = new ArrayList<>();

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
	private ServiceAllianceApplicationRecordProvider saapplicationRecordProvider;

    @Autowired
	protected ContentServerService contentServerService;
	@Autowired
	protected ServiceModuleProvider serviceModuleProvider;
	@Autowired
	protected GeneralFormService generalFormService;
    @Autowired
    protected GeneralFormProvider generalFormProvider;
    @Autowired
    protected GeneralFormValProvider generalFormValProvider;
    @Autowired
    protected LocaleStringService localeStringService;
    @Autowired
    protected OrganizationProvider organizationProvider;
	
	@Autowired
	ServiceAllianceProviderProvider serviceAllianceProvidProvider;
    @Autowired
    protected GeneralApprovalFieldProcessor generalApprovalFieldProcessor;
    
	@Autowired
	AllianceStandardService allianceStandardService;

    public ServiceAllianceFlowModuleListener() {
        for (GeneralFormDataSourceType value : GeneralFormDataSourceType.values()) {
            DEFAULT_FIELDS.add(value.getCode());
        }
    }

	@Autowired
	ClickStatDetailProvider detailProvider;

	@Override
	public FlowModuleInfo initModule() {
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = serviceModuleProvider.findServiceModuleById(MODULE_ID);
        moduleInfo.setModuleName(module.getName());
        moduleInfo.setModuleId(MODULE_ID);
        return moduleInfo;
	}

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		try{
			sOnFlowCaseCreating(flowCase);
		}catch (Exception e) {
			LOGGER.info("sOnFlowCaseCreating : ",e);
			throw e;
		}
	}

	public void sOnFlowCaseCreating(FlowCase flowCase) {
		//旧表单直接退出
		if(flowCase.getOwnerType()!=null && !FlowOwnerType.SERVICE_ALLIANCE.getCode().equals(flowCase.getOwnerType()))
			return;

		// 服务联盟的审批拼接工作流 content字符串
		String formItemsInfo = flowCase.getContent();
		
		PostGeneralFormValCommand cmd = JSONObject.parseObject(formItemsInfo, PostGeneralFormValCommand.class);
		List<PostApprovalFormItem> values = cmd.getValues();
		ServiceAlliances  yellowPage = yellowPageProvider.findServiceAllianceById(flowCase.getReferId(),null,null); 
		if (null == yellowPage) {
			LOGGER.error("formItemsInfo:"+formItemsInfo);
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_FLOW_CASE_SERVICE_NOT_FOUND, "service not found");
			return;
		}
		
		ConfigCommand configCmd = allianceStandardService.reNewConfigCommand(yellowPage.getOwnerType(),
				yellowPage.getOwnerId(), yellowPage.getParentId());
		ServiceCategoryMatch match = allianceStandardService.findServiceCategory(configCmd.getOwnerType(),
				configCmd.getOwnerId(), yellowPage.getParentId(), yellowPage.getId());
		if (null != match) {
			yellowPage.setCategoryId(match.getCategoryId());
			yellowPage.setServiceType(match.getCategoryName());
		}
		
		StringBuffer contentBuffer = new StringBuffer();
		contentBuffer.append("服务名称 : ");
		contentBuffer.append(yellowPage.getName());
		flowCase.setContent(contentBuffer.toString());

		Byte status = ServiceAllianceWorkFlowStatus.PROCESSING.getCode();
		if(FlowCaseType.fromCode(flowCase.getCaseType()) == FlowCaseType.DUMB){
			status = ServiceAllianceWorkFlowStatus.NONE.getCode();
		}
		
		//同步申请到es
		ServiceAllianceRequestInfo request = new ServiceAllianceRequestInfo();
		request.setServiceAllianceId(cmd.getSourceId());
		request.setType(yellowPage.getParentId());
		syncRequest(values, request, flowCase,yellowPage,status);
		
		//添加到埋点统计中
		createCommitStatDetail(flowCase, yellowPage);

		//发送消息给相关人员
		sendMessage(yellowPage,flowCase, formItemsInfo);
	}

	private void createCommitStatDetail(FlowCase flowCase, ServiceAlliances service) {
		if (null == service) {
			return;
		}
		
		//有可能categoryId为空
		Long categoryId = service.getCategoryId();
		if (null == categoryId) {
			categoryId = service.getParentId();
		}

		ClickStatDetail detail = new ClickStatDetail();
		detail.setNamespaceId(flowCase.getNamespaceId());
		detail.setType(service.getParentId());
		detail.setOwnerId(service.getOwnerId());
		detail.setCategoryId(categoryId == null ? 0L : categoryId);
		detail.setServiceId(service.getId());
		detail.setClickType(StatClickOrSortType.CLICK_TYPE_COMMIT_TIMES.getCode());
		detail.setClickTime(System.currentTimeMillis());

		// 设置用户相关信息
		detail.setUserId(flowCase.getApplyUserId());
		TargetDTO dto = organizationProvider.findUserContactByUserId(flowCase.getNamespaceId(),
				flowCase.getApplyUserId());
		if (null != dto) {
			detail.setUserName(dto.getTargetName());
			detail.setUserPhone(dto.getUserIdentifier());
		}

		detailProvider.createClickStatDetail(detail);
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
		request.setOwnerType(sa.getOwnerType());
		request.setOwnerId(sa.getOwnerId());
		request.setFlowCaseId(flowCase.getId());
		request.setCreatorUid(UserContext.current().getUser().getId());
		request.setSecondCategoryId(sa.getCategoryId());
		request.setSecondCategoryName(sa.getServiceType());
		request.setWorkflowStatus(status);
		request.setId(flowCase.getId());
		request.setTemplateType(ALLIANCE_TEMPLATE_TYPE);
		ServiceAllianceApplicationRecord record = ConvertHelper.convert(request, ServiceAllianceApplicationRecord.class);
		
		// issue-32529 web化表单提交成功后，后台不显示表单已填写/自动加载的企业
		PostApprovalFormItem companyVal = getFormFieldDTO(GeneralFormDataSourceType.USER_COMPANY.getCode(),values);
		String companyName = "";
		//5.9.0上线判断企业名称非空
		if(companyVal != null)
			companyName = JSON.parseObject(companyVal.getFieldValue(), PostApprovalFormTextValue.class).getText();
		if (!StringUtils.isBlank(companyName)) {
			record.setCreatorOrganization(companyName);
			request.setCreatorOrganization(companyName);
		}
			
		record.setServiceOrganization(sa.getName()); //这里储存服务名称，如有修改，需同步修改listUiServiceRecords
		record.setNamespaceId(flowCase.getNamespaceId());
		record.setServiceAllianceId(request.getServiceAllianceId());
		saapplicationRecordProvider.createServiceAllianceApplicationRecord(record);
		serviceAllianceRequestInfoSearcher.feedDoc(request);
	}

	private void sendMessage(ServiceAlliances sa, FlowCase flowCase, String formItemsInfo) {
		// 获取当前服务和服务类型
		ServiceAllianceCategories category = yellowPageProvider.findCategoryById(sa.getParentId());
		
		// 推送手机信息
		sendPhoneMsg(sa, category, flowCase);
		
		//异步推送邮件信息
		sendEmailAsynchronizedTask(sa, category, flowCase, formItemsInfo);
		
	}
	
	/**   
	* @Description: 发送短信通知
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年6月25日 下午2:00:02 
	*
	*/
	private void sendPhoneMsg(ServiceAlliances serviceOrg, ServiceAllianceCategories category, FlowCase flowCase) {

		// 发消息给服务联盟机构管理员
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<ServiceAllianceNotifyTargets> targets = yellowPageProvider.listNotifyTargets(category.getNamespaceId(),
				ContactType.MOBILE.getCode(), serviceOrg.getParentId(), locator, Integer.MAX_VALUE);

		if ((serviceOrg.getContactMemid() != null && serviceOrg.getContactMemid() != 0)
				|| (targets != null && targets.size() > 0)) {
			String body = new StringBuffer().append("收到一条").append(category.getName()).append("的申请,服务名称：")
					.append(serviceOrg.getName()).toString();
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

			if (serviceOrg.getContactMemid() != null && serviceOrg.getContactMemid() != 0) {
				OrganizationMember member = organizationProvider
						.findOrganizationMemberById(serviceOrg.getContactMemid());

				if (member != null && member.getTargetId() != null) {
					MessageDTO messageDto = createMessageDto(body, meta, member.getTargetId().toString());
					messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING,
							MessageChannelType.USER.getCode(), member.getTargetId().toString(), messageDto,
							MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
				}
			}

			if (targets != null && targets.size() > 0) {
				for (ServiceAllianceNotifyTargets target : targets) {
					if (target.getStatus().byteValue() == 1) {
						UserIdentifier contact = userProvider.findClaimedIdentifierByToken(
								UserContext.getCurrentNamespaceId(), target.getContactToken());
						if (contact != null) {
							MessageDTO message = createMessageDto(body, meta, contact.getOwnerUid().toString());
							messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING,
									MessageChannelType.USER.getCode(), contact.getOwnerUid().toString(), message,
									MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
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
	}
	
	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {
	}

	private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		if (flowCase.getOwnerType() != null && !FlowOwnerType.SERVICE_ALLIANCE.getCode().equals(flowCase.getOwnerType()))
			return this.processCustomRequest(flowCase);

		List<FlowCaseEntity> entities = new ArrayList<>();
		if (flowCase.getCreateTime() != null) {
			entities.add(new FlowCaseEntity("申请时间", flowCase.getCreateTime().toLocalDateTime().format(fmt), FlowCaseEntityType.MULTI_LINE.getCode()));
		}

		List<GeneralFormVal> vals = generalFormValProvider.queryGeneralFormVals(FORM_VAL_SOURCE_TYPE_NAME,
				flowCase.getId());
		if (CollectionUtils.isEmpty(vals)) {
			return null;
		}

		List<PostApprovalFormItem> valItems = vals.stream().map(r -> {
			return ConvertHelper.convert(r, PostApprovalFormItem.class);
		}).collect(Collectors.toList());


		ServiceAlliances yellowPage = yellowPageProvider.findServiceAllianceById(flowCase.getReferId(), null, null);
		if (null == yellowPage) {
			LOGGER.error("flowCaseInfo:"+flowCase.toString());
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_FLOW_CASE_SERVICE_NOT_FOUND, "service not found");
		}
		
		entities.add(new FlowCaseEntity("服务名称", yellowPage.getName(), FlowCaseEntityType.MULTI_LINE.getCode()));
		entities.add(new FlowCaseEntity("服务类型", yellowPage.getServiceType(), FlowCaseEntityType.MULTI_LINE.getCode()));

		//前面写服务联盟特有的默认字段-姓名-电话-企业-申请类型-申请来源-服务机构
		//姓名
		GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
				vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
		List<GeneralFormFieldDTO> fieldDTOs;
		GeneralFormFieldDTO dto;
		if (null == form) {
			return null;
		}

		fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		PostApprovalFormItem val = getFormFieldDTO(GeneralFormDataSourceType.USER_NAME.getCode(),valItems);
		if(val!=null){
			dto = getFieldDTO(val.getFieldName(), fieldDTOs);
			entities.add(new FlowCaseEntity(dto.getFieldDisplayName(), JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText(), FlowCaseEntityType.MULTI_LINE.getCode()));
		}
		//电话
		val = getFormFieldDTO(GeneralFormDataSourceType.USER_PHONE.getCode(),valItems);
		if (val != null) {
			dto = getFieldDTO(val.getFieldName(), fieldDTOs);
			entities.add(new FlowCaseEntity(dto.getFieldDisplayName(), JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText(), FlowCaseEntityType.MULTI_LINE.getCode()));
		}

		//企业
		val = getFormFieldDTO(GeneralFormDataSourceType.USER_COMPANY.getCode(),valItems);
		if (val != null) {
			dto = getFieldDTO(val.getFieldName(), fieldDTOs);
			entities.add(new FlowCaseEntity(dto.getFieldDisplayName(), JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText(), FlowCaseEntityType.MULTI_LINE.getCode()));
		}

		//楼栋门牌
		FlowCaseEntity e = new FlowCaseEntity();
		val = getFormFieldDTO(GeneralFormDataSourceType.USER_ADDRESS.getCode(),valItems);
		if (val != null) {
			dto = getFieldDTO(val.getFieldName(), fieldDTOs);
			e.setKey(dto.getFieldDisplayName());
			e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
			String textValue = JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText();
			if (!StringUtils.isBlank(textValue)) {
				e.setValue(textValue);
				entities.add(e);
			}
		}

		//后面跟自定义模块--
		entities.addAll(onFlowCaseCustomDetailRender(flowCase, flowUserType));

		//把客户端或前端需要的东西放到OBEJECT中
		flowCase.setCustomObject(getCustomObject(flowCase, entities));

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
	
	/**   
	* @Description: 异步邮件请求。
	* 执行类：ServiceAllianceAsynchronizedAction
	* 需要参数：namespaceId, serviceAllianceId,  userId, organizationId, formItemsInfo
	* 注：formItemsInfo格式为PostApprovalFormCommand 的json字符串
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年6月28日 上午10:49:30 
	*
	*/
	private void sendEmailAsynchronizedTask(ServiceAlliances sa, ServiceAllianceCategories category, FlowCase flowCase, String formItemsInfo) {
		
		JSONObject json = new JSONObject();
		json.put("namespaceId", category.getNamespaceId());
		json.put("serviceAllianceId", sa.getId());
		json.put("userId", flowCase.getApplyUserId());
		json.put("organizationId", flowCase.getApplierOrganizationId());
		json.put("formItemsInfo", formItemsInfo);
		
		//异步邮件
		ServiceAllianceAsynchronizedServiceImpl handler = PlatformContext.getComponent("serviceAllianceAsynchronizedServiceImpl");
		handler.pushToQueque(json.toJSONString());
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
		return null;
	}
	
	@Override
	/*
	 * 评价完成后，对新建事件进行评分更新
	 * */
    public void onFlowCaseEvaluate(FlowCaseState ctx, List<FlowEvaluate> evaluates) {
		
		//获取工作流
		FlowCase flowCase = ctx.getFlowCase();
		if (!FlowOwnerType.SERVICE_ALLIANCE.getCode().equals(flowCase.getOwnerType())
				|| FlowModuleType.NO_MODULE.getCode().equals(flowCase.getModuleType())) {
			// 非表单审核项，直接返回
			return;
		}
		
		// 如果评价项是空，直接返回
		if (CollectionUtils.isEmpty(evaluates)) {
			return;
		}
		
		// 对每一项进行更新
		for (FlowEvaluate item : evaluates) {
			serviceAllianceProvidProvider.updateScoreByEvaluation(flowCase.getId(), item);
		}
    }
	
	/**
	 * 根据工作流保存一些业务信息
	 * @param approvalId
	 * @return
	 */
	private String getCustomObject(FlowCase flowCase, List<FlowCaseEntity> entities) {

		JSONObject json = new JSONObject();
		for (FlowCaseEntity entity : entities) {
			json.put(entity.getKey(), entity.getValue());
		}

		fillEnanbleProvider(json, flowCase);

		return json.toJSONString();
	}

	private void fillEnanbleProvider(JSONObject json, FlowCase flowCase) {

		if (!FlowOwnerType.SERVICE_ALLIANCE.getCode().equals(flowCase.getOwnerType())) {
			return;
		}

		Byte enableProvider = (byte) 0;
		ServiceAllianceCategories sc = allianceStandardService.queryHomePageCategoryByScene(flowCase.getOwnerId(), flowCase.getProjectId());
		if (null != sc && null != sc.getEnableProvider()) {
			enableProvider = sc.getEnableProvider();
		}

		json.put("enableProvider", enableProvider);
	}

	@Override
	public void onFlowCaseStateChanged(FlowCaseState ctx) {

		
		FlowCase flowCase = ctx.getFlowCase();
		
		reNewEsWorkFlowStatus(flowCase.getId(), flowCase.getStatus());
		
	}
	
	/**   
	* @Function: ServiceAllianceFlowModuleListener.java
	* @Description: 更新es服务器里服务的状态
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年6月20日 下午1:54:27 
	*
	*/
	private void reNewEsWorkFlowStatus(Long flowCaseId, Byte flowCaseStatus) {

		// 将工作流状态转成服务状态
		ServiceAllianceWorkFlowStatus newStatus = ServiceAllianceWorkFlowStatus.getFromFlowCaseStatus(flowCaseStatus);
		
		//获取储存在数据库的记录
		ServiceAllianceApplicationRecord record = saapplicationRecordProvider.findServiceAllianceApplicationRecordByFlowCaseId(flowCaseId);
		if (null == record) {
			return ;
		}
		
		//更新数据库记录
		record.setWorkflowStatus(newStatus.getCode());
		saapplicationRecordProvider.updateServiceAllianceApplicationRecord(record);

		// 更新es存储的服务状态
		serviceAllianceRequestInfoSearcher.updateDocByField(flowCaseId, ALLIANCE_TEMPLATE_TYPE,
				ALLIANCE_WORK_FLOW_STATUS_FIELD_NAME, newStatus.getCode());

	}
	
	@Override
	public void onFlowButtonFired(FlowCaseState ctx) {
		
		if (FlowStepType.SUSPEND_STEP != ctx.getStepType() && FlowStepType.ABORT_SUSPEND_STEP != ctx.getStepType()) {
			return;
		}

		FlowCase flowCase = ctx.getFlowCase();
		
		byte newFlowCaseStatus = FlowCaseStatus.SUSPEND.getCode();
		if (FlowStepType.ABORT_SUSPEND_STEP == ctx.getStepType()) {
			newFlowCaseStatus = FlowCaseStatus.PROCESS.getCode();
		} 
		
		//更新es状态 #31378
		reNewEsWorkFlowStatus(flowCase.getId(), newFlowCaseStatus);
	}

	
    protected GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
        for (GeneralFormFieldDTO val : fieldDTOs) {
            if (val.getFieldName().equals(fieldName))
                return val;
        }
        return null;
    }

    protected PostApprovalFormItem getFormFieldDTO(String string, List<PostApprovalFormItem> values) {
        for (PostApprovalFormItem val : values) {
            if (val.getFieldName().equals(string))
                return val;
        }
        return null;
    }

    protected List<FlowCaseEntity> onFlowCaseCustomDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        if (flowCase.getReferType().equals(FlowReferType.SERVICE_ALLIANCE.getCode())) {
            List<GeneralFormVal> vals = generalFormValProvider.queryGeneralFormVals(FORM_VAL_SOURCE_TYPE_NAME, flowCase.getId());
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
                    vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
            // 模板设定的字段DTOs
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
                    GeneralFormFieldDTO.class);
            processEntities(entities, vals, fieldDTOs);

        }
        return entities;
    }


    private void processEntities(
            List<FlowCaseEntity> entities, List<GeneralFormVal> vals, List<GeneralFormFieldDTO> fieldDTOs) {

        for (GeneralFormVal val : vals) {
            try {
                if (!DEFAULT_FIELDS.contains(val.getFieldName())) {
                    // 不在默认fields的就是自定义字符串，组装这些
                    FlowCaseEntity e = new FlowCaseEntity();
                    GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(), fieldDTOs);
                    if (null == dto) {
                        //  just process the subForm value (there is no more subForm)
                        continue;
                    }
                    e.setKey(dto.getFieldDisplayName() == null ? dto.getFieldName() : dto.getFieldDisplayName());
                    switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
                        case SINGLE_LINE_TEXT:
                        case NUMBER_TEXT:
                        case DATE:
                        case DROP_BOX:
                            generalApprovalFieldProcessor.processDropBoxField(entities, e, val.getFieldValue());
                            break;
                        case MULTI_LINE_TEXT:
                            generalApprovalFieldProcessor.processMultiLineTextField(entities, e, val.getFieldValue());
                            break;
                        case IMAGE:
                            generalApprovalFieldProcessor.processImageField(entities, e, val.getFieldValue());
                            break;
                        case FILE:
                            generalApprovalFieldProcessor.processFileField(entities, e, val.getFieldValue());
                            break;
                        case INTEGER_TEXT:
                            generalApprovalFieldProcessor.processIntegerTextField(entities, e, val.getFieldValue());
                            break;
                        case SUBFORM:
                            generalApprovalFieldProcessor.processSubFormField(entities, dto, val.getFieldValue());
                            break;
                        case CONTACT:
                            //企业联系人
                            generalApprovalFieldProcessor.processContactField(entities, e, val.getFieldValue());
                            break;
                    }
                }
            } catch (NullPointerException e) {
                LOGGER.error(" ********** 空指针错误  val = " + JSON.toJSONString(val), e);
            } catch (Exception e) {
                LOGGER.error(" ********** 这是什么错误  = " + JSON.toJSONString(val), e);

            }
        }
    }
}
