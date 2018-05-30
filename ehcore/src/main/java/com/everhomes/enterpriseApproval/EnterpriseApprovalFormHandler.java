package com.everhomes.enterpriseApproval;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowService;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalService;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalServiceErrorCode;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "ENTERPRISE_APPROVAL")
public class EnterpriseApprovalFormHandler implements GeneralFormModuleHandler {

    @Autowired
    private GeneralApprovalService generalApprovalService;

    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Override
    public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {
        //  get the basic data
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.currentUserId();
        Long approvalId = cmd.getSourceId();
        OrganizationMember member = organizationProvider.findDepartmentMemberByTargetIdAndOrgId(userId, cmd.getCurrentOrganizationId());
        if (member == null)
            member = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, cmd.getCurrentOrganizationId());
        //  check the approval is legal
        GeneralApproval approval = generalApprovalProvider.getGeneralApprovalById(approvalId);
        if (approval == null || GeneralApprovalStatus.RUNNING.getCode() != approval.getStatus())
            throw RuntimeErrorException.errorWith(EnterpriseApprovalServiceErrorCode.SCOPE, EnterpriseApprovalServiceErrorCode.ERROR_ILLEGAL_APPROVAL,
                    "The approval's status is not running");
/*        List<GeneralApprovalScopeMapDTO> scopes = generalApprovalService.listGeneralApprovalScopes(namespaceId, approval.getId());
        if (generalApprovalService.checkTheApprovalScope(scopes, member))
            throw RuntimeErrorException.errorWith(EnterpriseApprovalServiceErrorCode.SCOPE, EnterpriseApprovalServiceErrorCode.ERROR_ILLEGAL_APPROVAL,
                    "The user is not in the approval scope");*/
        //  post value
        PostApprovalFormCommand command = new PostApprovalFormCommand();
        command.setApprovalId(approvalId);
        command.setValues(cmd.getValues());
        command.setOrganizationId(cmd.getCurrentOrganizationId());
        generalApprovalService.postApprovalForm(command);
        //  return
        return ConvertHelper.convert(cmd, PostGeneralFormDTO.class);
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        //  get the basic data
        Long approvalId = cmd.getSourceId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Flow flow = flowService.getEnabledFlow(namespaceId, EnterpriseApprovalController.MODULE_ID,
                EnterpriseApprovalController.MODULE_TYPE, approvalId, EnterpriseApprovalController.APPROVAL_OWNER_TYPE);
        if(flow == null)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalServiceErrorCode.SCOPE,
                    EnterpriseApprovalServiceErrorCode.ERROR_DISABLE_APPROVAL_FLOW, "flow not found");
        Long formOriginId = flow.getFormOriginId();
        //  compatible with the previous data
        if(0 == formOriginId){
            GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(cmd.getSourceId());
            formOriginId = ga.getFormOriginId();
        }
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(formOriginId);
        if(form == null )
            throw RuntimeErrorException.errorWith(EnterpriseApprovalServiceErrorCode.SCOPE,
                    EnterpriseApprovalServiceErrorCode.ERROR_FORM_NOT_FOUND, "form not found");
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);

        //  organizationId is still needed
        GeneralFormFieldDTO organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setDataSourceType(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
        organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        organizationIdField.setFieldName(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
        fieldDTOs.add(organizationIdField);

        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
        return null;
    }
}
