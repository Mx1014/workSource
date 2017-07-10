package com.everhomes.techpark.expansion;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.building.Building;
import com.everhomes.community.ResourceCategoryAssignment;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.FlowCase;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.talent.TalentOwnerType;
import com.everhomes.rest.techpark.expansion.ApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.LeasePromotionFormDataSourceType;
import com.everhomes.server.schema.Tables;
import com.everhomes.talent.TalentProvider;
import com.everhomes.talent.TalentRequest;
import com.everhomes.talent.TalentRequestProvider;
import com.everhomes.talent.TalentService;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

import org.apache.commons.lang.StringUtils;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "EhTalents")
public class TalentApplyEntryFormHandler implements GeneralFormModuleHandler {

    @Autowired
    private TalentService talentService;
    @Autowired
    private GeneralFormService generalFormService;
    @Autowired
    private GeneralFormProvider generalFormProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private TalentRequestProvider talentRequestProvider;
    @Autowired
	private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;

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
        dbProvider.execute(s->{
        	Long formId = talentService.findRequestSetting().getFormId();
        	TalentRequest talentRequest = createTalentRequest(cmd, applyUserName2, contactPhone2, enterpriseName2, formId);
        	createFormValues(formId, talentRequest.getId(), cmd.getValues());
        	createFlowCase(talentRequest);
        	return null;
        });

        PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);

        List<PostApprovalFormItem> items = new ArrayList<>();
        PostApprovalFormItem item = new PostApprovalFormItem();
        item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
//        item.setFieldValue(JSONObject.toJSONString(response));

        items.add(item);
        dto.setValues(items);
//        dto.setCustomObject(JSONObject.toJSONString(response));
        return dto;
    }

	private void createFlowCase(TalentRequest talentRequest) {
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
//		createFlowCaseCommand.setContent(talentRequest.getContent());
//
//		createFlowCaseCommand.setProjectId(talentRequest.getOwnerId());
//		createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());
//
//		FlowCase flowCase = flowService.createDumpFlowCase(gm, createFlowCaseCommand);
//		task.setFlowCaseId(flowCase.getId());
//		pmTaskProvider.updateTask(task);
	}

	private void createFormValues(Long generalFormId, Long sourceId, List<PostApprovalFormItem> values) {
		addGeneralFormValuesCommand cmd = new addGeneralFormValuesCommand();
		cmd.setGeneralFormId(generalFormId);
		cmd.setSourceType(EntityType.TALENT_REQUEST.getCode());
		cmd.setSourceId(sourceId);
		cmd.setValues(values);
		generalFormService.addGeneralFormValues(cmd );
	}

	private TalentRequest createTalentRequest(PostGeneralFormCommand cmd, String applyUserName, String contactPhone, String enterpriseName, Long formId) {
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
    	talentRequestProvider.createTalentRequest(talentRequest);
    	return talentRequest;
	}

	private Long userId() {
		return UserContext.current().getUser().getId();
	}
	
	private Integer namespaceId() {
		return UserContext.getCurrentNamespaceId();
	} 
	
    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(cmd.getNamespaceId(),
                cmd.getOwnerId(), EntityType.COMMUNITY.getCode(), EntityType.LEASEPROMOTION.getCode());

        GeneralFormDTO dto = null;
        if (null != request) {
            GetTemplateByFormIdCommand cmd2 = new GetTemplateByFormIdCommand();
            cmd2.setFormId(request.getSourceId());

            dto = generalFormService.getTemplateByFormId(cmd2);
            List<GeneralFormFieldDTO> temp = dto.getFormFields();

            //查询初始默认数据
            ApplyEntryBuildingFormHandler handler = PlatformContext.getComponent(
                    GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + EntityType.BUILDING.getCode());

            GeneralForm form = handler.getDefaultGeneralForm(EntityType.LEASEPROMOTION.getCode());
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);

            fieldDTOs.addAll(temp);
            dto.setFormFields(fieldDTOs);
        } else {
            //查询初始默认数据
            ApplyEntryBuildingFormHandler handler = PlatformContext.getComponent(
                    GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + EntityType.BUILDING.getCode());

            GeneralForm form = handler.getDefaultGeneralForm(EntityType.LEASEPROMOTION.getCode());

            dto = ConvertHelper.convert(form, GeneralFormDTO.class);
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);

            dto.setFormFields(fieldDTOs);
        }

        return dto;
    }

}
