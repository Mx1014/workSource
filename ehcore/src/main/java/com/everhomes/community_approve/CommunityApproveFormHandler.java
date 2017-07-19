package com.everhomes.community_approve;

import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        return null;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        return null;
    }
}
