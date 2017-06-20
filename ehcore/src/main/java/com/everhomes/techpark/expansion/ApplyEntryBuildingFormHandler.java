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
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "EhBuildings")
public class ApplyEntryBuildingFormHandler implements GeneralFormModuleHandler {

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
                cmd.getOwnerId(), EntityType.COMMUNITY.getCode(), EntityType.BUILDING.getCode());

        Long requestFormId = null;
        if (null == request) {
            //查询初始默认数据
            ApplyEntryBuildingFormHandler handler = PlatformContext.getComponent(
                    GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + EntityType.BUILDING.getCode());

            GeneralForm form = handler.getDefaultGeneralForm();
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
        return dto;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {

        LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(cmd.getNamespaceId(),
                cmd.getOwnerId(), EntityType.COMMUNITY.getCode(), EntityType.BUILDING.getCode());

        GeneralFormDTO dto = new GeneralFormDTO();

        if (null != request) {
            GetTemplateByFormIdCommand cmd2 = new GetTemplateByFormIdCommand();
            cmd2.setFormId(request.getSourceId());

            dto = generalFormService.getTemplateByFormId(cmd2);
            List<GeneralFormFieldDTO> temp = dto.getFormFields();
            List<GeneralFormFieldDTO> fileds = createFiled();
            fileds.addAll(temp);
            dto.setFormFields(fileds);
        } else {
            //查询初始默认数据
            GeneralForm form = getDefaultGeneralForm();

            dto = ConvertHelper.convert(form, GeneralFormDTO.class);
//		form.setFormVersion(form.getFormVersion());
//            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
//            List<GeneralFormFieldDTO> temp = dto.getFormFields();
            List<GeneralFormFieldDTO> fields = createFiled();
//            fields.addAll(temp);
            dto.setFormFields(fields);
        }


        return dto;
    }

    GeneralForm getDefaultGeneralForm() {
        List<GeneralForm> forms = this.generalFormProvider.queryGeneralForms(new ListingLocator(),
                Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                        SelectQuery<? extends Record> query) {
                        query.addConditions(Tables.EH_GENERAL_FORMS.NAMESPACE_ID.eq(0));
                        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(0L));
                        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(EntityType.LEASEPROMOTION.getCode()));
                        return query;
                    }
                });
        if(forms == null || forms.isEmpty()) {
            throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                    GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "Init leasePromotion form not found");
        }

        GeneralForm form = forms.get(0);
        if(form == null ) {
            throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                    GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "Init leasePromotion form not found");
        }

        return form;
    }

    private List<GeneralFormFieldDTO> createFiled() {

        List<GeneralFormFieldDTO> dtos = new ArrayList<>();

        GeneralFormFieldDTO organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setFieldName(GeneralFormDataSourceType.USER_NAME.getCode());
        organizationIdField.setFieldDisplayName("用户姓名");
        organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.EDITABLE.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setDataSourceType(GeneralFormDataSourceType.USER_NAME.getCode());
        organizationIdField.setValidatorType(GeneralFormValidatorType.TEXT_LIMIT.getCode());
        organizationIdField.setFieldExtra("{\"limitWord\":10}");
        dtos.add(organizationIdField);

        organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setFieldName(GeneralFormDataSourceType.USER_PHONE.getCode());
        organizationIdField.setFieldDisplayName("手机号码");
        organizationIdField.setFieldType(GeneralFormFieldType.INTEGER_TEXT.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.EDITABLE.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setDataSourceType(GeneralFormDataSourceType.USER_PHONE.getCode());
        organizationIdField.setValidatorType(GeneralFormValidatorType.TEXT_LIMIT.getCode());
        organizationIdField.setFieldExtra("{\"limitWord\":11}");
        dtos.add(organizationIdField);

        organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setFieldName(GeneralFormDataSourceType.USER_COMPANY.getCode());
        organizationIdField.setFieldDisplayName("公司名称");
        organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.EDITABLE.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setDataSourceType(GeneralFormDataSourceType.USER_COMPANY.getCode());
        organizationIdField.setValidatorType(GeneralFormValidatorType.TEXT_LIMIT.getCode());
        organizationIdField.setFieldExtra("{\"limitWord\":20}");
        dtos.add(organizationIdField);

        organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setFieldName(LeasePromotionFormDataSourceType.LEASE_PROMOTION_BUILDING.getCode());
        organizationIdField.setFieldDisplayName("楼栋名称");
        organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.EDITABLE.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setDataSourceType(LeasePromotionFormDataSourceType.LEASE_PROMOTION_BUILDING.getCode());
        organizationIdField.setValidatorType(GeneralFormValidatorType.TEXT_LIMIT.getCode());
        organizationIdField.setFieldExtra("{\"limitWord\":20}");
        dtos.add(organizationIdField);

        organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setFieldName(LeasePromotionFormDataSourceType.LEASE_PROMOTION_DESCRIPTION.getCode());
        organizationIdField.setFieldDisplayName("备注说明");
        organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NONEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NONEED.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.EDITABLE.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setDataSourceType(LeasePromotionFormDataSourceType.LEASE_PROMOTION_DESCRIPTION.getCode());
        organizationIdField.setValidatorType(GeneralFormValidatorType.TEXT_LIMIT.getCode());
        dtos.add(organizationIdField);

        organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NONEED.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setDataSourceType(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        dtos.add(organizationIdField);
        return dtos;
    }
}
