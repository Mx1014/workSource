package com.everhomes.general_approval;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryCommand;
import com.everhomes.techpark.expansion.EnterpriseApplyEntryService;
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

        String json = cmd.getCustomObject();

        PostApprovalFormCommand cmd2 = JSONObject.parseObject(json, PostApprovalFormCommand.class);
        cmd2.setApprovalId(cmd.getSourceId());
        cmd2.setValues(cmd.getValues());
//        for (PostApprovalFormItem item: cmd.getValues()) {
//            switch (GeneralFormDataSourceType.fromCode(item.getFieldName())) {
//                case USER_NAME:
//                    cmd2.setApplyUserName(item.getFieldValue());
//                    break;
//                case USER_PHONE:
//                    cmd2.setContactPhone(item.getFieldValue());
//                    break;
//                case USER_COMPANY:
//                    //工作流images怎么传
//                    cmd2.setEnterpriseName(item.getFieldValue());
//                    break;
//                case ORGANIZATION_ID:
//
//                    break;
//                case USER_ADDRESS:
//                    break;
//
//            }
//        }

        GetTemplateByApprovalIdResponse response = generalApprovalService.postApprovalForm(cmd2);
        PostGeneralFormDTO dto = new PostGeneralFormDTO();
        dto.setCustomObject(JSONObject.toJSONString(response));
        return dto;
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormPost(PostGeneralFormCommand cmd) {
        return null;
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
        sourceIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
        fieldDTOs.add(sourceIdField);

        GeneralFormFieldDTO organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setDataSourceType(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
        organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        organizationIdField.setFieldName(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
        fieldDTOs.add(organizationIdField);

        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        dto.setFormFields(fieldDTOs);
        dto.setCustomObject(JSONObject.toJSONString(ga));
        return dto;
    }
}
