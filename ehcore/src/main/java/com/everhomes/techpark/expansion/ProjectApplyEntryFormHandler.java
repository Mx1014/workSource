package com.everhomes.techpark.expansion;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.entity.EntityType;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.techpark.expansion.ApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.LeasePromotionFormDataSourceType;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "EhLeaseProjects")
public class ProjectApplyEntryFormHandler implements GeneralFormModuleHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectApplyEntryFormHandler.class);

    @Autowired
    private EnterpriseApplyEntryService enterpriseApplyEntryService;
    @Autowired
    private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Override
    public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {

        if (null == cmd.getSourceId()) {
            cmd.setSourceId(EnterpriseApplyEntryServiceImpl.DEFAULT_CATEGORY_ID);
        }

        LeaseFormRequest request = enterpriseApplyEntryService.getFormRequestByCommunityId(cmd.getNamespaceId(),
                cmd.getOwnerId(),cmd.getSourceType(),cmd.getSourceId());

        BuildingApplyEntryFormHandler handler = PlatformContext.getComponent(
                GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + EntityType.BUILDING.getCode());
        Long requestFormId;
        if (null == request || request.getSourceId() == null) {
            //查询初始默认数据
            GeneralForm form = handler.getDefaultGeneralForm(EntityType.LEASE_PROJECT.getCode());
            requestFormId = form.getFormOriginId();
        }else {
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(request.getSourceId());
            if (form == null) {
                GeneralForm defaultForm = handler.getDefaultGeneralForm(EntityType.LEASE_PROJECT.getCode());
                requestFormId = defaultForm.getFormOriginId();
            }else {
                requestFormId = request.getSourceId();
            }
        }
        List<PostApprovalFormItem> values = cmd.getValues();

        String json = null;
        String applyUserName = null;
        String contactPhone = null;
        String enterpriseName = null;
        String description = null;
        String communityId = null;
        String customerName = null;

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
                    case CUSTOMER_NAME:
                    	customerName = JSON.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class).getText();
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
                    case LEASE_PROJECT_NAME:
                        communityId = JSON.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class).getText();
                        break;
                }
            }

        }

        EnterpriseApplyEntryCommand cmd2 = JSONObject.parseObject(json, EnterpriseApplyEntryCommand.class);
        cmd2.setApplyUserName(applyUserName);
        cmd2.setContactPhone(contactPhone);
        cmd2.setEnterpriseName(enterpriseName);
        cmd2.setDescription(description);
        cmd2.setCustomerName(customerName);
        
        cmd2.setRequestFormId(requestFormId);
        cmd2.setNamespaceId(cmd.getNamespaceId());
        cmd2.setCommunityId(cmd.getOwnerId());
        cmd2.setFormValues(cmd.getValues());
        cmd2.setEnterpriseId(cmd.getCurrentOrganizationId());

        ApplyEntryResponse response = enterpriseApplyEntryService.applyEntry(cmd2);
        PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);

        List<PostApprovalFormItem> items = new ArrayList<>();
        PostApprovalFormItem item = new PostApprovalFormItem();
        item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        item.setFieldValue(JSONObject.toJSONString(response));

        items.add(item);
        dto.setValues(items);
//        dto.setCustomObject(JSONObject.toJSONString(response));
        return dto;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
//        LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(cmd.getNamespaceId(),
//                cmd.getOwnerId(), EntityType.COMMUNITY.getCode(), EntityType.LEASE_PROMOTION.getCode());
//
//        GeneralFormDTO dto;

        //查询初始默认数据
        BuildingApplyEntryFormHandler handler = PlatformContext.getComponent(
                GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + EntityType.BUILDING.getCode());

        return handler.getTemplateBySourceId(cmd);

//        GeneralForm form = handler.getDefaultGeneralForm(EntityType.LEASE_PROMOTION.getCode());
//        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);

//        if (null != request) {
//            try {
//                GetTemplateByFormIdCommand cmd2 = new GetTemplateByFormIdCommand();
//                cmd2.setFormId(request.getSourceId());
//                dto = generalFormService.getTemplateByFormId(cmd2);
//                fieldDTOs.addAll(dto.getFormFields());
//            }catch (Exception e) {
//                LOGGER.error("get Template By SourceId, cmd={}", cmd, e);
//                dto = ConvertHelper.convert(form, GeneralFormDTO.class);
//            }
//        } else {
//            dto = ConvertHelper.convert(form, GeneralFormDTO.class);
//        }
//        dto.setFormFields(fieldDTOs);

//        return dto;
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
        return null;
    }

}
