package com.everhomes.techpark.expansion;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.entity.EntityType;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.techpark.expansion.ApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.LeasePromotionFormDataSourceType;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "EhLeasePromotions")
public class EnterpriseApplyEntryFormHandler implements GeneralFormModuleHandler {

    @Autowired
    private EnterpriseApplyEntryService enterpriseApplyEntryService;
    @Autowired
    private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
    @Autowired
    private GeneralFormService generalFormService;
    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Override
    public PostGeneralFormDTO postGeneralForm(PostGeneralFormCommand cmd) {

        LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(cmd.getNamespaceId(),
                cmd.getOwnerId(), EntityType.COMMUNITY.getCode(), EntityType.LEASEPROMOTION.getCode());

        Long requestFormId = null;
        if (null == request) {
            //查询初始默认数据
            ApplyEntryBuildingFormHandler handler = PlatformContext.getComponent(
                    GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + EntityType.BUILDING.getCode());

            GeneralForm form = handler.getDefaultGeneralForm(EntityType.LEASEPROMOTION.getCode());
            requestFormId = form.getFormOriginId();
        }else {
            requestFormId = request.getSourceId();
        }
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

        EnterpriseApplyEntryCommand cmd2 = JSONObject.parseObject(json, EnterpriseApplyEntryCommand.class);
        cmd2.setApplyUserName(applyUserName);
        cmd2.setContactPhone(contactPhone);
        cmd2.setEnterpriseName(enterpriseName);
        cmd2.setDescription(description);

        cmd2.setRequestFormId(requestFormId);
        cmd2.setNamespaceId(cmd.getNamespaceId());
        cmd2.setCommunityId(cmd.getOwnerId());
        cmd2.setFormValues(cmd.getValues());

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
