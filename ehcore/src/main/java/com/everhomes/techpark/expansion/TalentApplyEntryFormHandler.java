package com.everhomes.techpark.expansion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GetTemplateByFormIdCommand;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.general_approval.addGeneralFormValuesCommand;
import com.everhomes.rest.group.GroupNotificationTemplateCode;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.talent.TalentOwnerType;
import com.everhomes.rest.techpark.expansion.ApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.LeasePromotionFormDataSourceType;
import com.everhomes.talent.Talent;
import com.everhomes.talent.TalentMessageSender;
import com.everhomes.talent.TalentMessageSenderProvider;
import com.everhomes.talent.TalentProvider;
import com.everhomes.talent.TalentRequest;
import com.everhomes.talent.TalentRequestProvider;
import com.everhomes.talent.TalentService;
import com.everhomes.talent.TalentTemplateCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "EhTalents")
public class TalentApplyEntryFormHandler implements GeneralFormModuleHandler {

    @Autowired
    private TalentService talentService;
    @Autowired
    private GeneralFormService generalFormService;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private TalentRequestProvider talentRequestProvider;
    @Autowired
	private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
    @Autowired
    private FlowService flowService;
    @Autowired
    private TalentProvider talentProvider;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private TalentMessageSenderProvider talentMessageSenderProvider;
    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Override
    public PostGeneralFormDTO postGeneralForm(PostGeneralFormCommand cmd) {

        List<PostApprovalFormItem> values = cmd.getValues();

        String json = null;
        String applyUserName = null;
        String contactPhone = null;
        String enterpriseName = null;
        String description = null;

        for (PostApprovalFormItem item: values) {
            GeneralFormDataSourceType dataSourceType = GeneralFormDataSourceType.fromCode(item.getFieldName());

            LeasePromotionFormDataSourceType rentSourceType = LeasePromotionFormDataSourceType.fromCode(item.getFieldName());
            if (null != dataSourceType) {
                switch (dataSourceType) {
                    case USER_NAME:
                        applyUserName = JSON.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class).getText();
                        break;
                    case USER_PHONE:
                        contactPhone = JSON.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class).getText();
                        break;
                    case USER_COMPANY:
                        //工作流images怎么传
                        enterpriseName = JSON.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class).getText();
                        break;
                    case CUSTOM_DATA:
                        json = JSON.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class).getText();
                        break;
                }
            }

            if (null != rentSourceType) {
                switch (rentSourceType) {
                    case LEASE_PROMOTION_BUILDING:
                        break;
                    case LEASE_PROMOTION_APARTMENT:
                        break;
                    case LEASE_PROMOTION_DESCRIPTION:
                        description = JSON.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class).getText();
                        break;
                }
            }

        }
        final String applyUserName2 = applyUserName;
        final String contactPhone2 = contactPhone;
        final String enterpriseName2 = enterpriseName;
        FlowCase flowCase = dbProvider.execute(s->{
        	Long formId = talentService.findRequestSetting().getFormId();
        	TalentRequest talentRequest = createTalentRequest(cmd, applyUserName2, contactPhone2, enterpriseName2, formId);
        	createFormValues(formId, talentRequest.getId(), cmd.getValues());
        	FlowCase flowCase1 =  createFlowCase(talentRequest);
        	sendMessage(talentRequest, flowCase1);
        	return flowCase1;
        });

        PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);

        List<PostApprovalFormItem> items = new ArrayList<>();
        PostApprovalFormItem item = new PostApprovalFormItem();
        item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        item.setFieldValue(JSONObject.toJSONString(new ApplyEntryResponse(processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId()))));

        items.add(item);
        dto.setValues(items);
