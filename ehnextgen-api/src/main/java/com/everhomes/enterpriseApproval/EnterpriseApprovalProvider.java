package com.everhomes.enterpriseApproval;

import com.everhomes.general_approval.GeneralApproval;

import java.util.List;

public interface EnterpriseApprovalProvider {

    List<EnterpriseApprovalGroup> listEnterpriseApprovalGroups();

    GeneralApproval getGeneralApprovalByTemplateId(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, Long templateId);

    List<EnterpriseApprovalTemplate> listEnterpriseApprovalTemplateByModuleId(Long moduleId);

}
