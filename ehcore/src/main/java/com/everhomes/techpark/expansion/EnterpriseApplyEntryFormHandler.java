package com.everhomes.techpark.expansion;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.entity.EntityType;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryCommand;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "EhLeasePromotions")
public class EnterpriseApplyEntryFormHandler implements GeneralFormModuleHandler {

    @Autowired
    private EnterpriseApplyEntryService enterpriseApplyEntryService;
    @Autowired
    private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
    @Autowired
    private GeneralFormService generalFormService;

    @Override
    public GeneralForm postGeneralForm(PostGeneralFormCommand cmd) {

        String json = cmd.getCustomObject();

        EnterpriseApplyEntryCommand cmd2 = JSONObject.parseObject(json, EnterpriseApplyEntryCommand.class);
        cmd2.setNamespaceId(cmd.getNamespaceId());
        cmd2.setCommunityId(cmd.getOwnerId());
        cmd2.setFormValues(cmd.getValues());
        for (PostApprovalFormItem item: cmd.getValues()) {
            switch (GeneralFormDataSourceType.fromCode(item.getFieldName())) {
                case USER_NAME:
                    cmd2.setApplyUserName(item.getFieldValue());
                    break;
                case USER_PHONE:
                    cmd2.setContactPhone(item.getFieldValue());
                    break;
                case USER_COMPANY:
                    //工作流images怎么传
                    cmd2.setEnterpriseName(item.getFieldValue());
                    break;
                case ORGANIZATION_ID:

                    break;
                case USER_ADDRESS:
                    break;

            }
        }

        enterpriseApplyEntryService.applyEntry(cmd2);
        return null;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        LeaseFormRequest request = enterpriseApplyEntryProvider.findLeaseRequestForm(cmd.getNamespaceId(),
                cmd.getOwnerId(), EntityType.COMMUNITY.getCode());

        GetTemplateByFormIdCommand cmd2 = new GetTemplateByFormIdCommand();
        cmd2.setFormId(request.getSourceId());

        GeneralFormDTO dto = generalFormService.getTemplateByFormId(cmd2);
    }
}
