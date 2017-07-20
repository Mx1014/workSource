package com.everhomes.community_approve;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.rest.community_approve.CommunityApproveServiceErrorCode;
import com.everhomes.rest.community_approve.GetTemplateByCommunityApproveIdResponse;
import com.everhomes.rest.community_approve.PostCommunityApproveFormCommand;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "COMMUNITY_APPROVE")
public class CommunityApproveFormHandler  implements GeneralFormModuleHandler {

    @Autowired
    private CommunityApproveService communityApproveService;

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private CommunityApproveProvider communityApproveProvider;

    @Override
    public PostGeneralFormDTO postGeneralForm(PostGeneralFormCommand cmd) {

        //审批申请表中插入数据

        PostCommunityApproveFormCommand cmd2 = new PostCommunityApproveFormCommand();
        cmd2.setApprovalId(cmd.getSourceId());
        cmd2.setValues(cmd.getValues());
        cmd2.setOrganizationId(cmd.getCurrentOrganizationId());

        GetTemplateByCommunityApproveIdResponse response = communityApproveService.postApprovalForm(cmd2);
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
        CommunityApprove ca = communityApproveProvider.getCommunityApproveById(cmd.getSourceId());
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(ca.getFormOriginId());
        if(form == null )
            throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                    CommunityApproveServiceErrorCode.ERROR_FORM_NOTFOUND, "form not found");
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
        customField.setFieldValue(JSONObject.toJSONString(ca));
        fieldDTOs.add(customField);

        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        dto.setFormFields(fieldDTOs);
//        dto.setCustomObject(JSONObject.toJSONString(ga));
        return dto;

    }
}
