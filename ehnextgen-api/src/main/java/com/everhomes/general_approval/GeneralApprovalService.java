package com.everhomes.general_approval;

import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.enterpriseApproval.VerifyApprovalTemplatesResponse;
import com.everhomes.rest.general_approval.*;

import java.util.List;
import java.util.Map;

import com.everhomes.rest.general_approval.CreateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalIdCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.ListActiveGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalResponse;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.UpdateGeneralApprovalCommand;

public interface GeneralApprovalService {

    GetTemplateByApprovalIdResponse getTemplateByApprovalId(GetTemplateByApprovalIdCommand cmd);

    GetTemplateByApprovalIdResponse postApprovalForm(PostApprovalFormCommand cmd);

    GeneralFormDTO createApprovalForm(CreateApprovalFormCommand cmd);

    ListGeneralFormResponse listApprovalForms(ListApprovalFormsCommand cmd);

    void deleteApprovalFormById(ApprovalFormIdCommand cmd);

    GeneralApprovalDTO createGeneralApproval(CreateGeneralApprovalCommand cmd);

    ListGeneralApprovalResponse listGeneralApproval(ListGeneralApprovalCommand cmd);

    List<GeneralApprovalScopeMapDTO> listGeneralApprovalScopes(Integer namespaceId, Long approvalId);

    GeneralApprovalDTO updateGeneralApproval(UpdateGeneralApprovalCommand cmd);

    GeneralApprovalDTO setGeneralApprovalForm(SetGeneralApprovalFormCommand cmd);

    void orderGeneralApprovals(OrderGeneralApprovalsCommand cmd);

    void deleteGeneralApproval(GeneralApprovalIdCommand cmd);

    GeneralFormDTO updateApprovalForm(UpdateApprovalFormCommand cmd);

    GeneralFormDTO getApprovalForm(ApprovalFormIdCommand cmd);

    void enableGeneralApproval(GeneralApprovalIdCommand cmd);

    void disableGeneralApproval(GeneralApprovalIdCommand cmd);

    Boolean checkNumberDefaultValue(String defaultValue, Map<String, Integer> map);

    GeneralApproval getGeneralApprovalByAttribute(Long ownerId, String attribute);

    ListGeneralApprovalResponse listActiveGeneralApproval(ListActiveGeneralApprovalCommand cmd);

    GeneralApprovalDTO verifyApprovalName(VerifyApprovalNameCommand cmd);

    void disableApprovalByFormOriginId(Long formOriginId, Long moduleId, String moduleType);

    String getUserRealName(GetUserRealNameCommand cmd);

    ListGeneralApprovalResponse listAvailableGeneralApprovals(ListGeneralApprovalCommand cmd);

    boolean checkTheApprovalScope(List<GeneralApprovalScopeMapDTO> scopes, OrganizationMember member);
}