//        dto.setCustomObject(JSONObject.toJSONString(response));
        return dto;
    }
    
	private void sendMessage(TalentRequest talentRequest, FlowCase flowCase) {
		List<TalentMessageSender> talentMessageSenders = talentMessageSenderProvider.listTalentMessageSenderByOwner(talentRequest.getOwnerType(), talentRequest.getOwnerId());
		if (talentMessageSenders.isEmpty()) {
			return ;
		}
		List<Long> includeList = talentMessageSenders.parallelStream().map(TalentMessageSender::getUserId).collect(Collectors.toList());
		
		String scope = TalentTemplateCode.SCOPE;
        int code = TalentTemplateCode.REQUEST_MESSAGE;
        String text = localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), null, "");
		String[] textSplit = text.split("|");
		
		String uri = processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId());
		
		sendMessage(includeList, uri, textSplit[0], textSplit[1]);
	}

	private void sendMessage(List<Long> includeList, String routerUri, String title, String message) {
		MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(message);
        messageDto.setMetaAppId(AppConstants.APPID_GROUP);

        RouterMetaObject mo = new RouterMetaObject();
        mo.setUrl(routerUri);
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(mo));
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, title);
        messageDto.setMeta(meta);

        includeList.stream().distinct().forEach(targetId -> {
            messageDto.setChannels(Collections.singletonList(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(targetId))));
            messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
                    AppConstants.APPID_MESSAGING, ChannelType.USER.getCode(), String.valueOf(targetId),
                    messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
        });
	}

	private String processFlowURL(Long flowCaseId, String string, Long moduleId) { 
		return "zl://workflow/detail?flowCaseId="+flowCaseId+"&flowUserType="+string+"&moduleId="+moduleId  ;
	}
	
	private FlowCase createFlowCase(TalentRequest talentRequest) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		GeneralModuleInfo gm = new GeneralModuleInfo();

		gm.setNamespaceId(namespaceId);
		gm.setOwnerType(FlowOwnerType.TALENT_REQUEST.getCode());
		gm.setOwnerId(talentRequest.getId());
		gm.setModuleType(FlowModuleType.NO_MODULE.getCode());
		gm.setModuleId(FlowConstants.TALENT_REQUEST);
		gm.setProjectId(talentRequest.getOwnerId());
		gm.setProjectType(EntityType.COMMUNITY.getCode());

		CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
		createFlowCaseCommand.setApplyUserId(talentRequest.getCreatorUid());
//		createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
//		createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
		createFlowCaseCommand.setReferId(talentRequest.getId());
		createFlowCaseCommand.setReferType(EntityType.TALENT_REQUEST.getCode());
		//createFlowCaseCommand.setContent("发起人：" + requestorName + "\n" + "联系方式：" + requestorPhone);
		createFlowCaseCommand.setContent(talentRequest.getContent());

		createFlowCaseCommand.setProjectId(talentRequest.getOwnerId());
		createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());

		FlowCase flowCase = flowService.createDumpFlowCase(gm, createFlowCaseCommand);
		talentRequest.setFlowCaseId(flowCase.getId());
		talentRequestProvider.updateTalentRequest(talentRequest);
		return flowCase;
	}

	private void createFormValues(Long generalFormId, Long sourceId, List<PostApprovalFormItem> values) {
		addGeneralFormValuesCommand cmd = new addGeneralFormValuesCommand();
		cmd.setGeneralFormId(generalFormId);
		cmd.setSourceType(EntityType.TALENT_REQUEST.getCode());
		cmd.setSourceId(sourceId);
		cmd.setValues(values);
		generalFormService.addGeneralFormValues(cmd);
	}

	private TalentRequest createTalentRequest(PostGeneralFormCommand cmd, String applyUserName, String contactPhone, String enterpriseName, Long formId) {
		Talent talent = talentProvider.findTalentById(cmd.getSourceId());
		TalentRequest talentRequest = new TalentRequest();
    	talentRequest.setNamespaceId(namespaceId());
    	talentRequest.setOwnerType(TalentOwnerType.COMMUNITY.getCode());
    	talentRequest.setOwnerId(cmd.getOwnerId());
    	talentRequest.setRequestor(applyUserName);
    	talentRequest.setPhone(contactPhone);
    	talentRequest.setOrganizationName(enterpriseName);
    	talentRequest.setTalentId(cmd.getSourceId());
    	talentRequest.setStatus(CommonStatus.ACTIVE.getCode());
    	talentRequest.setFormOriginId(formId);
    	talentRequest.setContent("人才姓名："+talent.getName()+"\n求职意向："+talent.getPosition());
    	talentRequestProvider.createTalentRequest(talentRequest);
    	return talentRequest;
	}
	
	private Integer namespaceId() {
		return UserContext.getCurrentNamespaceId();
	} 
	
    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        return generalFormService.getTemplateByFormId(new GetTemplateByFormIdCommand(talentService.findRequestSetting().getFormId()));
    }

}
