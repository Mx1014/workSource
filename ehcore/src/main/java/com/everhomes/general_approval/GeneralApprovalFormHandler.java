package com.everhomes.general_approval;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "GENERAL_APPROVE")
public class GeneralApprovalFormHandler implements GeneralFormModuleHandler {

    @Autowired
    private GeneralApprovalService generalApprovalService;
    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;
    @Override
    public PostGeneralFormDTO postGeneralForm(PostGeneralFormCommand cmd) {

//        String json = null;
//
//
//        for (PostApprovalFormItem item: cmd.getValues()) {
//            GeneralFormDataSourceType sourceType = GeneralFormDataSourceType.fromCode(item.getFieldName());
//            boolean flag = false;
//            if (null != sourceType) {
//                switch (sourceType) {
//                    case CUSTOM_DATA:
//                        json = JSON.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class).getText();
//                        flag = true;
//                        break;
//                }
//            }
//
//            if (flag) {
//                break;
//            }
//        }
//
//        PostApprovalFormCommand cmd2 = JSONObject.parseObject(json, PostApprovalFormCommand.class);
        PostApprovalFormCommand cmd2 = new PostApprovalFormCommand();
        cmd2.setApprovalId(cmd.getSourceId());
        cmd2.setValues(cmd.getValues());
        cmd2.setOrganizationId(cmd.getCurrentOrganizationId());

        GetTemplateByApprovalIdResponse response = generalApprovalService.postApprovalForm(cmd2);
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
        //
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(cmd
                .getSourceId());

        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(ga
                .getFormOriginId());
        if(form == null )
            throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                    GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "form not found");
        form.setFormVersion(form.getFormVersion());
        List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
        fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        //增加一个隐藏的field 用于存放sourceId
        GeneralFormFieldDTO sourceIdField = new GeneralFormFieldDTO();
        sourceIdField.setDataSourceType(GeneralFormDataSourceType.SOURCE_ID.getCode());
        sourceIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        sourceIdField.setFieldName(GeneralFormDataSourceType.SOURCE_ID.getCode());
        sourceIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        sourceIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        sourceIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        sourceIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
        fieldDTOs.add(sourceIdField);

        GeneralFormFieldDTO organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setDataSourceType(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
        organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        organizationIdField.setFieldName(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
        fieldDTOs.add(organizationIdField);

        GeneralFormFieldDTO customField = new GeneralFormFieldDTO();
        customField.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        customField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        customField.setRequiredFlag(NormalFlag.NONEED.getCode());
        customField.setDynamicFlag(NormalFlag.NONEED.getCode());
        customField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
        customField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        customField.setDataSourceType(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        customField.setFieldValue(JSONObject.toJSONString(ga));
        fieldDTOs.add(customField);

        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        dto.setFormFields(fieldDTOs);
//        dto.setCustomObject(JSONObject.toJSONString(ga));
        return dto;
    }
}
