package com.everhomes.authorization;

import java.util.List;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.entity.EntityType;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.general_approval.GeneralApprovalServiceErrorCode;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.techpark.expansion.EnterpriseApplyEntryProvider;
import com.everhomes.techpark.expansion.EnterpriseApplyEntryService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX +"zj_organization_auth")
public class OrganiztionAuthorizationFormHandler implements GeneralFormModuleHandler {

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
        AuthorizationModuleHandler handler = getAuthorizationHandler(cmd.getOwnerType()+cmd.getOwnerId());
        return handler.organiztionAuthorization(cmd);
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        GeneralFormDTO dto = new GeneralFormDTO();
        //通过sourceType获取表单
        GeneralForm form = getGeneralFormBySourceType(cmd.getSourceType());
        dto = ConvertHelper.convert(form, GeneralFormDTO.class);
       //改namespace,后面做handler
        dto.setOwnerType(EntityType.NAMESPACE.getCode());
        dto.setOwnerId(Long.valueOf(cmd.getNamespaceId()));
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }

    GeneralForm getGeneralFormBySourceType(String ownerType) {
        List<GeneralForm> forms = this.generalFormProvider.queryGeneralForms(new ListingLocator(),
                Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                        SelectQuery<? extends Record> query) {
                        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(ownerType));
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
    
    /**
     * 获取认证handler
     */
    public AuthorizationModuleHandler getAuthorizationHandler(String type) {
		String handler = AuthorizationModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + type;
		return PlatformContext.getComponent(handler);
	}

}
