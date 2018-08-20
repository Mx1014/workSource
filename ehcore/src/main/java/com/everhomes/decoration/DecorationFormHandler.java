package com.everhomes.decoration;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.rest.decoration.PostApprovalFormCommand;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.*;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "EhDecoration")
public class DecorationFormHandler implements GeneralFormModuleHandler {
    @Autowired
    private DecorationProvider decorationProvider;
    @Autowired
    private DecorationService decorationService;

    @Override
    public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {

        PostApprovalFormCommand cmd2 = new PostApprovalFormCommand();
        cmd2.setRequestId(cmd.getSourceId());
        cmd2.setValues(cmd.getValues());
        cmd2.setApprovalId(cmd.getOwnerId());
        Long flowcaseId = decorationService.postApprovalForm(cmd2);
        String url ="";
        if (flowcaseId != null && flowcaseId>0){
            url = processFlowURL(flowcaseId, FlowUserType.PROCESSOR.getCode(), DecorationController.moduleId);
        }
        PostGeneralFormDTO dto = ConvertHelper.convert(cmd,PostGeneralFormDTO.class);
        List<PostApprovalFormItem> items = new ArrayList<>();
        PostApprovalFormItem item = new PostApprovalFormItem();
        item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        JSONObject obj = new JSONObject();
        obj.put("url",url);
        item.setFieldValue(obj.toJSONString());

        items.add(item);
        dto.setValues(items);
        return dto;
    }

    private String processFlowURL(Long flowCaseId, String string, Long moduleId) {
        return "zl://workflow/detail?flowCaseId="+flowCaseId+"&flowUserType="+string+"&moduleId="+moduleId  ;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        return null;
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
        return null;
    }
}
