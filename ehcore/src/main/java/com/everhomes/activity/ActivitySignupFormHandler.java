// @formatter:off
package com.everhomes.activity;

import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.general_approval.GeneralApprovalServiceErrorCode;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "ActivitySignup")
public class ActivitySignupFormHandler implements GeneralFormModuleHandler{

    @Autowired
    private GeneralFormProvider generalFormProvider;


    @Override
    public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {
        return null;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        List<GeneralForm> forms = getGeneralForum(cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType());
        GeneralFormDTO dto = new GeneralFormDTO();
        if (!CollectionUtils.isEmpty(forms)) {
            dto = ConvertHelper.convert(forms.get(0), GeneralFormDTO.class);
        }else {
            dto = ConvertHelper.convert(getDefaultGeneralForm(cmd.getOwnerType()), GeneralFormDTO.class);
        }
        return dto;
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
        return null;
    }


    GeneralForm getDefaultGeneralForm(String ownerType) {
        List<GeneralForm> forms = getGeneralForum(0, 0L, ownerType);
        if(forms == null || forms.isEmpty()) {
            throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                    GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "Init activity sign up form not found");
        }

        GeneralForm form = forms.get(0);
        if(form == null ) {
            throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                    GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "Init activity sign up form not found");
        }

        return form;
    }

    private List<GeneralForm> getGeneralForum(Integer namespaceId, Long ownerId, String ownerType) {
        List<GeneralForm> forms = this.generalFormProvider.queryGeneralForms(new ListingLocator(),
                Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                        SelectQuery<? extends Record> query) {
                        query.addConditions(Tables.EH_GENERAL_FORMS.NAMESPACE_ID.eq(namespaceId));
                        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(ownerId));
                        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(ownerType));
                        return query;
                    }
                });
        return forms;
    }
}
