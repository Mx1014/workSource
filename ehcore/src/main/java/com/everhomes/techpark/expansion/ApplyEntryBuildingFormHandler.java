package com.everhomes.techpark.expansion;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.entity.EntityType;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.techpark.expansion.ApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryCommand;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
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

        String json = null;

        EnterpriseApplyEntryCommand cmd2 = JSONObject.parseObject(json, EnterpriseApplyEntryCommand.class);

        for (PostApprovalFormItem item: cmd.getValues()) {
            GeneralFormDataSourceType dataSourceType = GeneralFormDataSourceType.fromCode(item.getFieldName());
            if (null != dataSourceType) {
                switch (dataSourceType) {
                    case USER_NAME:
                        cmd2.setApplyUserName(JSON.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class).getText());
                        break;
                    case USER_PHONE:
                        cmd2.setContactPhone(JSON.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class).getText());
                        break;
                    case USER_COMPANY:
                        //工作流images怎么传
                        cmd2.setEnterpriseName(JSON.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class).getText());
                        break;
                    case ORGANIZATION_ID:

                        break;
                    case USER_ADDRESS:
                        break;

                }
            }

        }

        ApplyEntryResponse response = enterpriseApplyEntryService.applyEntry(cmd2);
        PostGeneralFormDTO dto = new PostGeneralFormDTO();
        dto.setCustomObject(JSONObject.toJSONString(response));
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
            GeneralForm form = this.generalFormProvider.getGeneralFormById(56L);
            if(form == null )
                throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                        GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "form not found");

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
        organizationIdField.setFieldName("buildingName");
        organizationIdField.setFieldDisplayName("楼栋名称");
        organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.EDITABLE.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setValidatorType(GeneralFormValidatorType.TEXT_LIMIT.getCode());
        organizationIdField.setFieldExtra("{\"limitWord\":20}");
        dtos.add(organizationIdField);

        organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setFieldName("description");
        organizationIdField.setFieldDisplayName("备注说明");
        organizationIdField.setFieldType(GeneralFormFieldType.MULTI_LINE_TEXT.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NONEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.EDITABLE.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setValidatorType(GeneralFormValidatorType.TEXT_LIMIT.getCode());
        dtos.add(organizationIdField);

        return dtos;
    }
}
